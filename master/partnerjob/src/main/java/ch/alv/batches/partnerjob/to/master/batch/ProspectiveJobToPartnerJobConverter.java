package ch.alv.batches.partnerjob.to.master.batch;

import ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration;
import ch.alv.batches.partnerjob.to.master.jaxb.prospective.Inserat;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

/**
 * Contains all logic that transforms a raw {@link Inserat} object into
 * its proper internal state as an {@link OstePartnerRecord}.
 */
public class ProspectiveJobToPartnerJobConverter implements ItemProcessor<Inserat, OstePartnerRecord> {

    // TODO: convert these constants as configurable parameters
    public static final int DESC_MAX_LENGTH = 10000;
    public static final String DESC_TRUNCATE_SUFFIX = " [...]";
    private static final SimpleDateFormat AVAM_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'-00.00.00.000000'");

    private String partnerCode = null;

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        Map<String, JobParameter> parameters = stepExecution.getJobParameters().getParameters();
        partnerCode = parameters.get(PartnerJobToMasterConfiguration.BATCH_JOB_PARAMETER_PARTNER_CODE).getValue().toString();
    }

    @Override
    public OstePartnerRecord process(Inserat prospectiveJob) throws Exception {

        OstePartnerRecord partnerJob = new OstePartnerRecord();

        //
        // Mandatory Fields
        //
        partnerJob.setId(UUID.randomUUID().toString());
        partnerJob.setQuelle(partnerCode);
        partnerJob.setBezeichnung(prospectiveJob.getStellentitel().trim());
        partnerJob.setUntName(prospectiveJob.getKundenname().trim());
        partnerJob.setAnmeldeDatum(AVAM_DATETIME_FORMAT.format(prospectiveJob.getDatumStart().toGregorianCalendar().getTime()));

        //
        // Optional Fields
        //
        processUrls(prospectiveJob, partnerJob);
        processJobDescriptionData(prospectiveJob.getTexte(), partnerJob);
        processMetaData(prospectiveJob.getMetadaten(), partnerJob);

        // TODO: not available (yet) in legacy database: partnerJob.setSprache(prospectiveJob.getSprache().trim());

        return partnerJob;
    }

    private void processUrls(Inserat prospectiveJob, OstePartnerRecord partnerJob) {

        if (prospectiveJob.getUrlDirektlink() != null) {
            partnerJob.setUrlDetail(prospectiveJob.getUrlDirektlink().trim());
        }
        if (prospectiveJob.getUrlBewerber() != null) {
            partnerJob.setUrlBewerbung(prospectiveJob.getUrlBewerber().trim());
        }

    }

    private void processMetaData(Inserat.Metadaten metaData, OstePartnerRecord partnerJob) {

        if (metaData != null) {
            if (metaData.getJobcategory() != null) {
                partnerJob.setBerufsgruppe(metaData.getJobcategory().intValue());
            }
            if (metaData.getZipcode() != null) {
                partnerJob.setArbeitsortPlz(metaData.getZipcode().trim());
            }
            if (metaData.getPlace() != null) {
                // This field is not available yet in OSTE_PARTNER table, and is not required to be imported for the moment.
                // partnerJob.setArbeitsort(metaData.getPlace().trim());
            }
            if (metaData.getPensumvon() != null) {
                partnerJob.setPensumVon(metaData.getPensumvon().intValue());
            }
            if (metaData.getPensumbis() != null) {
                partnerJob.setPensumBis(metaData.getPensumbis().intValue());
            }
            if (metaData.getEducation() != null) {
                partnerJob.setAusbildungCode(metaData.getEducation());
            }
            if (metaData.getExperience() != null) {
                partnerJob.setErfahrungCode(metaData.getExperience());
            }
            if (metaData.getJobduration() != null) {
                partnerJob.setUnbefristetB(metaData.getJobduration().intValue() != 0);
            }

            //
            // And finally let's try to sanitize as good as we can...
            //

            if (partnerJob.getPensumVon() != null && partnerJob.getPensumBis() == null) {
                partnerJob.setPensumBis(partnerJob.getPensumVon());
            } else if (partnerJob.getPensumBis() != null && partnerJob.getPensumVon() == null) {
                partnerJob.setPensumVon(partnerJob.getPensumBis());
            }
        }

    }

    private void processJobDescriptionData(Inserat.Texte textData, OstePartnerRecord partnerJob) {

        if (textData != null) {
            partnerJob.setBeschreibung(textData.getText1().trim() + "\n" +
                    textData.getText2().trim() + "\n" +
                    textData.getText3().trim() + "\n" +
                    textData.getText4().trim() + "\n" +
                    textData.getText5().trim());
            if (textData.getText6() != null) {
                partnerJob.setBeschreibung(partnerJob.getBeschreibung() + "\n" + textData.getText6().trim());
            }
            if (textData.getText7() != null) {
                partnerJob.setBeschreibung(partnerJob.getBeschreibung() + "\n" + textData.getText7().trim());
            }
            if (partnerJob.getBeschreibung().length() > DESC_MAX_LENGTH) {
                partnerJob.setBeschreibung(partnerJob.getBeschreibung()
                        .substring(0, DESC_MAX_LENGTH - DESC_TRUNCATE_SUFFIX.length()) + DESC_TRUNCATE_SUFFIX);
            }
            partnerJob.setBeschreibung(partnerJob.getBeschreibung().trim());
        }

    }
}
