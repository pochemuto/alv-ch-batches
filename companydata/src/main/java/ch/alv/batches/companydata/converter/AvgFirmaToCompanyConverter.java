package ch.alv.batches.companydata.converter;

import ch.alv.batches.companydata.Company;
import ch.alv.batches.companydata.jaxb.AvgFirma;
import org.springframework.batch.item.ItemProcessor;

/**
 * Contains all logic that transforms a raw {@link AvgFirma} object into
 * its proper internal state as a {@link Company}.
 *
 * @since: 1.0.0
 */
public class AvgFirmaToCompanyConverter implements ItemProcessor<AvgFirma, Company> {
    @Override
    public Company process(AvgFirma avgFirma) throws Exception {
        Company company = new Company();
        company.setId(Integer.valueOf(avgFirma.getId().trim()));
        company.setCompanyId(avgFirma.getId().trim());
        company.setEmail(avgFirma.getEmail().trim());
        company.setName(avgFirma.getBezeichnung());
        company.setCity(avgFirma.getOrt());
        company.setZip(avgFirma.getPLZ());
        company.setStreet(avgFirma.getStrasse());
        company.setPhone(avgFirma.getTelefonnummer());
        return company;
    }
}
