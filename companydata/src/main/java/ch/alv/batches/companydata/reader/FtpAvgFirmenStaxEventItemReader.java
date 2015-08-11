package ch.alv.batches.companydata.reader;

import ch.alv.batches.companydata.jaxb.AvgFirma;
import ch.alv.batches.companydata.jaxb.Avggstelle;
import ch.alv.batches.jooq.tables.records.StagingAvgFirmenImportRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxUtils;
import org.springframework.batch.item.xml.stax.DefaultFragmentEventReader;
import org.springframework.batch.item.xml.stax.FragmentEventReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * A custom StaxReader implementation that extracts AvgFirmen and nested Avggstellen as {@link StagingAvgFirmenImportRecord} objects.
 */
public class FtpAvgFirmenStaxEventItemReader extends AbstractItemCountingItemStreamItemReader<StagingAvgFirmenImportRecord> implements
        ResourceAwareItemReaderItemStream<StagingAvgFirmenImportRecord>, InitializingBean {

    private static final Log logger = LogFactory.getLog(StaxEventItemReader.class);

    private FragmentEventReader fragmentReader;

    private XMLEventReader eventReader;

    private Unmarshaller unmarshaller;

    private Resource resource;

    private InputStream inputStream;

    private List<QName> fragmentRootElementNames;

    private boolean noInput;

    private boolean strict = true;

    private final Stack<StagingAvgFirmenImportRecord> gsCompanies = new Stack<>();

    public FtpAvgFirmenStaxEventItemReader() {
        setName(ClassUtils.getShortName(StaxEventItemReader.class));
    }

    /**
     * In strict mode the reader will throw an exception on
     * {@link #open(org.springframework.batch.item.ExecutionContext)} if the input resource does not exist.
     *
     * @param strict false by default
     */
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * @param unmarshaller maps xml fragments corresponding to records to objects
     */
    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    /**
     * @param fragmentRootElementName name of the root element of the fragment
     */
    public void setFragmentRootElementName(String fragmentRootElementName) {
        setFragmentRootElementNames(new String[]{fragmentRootElementName});
    }

    /**
     * @param fragmentRootElementNames list of the names of the root element of the fragment
     */
    public void setFragmentRootElementNames(String[] fragmentRootElementNames) {
        this.fragmentRootElementNames = new ArrayList<QName>();
        for (String fragmentRootElementName : fragmentRootElementNames) {
            this.fragmentRootElementNames.add(parseFragmentRootElementName(fragmentRootElementName));
        }
    }

    /**
     * Ensure that all required dependencies for the ItemReader to run are provided after all properties have been set.
     *
     * @throws IllegalArgumentException if the Resource, FragmentDeserializer or FragmentRootElementName is null, or if
     *                                  the root element is empty.
     * @throws IllegalStateException    if the Resource does not exist.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(unmarshaller, "The Unmarshaller must not be null.");
        Assert.notEmpty(fragmentRootElementNames, "The FragmentRootElementNames must not be empty");
        for (QName fragmentRootElementName : fragmentRootElementNames) {
            Assert.hasText(fragmentRootElementName.getLocalPart(), "The FragmentRootElementNames must not contain empty elements");
        }
    }

    /**
     * Responsible for moving the cursor before the StartElement of the fragment root.
     * <p>
     * This implementation simply looks for the next corresponding element, it does not care about element nesting. You
     * will need to override this method to correctly handle composite fragments.
     *
     * @return <code>true</code> if next fragment was found, <code>false</code> otherwise.
     * @throws NonTransientResourceException if the cursor could not be moved. This will be treated as fatal and
     *                                       subsequent calls to read will return null.
     */
    protected boolean moveCursorToNextFragment(XMLEventReader reader) throws NonTransientResourceException {
        try {
            while (true) {
                while (reader.peek() != null && !reader.peek().isStartElement()) {
                    reader.nextEvent();
                }
                if (reader.peek() == null) {
                    return false;
                }
                QName startElementName = ((StartElement) reader.peek()).getName();
                if (isFragmentRootElementName(startElementName)) {
                    return true;
                }
                reader.nextEvent();

            }
        } catch (XMLStreamException e) {
            throw new NonTransientResourceException("Error while reading from event reader", e);
        }
    }

    @Override
    protected void doClose() throws Exception {
        try {
            if (fragmentReader != null) {
                fragmentReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } finally {
            fragmentReader = null;
            inputStream = null;
        }

    }

    @Override
    protected void doOpen() throws Exception {
        Assert.notNull(resource, "The Resource must not be null.");

        noInput = true;
        if (!resource.exists()) {
            if (strict) {
                throw new IllegalStateException("Input resource must exist (reader is in 'strict' mode)");
            }
            logger.warn("Input resource does not exist " + resource.getDescription());
            return;
        }
        if (!resource.isReadable()) {
            if (strict) {
                throw new IllegalStateException("Input resource must be readable (reader is in 'strict' mode)");
            }
            logger.warn("Input resource is not readable " + resource.getDescription());
            return;
        }

        inputStream = resource.getInputStream();
        eventReader = XMLInputFactory.newInstance().createXMLEventReader(inputStream);
        fragmentReader = new DefaultFragmentEventReader(eventReader);
        noInput = false;

    }

    /**
     * Move to next fragment and map it to item.
     */
    @Override
    protected StagingAvgFirmenImportRecord doRead() throws Exception {
        StagingAvgFirmenImportRecord company = null;

        if (!gsCompanies.empty()) {
            company = gsCompanies.pop();
            return company;
        }

        if (noInput) {
            return null;
        }


        boolean success = false;
        try {
            success = moveCursorToNextFragment(fragmentReader);
        } catch (NonTransientResourceException e) {
            // Prevent caller from retrying indefinitely since this is fatal
            noInput = true;
            throw e;
        }
        if (success) {
            fragmentReader.markStartFragment();
            try {
                AvgFirma firma = (AvgFirma) unmarshaller.unmarshal(StaxUtils.getSource(fragmentReader));
                company = new StagingAvgFirmenImportRecord();
                company.setId(Integer.valueOf(firma.getId().trim()));
                company.setEmail(firma.getEmail().trim());
                company.setName(firma.getBezeichnung());
                company.setOrt(firma.getOrt());
                company.setPlz(firma.getPLZ());
                company.setStrasse(firma.getStrasse());
                company.setTelefonnummer(firma.getTelefonnummer());

                if (firma.getGStelle() != null) {
                    for (Avggstelle avgGs : firma.getGStelle()) {
                        StagingAvgFirmenImportRecord gsCompany = new StagingAvgFirmenImportRecord();
                        gsCompany.setId(Integer.valueOf(avgGs.getId().trim()));
                        gsCompany.setBetid("" + company.getId());
                        gsCompany.setName(firma.getBezeichnung());
                        gsCompany.setName2("GS");
                        gsCompany.setEmail(avgGs.getEmail());
                        gsCompany.setTelefonnummer(avgGs.getTelefonnummer());
                        gsCompany.setOrt(avgGs.getOrt());
                        gsCompany.setPlz(avgGs.getPLZ());
                        gsCompany.setStrasse(avgGs.getStrasse());
                        gsCompanies.push(gsCompany);
                    }
                }
            } finally {
                fragmentReader.markFragmentProcessed();
            }
        }

        return company;
    }

    /*
     * jumpToItem is overridden because reading in and attempting to bind an entire fragment is unacceptable in a
     * restart scenario, and may cause exceptions to be thrown that were already skipped in previous runs.
     */
    @Override
    protected void jumpToItem(int itemIndex) throws Exception {
        for (int i = 0; i < itemIndex; i++) {
            try {
                QName fragmentName = readToStartFragment();
                readToEndFragment(fragmentName);
            } catch (NoSuchElementException e) {
                if (itemIndex == (i + 1)) {
                    // we can presume a NoSuchElementException on the last item means the EOF was reached on the last run
                    return;
                } else {
                    // if NoSuchElementException occurs on an item other than the last one, this indicates a problem
                    throw e;
                }
            }
        }
    }

    /*
     * Read until the first StartElement tag that matches any of the provided fragmentRootElementNames. Because there may be any
     * number of tags in between where the reader is now and the fragment start, this is done in a loop until the
     * element type and name match.
     */
    private QName readToStartFragment() throws XMLStreamException {
        while (true) {
            XMLEvent nextEvent = eventReader.nextEvent();
            if (nextEvent.isStartElement()
                    && isFragmentRootElementName(((StartElement) nextEvent).getName())) {
                return ((StartElement) nextEvent).getName();
            }
        }
    }

    /*
     * Read until the first EndElement tag that matches the provided fragmentRootElementName. Because there may be any
     * number of tags in between where the reader is now and the fragment end tag, this is done in a loop until the
     * element type and name match
     */
    private void readToEndFragment(QName fragmentRootElementName) throws XMLStreamException {
        while (true) {
            XMLEvent nextEvent = eventReader.nextEvent();
            if (nextEvent.isEndElement()
                    && fragmentRootElementName.equals(((EndElement) nextEvent).getName())) {
                return;
            }
        }
    }

    private boolean isFragmentRootElementName(QName name) {
        for (QName fragmentRootElementName : fragmentRootElementNames) {
            if (fragmentRootElementName.getLocalPart().equals(name.getLocalPart())) {
                if (!StringUtils.hasText(fragmentRootElementName.getNamespaceURI())
                        || fragmentRootElementName.getNamespaceURI().equals(name.getNamespaceURI())) {
                    return true;
                }
            }
        }
        return false;
    }

    private QName parseFragmentRootElementName(String fragmentRootElementName) {
        String name = fragmentRootElementName;
        String nameSpace = null;
        if (fragmentRootElementName.contains("{")) {
            nameSpace = fragmentRootElementName.replaceAll("\\{(.*)\\}.*", "$1");
            name = fragmentRootElementName.replaceAll("\\{.*\\}(.*)", "$1");
        }
        return new QName(nameSpace, name, "");
    }
}
