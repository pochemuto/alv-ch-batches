package ch.alv.batches.partnerjob.to.master.batch;

import ch.alv.batches.partnerjob.to.master.jaxb.ProspectiveJob;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.util.Map;
import java.util.UUID;

import static ch.alv.batches.commons.sql.SqlDataTypesHelper.fromGregorianCalendar;
import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.BATCH_JOB_PARAMETER_PARTNER_CODE;

/**
 * Contains all logic that transforms a raw {@link ProspectiveJob} object into
 * its proper internal state as an {@link OstePartnerRecord}.
 */
public class ProspectiveJobToPartnerJobConverter implements ItemProcessor<ProspectiveJob, OstePartnerRecord> {

    // TODO: convert these constants as configurable parameters
    public static final int DESC_MAX_LENGTH = 10000;
    public static final String DESC_TRUNCATE_SUFFIX = " [...]";

    private String partnerCode = null;

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        Map<String, JobParameter> parameters = stepExecution.getJobParameters().getParameters();
        partnerCode = parameters.get(BATCH_JOB_PARAMETER_PARTNER_CODE).getValue().toString();
    }

    @Override
    public OstePartnerRecord process(ProspectiveJob prospectiveJob) throws Exception {

        OstePartnerRecord partnerJob = new OstePartnerRecord();
        partnerJob.setQuelle(partnerCode);
        partnerJob.setBezeichnung(prospectiveJob.getStellentitel().trim());
        partnerJob.setBeschreibung(prospectiveJob.getTexte().getText1().trim() + " " +
                prospectiveJob.getTexte().getText2().trim() + " " +
                prospectiveJob.getTexte().getText3().trim() + " " +
                prospectiveJob.getTexte().getText4().trim() + " " +
                prospectiveJob.getTexte().getText5().trim());
        if (partnerJob.getBeschreibung().length() > DESC_MAX_LENGTH) {
            partnerJob.setBeschreibung(partnerJob.getBeschreibung()
                    .substring(0, DESC_MAX_LENGTH - DESC_TRUNCATE_SUFFIX.length()) + DESC_TRUNCATE_SUFFIX);
        }
        partnerJob.setBeschreibung(partnerJob.getBeschreibung().trim());
        partnerJob.setId(UUID.randomUUID().toString());
        partnerJob.setUntName(prospectiveJob.getKundenname().trim());
        partnerJob.setBerufsgruppe(Integer.valueOf(prospectiveJob.getMetadaten().getTmp10().trim()));
        partnerJob.setArbeitsortPlz(prospectiveJob.getMetadaten().getXtmp20().trim());
        partnerJob.setAnmeldeDatum(fromGregorianCalendar(prospectiveJob.getDatumStart().toGregorianCalendar()));
        String[] quotaData = prospectiveJob.getMetadaten().getXtmp30().trim().split("-");
        partnerJob.setPensumVon(Integer.valueOf(quotaData[0].trim()));
        if (quotaData.length == 1) {
            partnerJob.setPensumBis(Integer.valueOf(quotaData[0].trim()));
        } else {
            partnerJob.setPensumBis(Integer.valueOf(quotaData[1].trim()));
        }
        partnerJob.setUrlDetail(prospectiveJob.getUrlDirektlink().trim());
        partnerJob.setSprache(prospectiveJob.getSprache().trim());
        return partnerJob;
    }
}
