package ch.alv.batches.master.to.jobdesk;

import ch.alv.batches.master.to.jobdesk.jooq.tables.records.LocationRecord;
import ch.alv.batches.master.to.jobdesk.model.JobdeskLocation;
import ch.alv.batches.master.to.jobdesk.model.JobdeskLocationCoordinate;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import java.sql.SQLException;

/**
 * Converts a master location (represented by the {@link LocationRecord} class) into its
 * Jobdesk specific {@link JobdeskLocation} representation.
 *
 * @since 1.0.0
 */
public class LocationRecordToJobdeskLocationConverter implements ItemProcessor<LocationRecord, JobdeskLocation> {

    @Override
    public JobdeskLocation process(LocationRecord in) throws Exception {
        JobdeskLocation out = new JobdeskLocation();
        mapSimpleData(in, out);
        mapComplexData(in, out);
        return out;
    }

    private void mapSimpleData(LocationRecord in, JobdeskLocation out) {
        BeanUtils.copyProperties(in, out);
    }

    private void mapComplexData(LocationRecord in, JobdeskLocation out) throws SQLException {
        out.setCoords(new JobdeskLocationCoordinate(in.getLat(), in.getLon()));
    }

}
