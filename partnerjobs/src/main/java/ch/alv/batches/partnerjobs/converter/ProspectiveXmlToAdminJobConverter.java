package ch.alv.batches.partnerjobs.converter;

import ch.alv.batches.partnerjobs.PartnerJob;
import ch.alv.batches.partnerjobs.jaxb.ProspectiveJob;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

/**
 * Contains all logic that transforms a raw prospectiveJob object into its proper internal state as an AdminJob.
 *
 * @since: 1.0.0
 */
public class ProspectiveXmlToAdminJobConverter implements ItemProcessor<ProspectiveJob, PartnerJob> {
    @Override
    public PartnerJob process(ProspectiveJob prospectiveJob) throws Exception {
        PartnerJob adminJob = new PartnerJob();
        adminJob.setTitle(prospectiveJob.getStellentitel().trim());
        adminJob.setDescription(prospectiveJob.getTexte().getText1().trim() + " " +
                prospectiveJob.getTexte().getText2().trim() + " " +
                prospectiveJob.getTexte().getText3().trim() + " " +
                prospectiveJob.getTexte().getText4().trim() + " " +
                prospectiveJob.getTexte().getText5().trim());
        if (adminJob.getDescription().length() > 2000) {
            adminJob.setDescription(adminJob.getDescription().substring(0,1996) + "...");
        }
        adminJob.setDescription(adminJob.getDescription().trim());
        adminJob.setId(UUID.randomUUID().toString());
        adminJob.setCompanyName(prospectiveJob.getKundenname().trim());
        adminJob.setJobGroup(Integer.valueOf(prospectiveJob.getMetadaten().getTmp10().trim()));
        adminJob.setJobLocation(prospectiveJob.getMetadaten().getXtmp20().trim());
        adminJob.setOnlineSince(prospectiveJob.getDatumStart().toGregorianCalendar().getTime());
        String[] quotaData = prospectiveJob.getMetadaten().getXtmp30().trim().split("-");
        adminJob.setQuotaFrom(Integer.valueOf(quotaData[0].trim()));
        if (quotaData.length == 1) {
            adminJob.setQuotaTo(Integer.valueOf(quotaData[0].trim()));
        } else {
            adminJob.setQuotaTo(Integer.valueOf(quotaData[1].trim()));
        }
        adminJob.setUrlDetail(prospectiveJob.getUrlDirektlink().trim());
        adminJob.setLanguage(prospectiveJob.getSprache().trim());
        return adminJob;
    }
}
