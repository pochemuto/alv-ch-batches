package ch.alv.batches.master.to.jobdesk;


import ch.alv.batches.master.to.jobdesk.jooq.Keys;
import ch.alv.batches.master.to.jobdesk.jooq.tables.records.JobRecord;
import ch.alv.batches.master.to.jobdesk.jooq.tables.records.LocationRecord;
import ch.alv.batches.master.to.jobdesk.model.*;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.*;

/**
 * Converts a master job (represented by the {@link JobRecord} class) into its
 * Jobdesk specific {@link JobdeskJob} representation.
 *
 * @since 1.0.0
 */
public class JobRecordToJobdeskJobConverter implements ItemProcessor<JobRecord, JobdeskJob> {

    private static final List<String> INTERNAL_SOURCES = Collections.singletonList("RAV"); // upcoming "EPA" ?

    private final DSLContext jooq;

    public JobRecordToJobdeskJobConverter(DSLContext jooq) {
        this.jooq = jooq;
    }

    @Override
    public JobdeskJob process(JobRecord in) throws Exception {

        // FIXME: if possible, find a way to ensure this step upstream ?!!!
        if (in.configuration() == null) {
            in.attach(jooq.configuration());
        }

        JobdeskJob out = new JobdeskJob();
        mapSimpleData(in, out);
        mapComplexData(in, out);

        return out;
    }

    private void mapSimpleData(JobRecord in, JobdeskJob out) {
        BeanUtils.copyProperties(in, out);
    }

    private void mapComplexData(JobRecord in, JobdeskJob out) throws SQLException {

        setTitle(in, out);
        setDescription(in, out);
        setFulltimeFlag(in, out);
        setExternalFlag(in, out);
        setISCOCodes(in, out);
        setCompany(in, out);

        if (!out.isExternal()) {
            setApplication(in, out);
            setContact(in, out);
        }

        retrieveAndSetJobLocations(in, out);

        retrieveAndSetLanguages(in, out);

    }

    private void setTitle(JobRecord in, JobdeskJob out) {
        out.setTitle(new JobdeskMultiLanguageValue(
                in.getTitleDe(),
                in.getTitleFr(),
                in.getTitleIt(),
                in.getTitleEn()));
    }

    private void setDescription(JobRecord in, JobdeskJob out) {
        out.setDescription(new JobdeskMultiLanguageValue(
                in.getDescriptionDe(),
                in.getDescriptionFr(),
                in.getDescriptionIt(),
                in.getDescriptionEn()));
    }

    private void retrieveAndSetJobLocations(JobRecord in, JobdeskJob out) throws SQLException {
        out.setLocation(new JobdeskJobLocation(
                in.getLocationRemarksDe(),
                in.getLocationRemarksFr(),
                in.getLocationRemarksIt(),
                in.getLocationRemarksEn()));

        List<LocationRecord> locationRecords = jooq.select()
                .from(LOCATION)
                .where(LOCATION.ID.in(
                                jooq.select(JOB_LOCATION.LOCATION_ID)
                                        .from(JOB_LOCATION)
                                        .where(JOB_LOCATION.JOB_ID.equal(in.getId()))))
                .fetchInto(LOCATION);
        // FIXME fetchInto sounds "overkill" in this case...

        JobdeskJobLocation location = new JobdeskJobLocation(in.getLocationRemarksDe());

        // FIXME is it correct to reuse the same JobdesLocation class ?
        // the jackson mapper outputs nulled fields (noisy...)
        locationRecords.forEach(l -> location.addLocation(Integer.parseInt(l.getZip()), l.getLat(), l.getLon()));
        out.setLocation(location);
    }

    private void retrieveAndSetLanguages(JobRecord in, JobdeskJob out) throws SQLException {
        List<JobdeskLanguage> languages = new LinkedList<>();
        in.fetchChildren(Keys.JOB_LANGUAGE__JOB_LANGUAGE_JOB_ID_FK).forEach(
            r -> languages.add(new JobdeskLanguage(r.getLanguageId(), r.getSkillSpoken(), r.getSkillWritten()))
        );
        out.setLanguages(languages);
    }

    //TODO fix to
    private void setApplication(JobRecord in, JobdeskJob out) {
        out.setApplication(new JobdeskJobApplication(
                in.getApplicationWritten(),
                in.getApplicationElectronical(),
                in.getApplicationPhone(),
                in.getApplicationPersonal(),
                in.getContactPhone(),
                in.getContactEmail()));
    }

    private void setCompany(JobRecord in, JobdeskJob out) {
        JobdeskJobCompany company = new JobdeskJobCompany();

        company.setName(in.getCompanyName());

        if (!out.isExternal()) {
            company.setAddress(new JobdeskCompanyAddress(
                    in.getCompanyAddress(),
                    in.getCompanyZip(),
                    in.getCompanyCity(),
                    in.getCompanyCountry()
            ));

            company.setPoAddress(new JobdeskCompanyPostboxAddress(
                    in.getCompanyPoNumber(),
                    String.valueOf(in.getCompanyPoZip()),
                    String.valueOf(in.getCompanyPoCity())
            ));

            company.setPhone(in.getCompanyPhone());
            company.setEmail(in.getCompanyEmail());
            company.setUrl(in.getCompanyUrl());
        }

        out.setCompany(company);
    }

    private void setContact(JobRecord in, JobdeskJob out) {
        out.setContact(new JobdeskJobContact(
                in.getContactGender(),
                in.getContactFirstName(),
                in.getContactLastName(),
                in.getContactPhone(),
                in.getContactEmail()
        ));
    }

    private void setFulltimeFlag(JobRecord in, JobdeskJob out) {
        if (100 == in.getQuotaFrom() && 100 == in.getQuotaTo()) {
            out.setFulltime(true);
        }
    }

    private void setExternalFlag(JobRecord in, JobdeskJob out) {
        if (!INTERNAL_SOURCES.contains(in.getSource())) {
            out.setExternal(true);
        }
    }

    private void setISCOCodes(JobRecord in, JobdeskJob out) {
        out.setIsco(new JobdeskIscoCode(in.getIsco08Id()));
    }

}
