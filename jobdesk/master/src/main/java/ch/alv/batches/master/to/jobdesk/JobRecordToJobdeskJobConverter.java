package ch.alv.batches.master.to.jobdesk;



import ch.alv.batches.master.to.jobdesk.jooq.tables.records.JobRecord;
import ch.alv.batches.master.to.jobdesk.model.*;
import org.jooq.DSLContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static ch.alv.batches.master.to.jobdesk.jooq.Tables.JOB_LANGUAGE;
import static ch.alv.batches.master.to.jobdesk.jooq.Tables.JOB_LOCATION;
import static ch.alv.batches.master.to.jobdesk.jooq.Tables.LOCATION;


/**
 * Converts a master job (represented by the {@link JobRecord} class) into its
 * Jobdesk specific {@link JobdeskJob} representation.
 *
 * @since 1.0.0
 */
public class JobRecordToJobdeskJobConverter implements ItemProcessor<JobRecord, JobdeskJob> {

    private static final List<String> INTERNAL_SOURCES = Collections.singletonList("AVAM");

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private DSLContext jooq;

    @Override
    public JobdeskJob process(JobRecord in) throws Exception {
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
        // FIXME retrieveAndSetJobLocations(in, out);
        setApplication(in, out);
        setCompany(in, out);
        setContact(in, out);
        // FIXME retrieveAndSetLanguages(in, out);
        setFulltimeFlag(in, out);
        setExternalFlag(in, out);
        // FIXME setISCOCodes(in, out);
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
        out.setLocations(new JobdeskJobLocation(
                in.getLocationRemarksDe(),
                in.getLocationRemarksFr(),
                in.getLocationRemarksIt(),
                in.getLocationRemarksEn()));

        out.getLocations().setLocation(jdbcTemplate.query(
                jooq.select()
                        .from(JOB_LOCATION)
                        .join(LOCATION)
                        .on(JOB_LOCATION.LOCATION_ID.eq(LOCATION.ID))
                        .where(JOB_LOCATION.JOB_ID.eq(in.getId())).getSQL().replace("?", in.getId().toString()),
                (resultSet, i) -> {
                    JobdeskLocation location = new JobdeskLocation();
                    location.setZip(resultSet.getString(LOCATION.ZIP.getName()));
                    location.setCoords(new JobdeskLocationCoordinate(resultSet.getDouble(LOCATION.LAT.getName()), resultSet.getDouble(LOCATION.LON.getName())));
                    return location;
                }));
    }

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

    private void retrieveAndSetLanguages(JobRecord in, JobdeskJob out) throws SQLException {
        out.setLanguages(jdbcTemplate.query(jooq.select().from(JOB_LANGUAGE).where(JOB_LANGUAGE.JOB_ID.eq(in.getId())).getSQL().replace("?", in.getId().toString()), (resultSet, i) -> {
            return new JobdeskLanguage(
                    resultSet.getInt(JOB_LANGUAGE.LANGUAGE_ID.getName()),
                    resultSet.getInt(JOB_LANGUAGE.SKILL_SPOKEN.getName()),
                    resultSet.getInt(JOB_LANGUAGE.SKILL_WRITTEN.getName())
            );
        }));
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
        out.setIsco(new JobdeskISCOCode(
                in.getIscoLevel_1(),
                in.getIscoLevel_2(),
                in.getIscoLevel_3(),
                in.getIscoLevel_4()
        ));
    }

}
