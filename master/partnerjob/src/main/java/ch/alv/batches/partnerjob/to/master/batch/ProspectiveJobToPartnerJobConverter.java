package ch.alv.batches.partnerjob.to.master.batch;

import ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration;
import ch.alv.batches.partnerjob.to.master.jaxb.prospective.Inserat;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import java.util.Map;
import java.util.UUID;

/**
 * Contains all logic that transforms a raw {@link prospective.Inserat} object into
 * its proper internal state as an {@link OstePartnerRecord}.
 */
public class ProspectiveJobToPartnerJobConverter implements ItemProcessor<Inserat, OstePartnerRecord> {

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
        // Mandatory Fields (no defensive programming, see also ALV #5214)
        //
        String id = partnerCode + "-" + prospectiveJob.getInseratId() + "-" + prospectiveJob.getSprache();
        partnerJob.setId(UUID.nameUUIDFromBytes(id.getBytes()).toString());
        partnerJob.setQuelle(partnerCode);
        partnerJob.setBezeichnung(prospectiveJob.getStellentitel().trim());
        partnerJob.setUntName(prospectiveJob.getKundenname().trim());
        partnerJob.setAnmeldeDatum(PartnerJobImport.DB_DATETIME_FORMAT.format(prospectiveJob.getDatumStart().toGregorianCalendar().getTime()));

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
                partnerJob.setArbeitsortLand("CH");
            }
            if (metaData.getXplace() != null) {
                partnerJob.setArbeitsortText(metaData.getXplace().trim());
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
            String description = textData.getText1().trim() + "\n" +
                    textData.getText2().trim() + "\n" +
                    textData.getText3().trim() + "\n" +
                    textData.getText4().trim() + "\n" +
                    textData.getText5().trim();
            if (textData.getText6() != null) {
                description += "\n" + textData.getText6().trim();
            }
            if (textData.getText7() != null) {
                description += "\n" + textData.getText7().trim();
            }
            partnerJob.setBeschreibung(PartnerJobImport.abbreviateDescription(description));
        }

    }
}
