package ch.alv.batches.partnerjob.to.master.converter;

import ch.alv.batches.partnerjob.to.master.jaxb.ProspectiveJob;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OsteAdminRecord;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

import static ch.alv.batches.commons.sql.SqlDataTypesHelper.fromGregorianCalendar;

/**
 * Contains all logic that transforms a raw {@link ProspectiveJob} object into
 * its proper internal state as an {@link OsteAdminRecord}.
 */
public class ProspectiveJobToAdminJobConverter implements ItemProcessor<ProspectiveJob, OsteAdminRecord> {

    private static final int DESC_MAX_LENGTH = 2000;

    @Override
    public OsteAdminRecord process(ProspectiveJob prospectiveJob) throws Exception {
        OsteAdminRecord adminJob = new OsteAdminRecord();
        adminJob.setBezeichnung(prospectiveJob.getStellentitel().trim());
        adminJob.setBeschreibung(prospectiveJob.getTexte().getText1().trim() + " " +
                prospectiveJob.getTexte().getText2().trim() + " " +
                prospectiveJob.getTexte().getText3().trim() + " " +
                prospectiveJob.getTexte().getText4().trim() + " " +
                prospectiveJob.getTexte().getText5().trim());
        if (adminJob.getBeschreibung().length() > DESC_MAX_LENGTH) {
            adminJob.setBeschreibung(adminJob.getBeschreibung().substring(0, DESC_MAX_LENGTH - 4) + "...");
        }
        adminJob.setBeschreibung(adminJob.getBeschreibung().trim());
        adminJob.setId(UUID.randomUUID().toString());
        adminJob.setUntName(prospectiveJob.getKundenname().trim());
        adminJob.setBerufsgruppe(Integer.valueOf(prospectiveJob.getMetadaten().getTmp10().trim()));
        adminJob.setArbeitsortPlz(prospectiveJob.getMetadaten().getXtmp20().trim());
        adminJob.setAnmeldeDatum(fromGregorianCalendar(prospectiveJob.getDatumStart().toGregorianCalendar()));
        String[] quotaData = prospectiveJob.getMetadaten().getXtmp30().trim().split("-");
        adminJob.setPensumVon(Short.valueOf(quotaData[0].trim()));
        if (quotaData.length == 1) {
            adminJob.setPensumBis(Short.valueOf(quotaData[0].trim()));
        } else {
            adminJob.setPensumBis(Short.valueOf(quotaData[1].trim()));
        }
        adminJob.setUrlDetail(prospectiveJob.getUrlDirektlink().trim());
        adminJob.setSprache(prospectiveJob.getSprache().trim());
        return adminJob;
    }
}
