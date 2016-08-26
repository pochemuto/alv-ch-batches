package ch.alv.batches.partnerjob.to.master.batch;


import ch.alv.batches.partnerjob.to.master.config.Partner;
import ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration;
import ch.alv.batches.partnerjob.to.master.jaxb.prospective.Inserat;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class ProspectiveJobXmlItemReader extends StaxEventItemReader<Inserat> {

    private String partnerCode = null;

    private HashMap<String, Partner> partners;

    public ProspectiveJobXmlItemReader(HashMap<String, Partner> partners) {
        this.partners = partners;
        this.setStrict(true); // be sure to fail if the resource is not set when opening the reader
    }

    @BeforeStep
    public void beforeNewStepExecution(StepExecution stepExecution) throws MalformedURLException {
        // state reset on each new step execution
        this.setResource(null);

        Map<String, JobParameter> parameters = stepExecution.getJobParameters().getParameters();
        partnerCode = parameters.get(PartnerJobToMasterConfiguration.BATCH_JOB_PARAMETER_PARTNER_CODE).getValue().toString();
        if (partners.containsKey(partnerCode) && partners.get(partnerCode).getMode() == Partner.Mode.PROSPECTIVE) {
            this.setResource(new UrlResource(partners.get(partnerCode).getUri()));
        }
    }
}
