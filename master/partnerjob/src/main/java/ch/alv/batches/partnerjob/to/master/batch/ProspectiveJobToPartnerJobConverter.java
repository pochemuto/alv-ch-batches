package ch.alv.batches.partnerjob.to.master.batch;

import ch.alv.batches.partnerjob.to.master.jaxb.ProspectiveJob;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.BATCH_JOB_PARAMETER_PARTNER_CODE;

/**
 * Contains all logic that transforms a raw {@link ProspectiveJob} object into
 * its proper internal state as an {@link OstePartnerRecord}.
 */
public class ProspectiveJobToPartnerJobConverter implements ItemProcessor<ProspectiveJob, OstePartnerRecord> {

    // TODO: convert these constants as configurable parameters
    public static final int DESC_MAX_LENGTH = 10000;
    public static final String DESC_TRUNCATE_SUFFIX = " [...]";
    private static final SimpleDateFormat AVAM_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'-00.00.00.000000'");

    private String partnerCode = null;

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        Map<String, JobParameter> parameters = stepExecution.getJobParameters().getParameters();
        partnerCode = parameters.get(BATCH_JOB_PARAMETER_PARTNER_CODE).getValue().toString();
    }

    @Override
    public OstePartnerRecord process(ProspectiveJob prospectiveJob) throws Exception {

        OstePartnerRecord partnerJob = new OstePartnerRecord();
        partnerJob.setId(UUID.randomUUID().toString());
        partnerJob.setQuelle(partnerCode);
        partnerJob.setBezeichnung(prospectiveJob.getStellentitel().trim());
        processBeschreibung(prospectiveJob, partnerJob);
        partnerJob.setUntName(prospectiveJob.getKundenname().trim());
        partnerJob.setBerufsgruppe((long) prospectiveJob.getMetadaten().getJobcategory()); // FIXME switch to Integer in jOOQ
        partnerJob.setArbeitsortPlz(prospectiveJob.getMetadaten().getZipcode().trim());
        partnerJob.setAnmeldeDatum(AVAM_DATETIME_FORMAT.format(prospectiveJob.getDatumStart().toGregorianCalendar().getTime()));
        partnerJob.setPensumVon(prospectiveJob.getMetadaten().getPensumvon());
        partnerJob.setPensumBis(prospectiveJob.getMetadaten().getPensumbis());
        partnerJob.setUrlDetail(prospectiveJob.getUrlDirektlink().trim());
        // not available in legacy database: partnerJob.setSprache(prospectiveJob.getSprache().trim());

        return partnerJob;
    }

    private void processBeschreibung(ProspectiveJob prospectiveJob, OstePartnerRecord partnerJob) {
        partnerJob.setBeschreibung(prospectiveJob.getTexte().getText1().trim() + "\n" +
                prospectiveJob.getTexte().getText2().trim() + "\n" +
                prospectiveJob.getTexte().getText3().trim() + "\n" +
                prospectiveJob.getTexte().getText4().trim() + "\n" +
                prospectiveJob.getTexte().getText5().trim());
        if (prospectiveJob.getTexte().getText6() != null) {
            partnerJob.setBeschreibung(partnerJob.getBeschreibung() + "\n" + prospectiveJob.getTexte().getText6().trim());
        }
        if (prospectiveJob.getTexte().getText7() != null) {
            partnerJob.setBeschreibung(partnerJob.getBeschreibung() + "\n" + prospectiveJob.getTexte().getText7().trim());
        }
        if (partnerJob.getBeschreibung().length() > DESC_MAX_LENGTH) {
            partnerJob.setBeschreibung(partnerJob.getBeschreibung()
                    .substring(0, DESC_MAX_LENGTH - DESC_TRUNCATE_SUFFIX.length()) + DESC_TRUNCATE_SUFFIX);
        }
        partnerJob.setBeschreibung(partnerJob.getBeschreibung().trim());
    }
}
