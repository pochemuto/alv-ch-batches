package ch.alv.batches.partnerjobs.partner.prospective;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
public class ProspectiveXmlJobReader implements ItemReader<ProspectiveXmlJob> {

    @Override
    public ProspectiveXmlJob read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
