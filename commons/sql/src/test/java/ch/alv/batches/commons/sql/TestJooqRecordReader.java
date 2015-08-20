package ch.alv.batches.commons.sql;

import ch.alv.batches.commons.sql.jooq.tables.records.TestJooqRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestJooqRecordReader implements ItemReader<TestJooqRecord> {

    private static final Log logger = LogFactory.getLog(TestJooqRecordReader.class);

    private List<TestJooqRecord> items;

    private Integer index = 0;

    public TestJooqRecordReader(int size) {
        items = IntStream.range(0, size)
                .boxed()
                .map(x -> new TestJooqRecord(x, Character.getName(x)))
                .collect(Collectors.toList());
    }

    @Override
    public TestJooqRecord read() throws Exception {
        TestJooqRecord currentItem = null;
        if (index < items.size()) {
            currentItem = items.get(index);
            if (logger.isDebugEnabled()) {
                logger.debug(currentItem);
            }
            index += 1;
        }
        return currentItem;
    }

}
