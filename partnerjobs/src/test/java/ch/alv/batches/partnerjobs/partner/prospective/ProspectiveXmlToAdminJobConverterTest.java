package ch.alv.batches.partnerjobs.partner.prospective;

import ch.alv.batches.partnerjobs.PartnerJob;
import ch.alv.batches.partnerjobs.jaxb.ProspectiveJob;
import ch.alv.batches.partnerjobs.converter.ProspectiveXmlToAdminJobConverter;
import org.junit.Assert;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
public class ProspectiveXmlToAdminJobConverterTest {

    private ProspectiveXmlToAdminJobConverter converter;
    private ProspectiveJob prospectiveXmlJob;

    //@Before
    public void init() {
        converter = new ProspectiveXmlToAdminJobConverter();
        prospectiveXmlJob = new ProspectiveJob();
        prospectiveXmlJob.setInseratId(11);

    }

    //@Test
    public void mapperTest() throws Exception {
        PartnerJob partnerJob = converter.process(prospectiveXmlJob);
        Assert.assertEquals(prospectiveXmlJob.getInseratId(), partnerJob.getId());
    }

}
