package ch.alv.batches.partnerjob.to.master.batch;

import ch.alv.batches.partnerjob.to.master.jaxb.ubs.Inserat;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contains all logic that transforms a raw {@link ch.alv.batches.partnerjob.to.master.jaxb.ubs.Inserat} object into
 * its proper internal state as an {@link ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord}.
 */
public class UbsJobToPartnerJobConverter implements ItemProcessor<Inserat, OstePartnerRecord> {

    private String partnerCode = "ubs";

    private static final String UBS_MULTIVALUED_STRING_SEPARATOR_REGEX = "\\|";

    private static final Integer JOBROOM_CATEGORY_ADMIN_MANAGEMENT = 1;
    private static final Integer JOBROOM_CATEGORY_FINANCE_LAW = 2;
    private static final Integer JOBROOM_CATEGORY_MARKETING_COMMUNICATION = 3;
    private static final Integer JOBROOM_CATEGORY_ENGINEERING_COMPUTING = 4;
    private static final Integer JOBROOM_CATEGORY_SERVICES_OTHERS = 14;

    private static final Map<String, Integer> UBS_JOB_CATEGORIES = new HashMap<String, Integer>() {
        {
            put("Administration and Support", JOBROOM_CATEGORY_ADMIN_MANAGEMENT);
            put("Audit", JOBROOM_CATEGORY_FINANCE_LAW);
            put("C&ORC (Compliance and Operational Risk)", JOBROOM_CATEGORY_ADMIN_MANAGEMENT);
            put("Client Advisors/Relationship Managers", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Corporate Infrastructure & Facilities", JOBROOM_CATEGORY_ADMIN_MANAGEMENT);
            put("Financial Advisors", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Financial Controlling & Accounting", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Fund Services", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Human Resources", JOBROOM_CATEGORY_ADMIN_MANAGEMENT);
            put("Information Technology", JOBROOM_CATEGORY_ENGINEERING_COMPUTING);
            put("Investment Banking", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Legal", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Management and Business Support", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Management Group", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Marketing and Communications", JOBROOM_CATEGORY_MARKETING_COMMUNICATION);
            put("Operations", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Other", JOBROOM_CATEGORY_SERVICES_OTHERS);
            put("Outsourcing / Offshoring", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Prime Services", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Process Project and Program Management", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Product and Portfolio Management", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Product Management and Development", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Quantitative Analysis", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Research & Analysis", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Risk Control", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Sales", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Sales Trading", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Trading", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Training & Development", JOBROOM_CATEGORY_FINANCE_LAW);
            put("Wealth Planning", JOBROOM_CATEGORY_FINANCE_LAW);
        }
    };

    @Override
    public OstePartnerRecord process(Inserat ubsJob) throws Exception {

        OstePartnerRecord partnerJob = new OstePartnerRecord();

        //
        // Mandatory Fields (no defensive programming, see also ALV #5214)
        //
        String id = ubsJob.getKundenname() + "-" + ubsJob.getInseratId() + "-" + ubsJob.getSprache();
        partnerJob.setId(UUID.nameUUIDFromBytes(id.getBytes()).toString());

        partnerJob.setQuelle(partnerCode);
        partnerJob.setBezeichnung(ubsJob.getStellentitel().trim());
        partnerJob.setUntName(ubsJob.getKundenname().trim());
        partnerJob.setAnmeldeDatum(PartnerJobImport.DB_DATETIME_FORMAT.format(ubsJob.getDatumStart().toGregorianCalendar().getTime()));

        //
        // Optional Fields
        //
        processUrls(ubsJob, partnerJob);
        processJobDescriptionData(ubsJob.getTexte(), partnerJob);
        processMetaData(ubsJob.getMetadaten(), partnerJob);

        // TODO: not available (yet) in legacy database: partnerJob.setSprache(ubsJob.getSprache().trim());

        return partnerJob;
    }

    private void processUrls(Inserat prospectiveJob, OstePartnerRecord partnerJob) {

        if (prospectiveJob.getUrlBewerber() != null) {
            partnerJob.setUrlBewerbung(prospectiveJob.getUrlBewerber().trim());
        }
        if (prospectiveJob.getUrlDirektlink() != null && !prospectiveJob.getUrlDirektlink().trim().equals(partnerJob.getUrlBewerbung())) {
            // Only import the "Job Detail URL" information if its value is different from the "Application URL"
            partnerJob.setUrlDetail(prospectiveJob.getUrlDirektlink().trim());
        }

    }

    private void processMetaData(Inserat.Metadaten metaData, OstePartnerRecord partnerJob) {

        if (metaData != null) {
            processJobLocation(metaData.getLocation(), metaData.getCity(), partnerJob);
            processJobCategories(metaData.getJobcategory(), partnerJob);
            processJobDurations(metaData.getJobduration(), partnerJob);
        }

    }

    private void processJobLocation(String jobLocation, String jobCity, OstePartnerRecord partnerJob) {
        final String SWITZERLAND_REGEX = "^(Switzerland|Schweiz|Suisse|Svizzera) - ";

        if (!StringUtils.isEmpty(jobLocation)) {
            if (jobLocation.matches(SWITZERLAND_REGEX + ".+")) {
                String location = jobLocation.split(SWITZERLAND_REGEX, 2)[1].trim();
                partnerJob.setArbeitsortText(composeLocation(jobCity, location));
                partnerJob.setArbeitsortLand("CH");
            } else {
                partnerJob.setArbeitsortText(composeLocation(jobCity, jobLocation));
                partnerJob.setArbeitsortLand(null); // For now, a null value is interpreted as "not in Switzerland"
            }
        }
    }

    private static String composeLocation(String city, String location) {
        if (location.compareToIgnoreCase(city) == 0 || StringUtils.isEmpty(city)) {
            return location;
        } else {
            return location + " - " + city;
        }
    }

    private void processJobDurations(String ubsJobDurations, OstePartnerRecord partnerJob) {
        if (ubsJobDurations != null) {
            List<String> jobDurations = Arrays.asList(ubsJobDurations.trim().split(UBS_MULTIVALUED_STRING_SEPARATOR_REGEX));
            partnerJob.setUnbefristetB(!jobDurations.contains("Temporary"));
            if (jobDurations.contains("Full Time") && !jobDurations.contains("Part Time")) {
                partnerJob.setPensumVon(100);
            } else {
                partnerJob.setPensumVon(0);
            }
            partnerJob.setPensumBis(100);

            //
            // And finally let's try to sanitize as good as we can...
            //
            if (partnerJob.getPensumVon() != null && partnerJob.getPensumBis() == null) {
                partnerJob.setPensumBis(partnerJob.getPensumVon());
            } else if (partnerJob.getPensumBis() != null && partnerJob.getPensumVon() == null) {
                partnerJob.setPensumVon(partnerJob.getPensumBis());
            }
        } else {
            partnerJob.setPensumVon(0);
            partnerJob.setPensumBis(100);
        }
    }

    private void processJobCategories(String ubsCategories, OstePartnerRecord partnerJob) {

        int category = JOBROOM_CATEGORY_FINANCE_LAW; // default value
        if (ubsCategories != null) {
            // Pick a unique category, as we don't support multiple values
            List<String> jobCategories = Arrays.asList(ubsCategories.trim().split(UBS_MULTIVALUED_STRING_SEPARATOR_REGEX));
            if (jobCategories.size() > 0) {
                Integer firstValid = null;
                for (String k : jobCategories) {

                    Integer c = UBS_JOB_CATEGORIES.get(k); // returned 'c' can be null if key is unknown or invalid

                    if (c != null && firstValid == null) {
                        firstValid = c;
                    }
                    if (c != null) {
                        category = c;
                    }

                    if (category == JOBROOM_CATEGORY_FINANCE_LAW) {
                        // Give strong preference to JOBROOM_CATEGORY_FINANCE_LAW
                        break;
                    }
                }
                if (category != JOBROOM_CATEGORY_FINANCE_LAW) {
                    // With multiple values, without Finance category, pick the first from the list
                    category = firstValid;
                }
            }
        }
        partnerJob.setBerufsgruppe(category);

    }

    private void processJobDescriptionData(Inserat.Texte textData, OstePartnerRecord partnerJob) {

        if (textData != null) {
            String description = textData.getText1().trim() + "\n" +
                    textData.getText2().trim() + "\n" +
                    textData.getText3().trim() + "\n" +
                    textData.getText4().trim() + "\n" +
                    textData.getText5().trim() + "\n" +
                    textData.getText6().trim() + "\n" +
                    textData.getText7().trim();

            partnerJob.setBeschreibung(PartnerJobImport.abbreviateDescription(description));
        }

    }
}
