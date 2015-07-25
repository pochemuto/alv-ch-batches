package ch.alv.batches.partnerjobs.partner.prospective;

import ch.alv.batches.partnerjobs.PartnerJob;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

/**
 * Contains all logic that transforms a raw prospectiveJob object into its proper internal state as an AdminJob.
 *
 * @since: 1.0.0
 */
public class ProspectiveXmlToAdminJobConverter implements ItemProcessor<ProspectiveXmlJob, PartnerJob> {
    @Override
    public PartnerJob process(ProspectiveXmlJob prospectiveXmlJob) throws Exception {
        PartnerJob adminJob = new PartnerJob();
        BeanUtils.copyProperties(adminJob, prospectiveXmlJob, PartnerJob.class);
        return adminJob;
    }
}
