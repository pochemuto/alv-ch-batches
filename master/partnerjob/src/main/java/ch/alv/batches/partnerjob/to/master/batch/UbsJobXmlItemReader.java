package ch.alv.batches.partnerjob.to.master.batch;


import ch.alv.batches.partnerjob.to.master.config.Partner;
import ch.alv.batches.partnerjob.to.master.jaxb.ubs.Inserat;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;

import java.util.HashMap;

public class UbsJobXmlItemReader extends StaxEventItemReader<Inserat> {

    private final static String ubsKey = Partner.Mode.UBS.toString();

    public UbsJobXmlItemReader(HashMap<String, Partner> partners) {
        this.setStrict(true); // be sure to fail if the resource is not set when opening the reader

        if (partners.containsKey(ubsKey) && partners.get(ubsKey).getMode() == Partner.Mode.UBS) {
            this.setResource(new FileSystemResource(partners.get(ubsKey).getUri()));
        }
    }

}
