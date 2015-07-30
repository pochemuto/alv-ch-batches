package ch.alv.batches.companydata.reader;

import ch.alv.batches.companydata.Company;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by stibe on 30.07.15.
 */
public class CompanyRowMapper implements RowMapper<Company> {


    @Override
    public Company mapRow(ResultSet rs, int i) throws SQLException {
        Company company = new Company();
        company.setId((rs.getInt("ID")));
        company.setCompanyId(rs.getString("BETID"));
        company.setEmail(rs.getString("EMAIL"));
        company.setName(rs.getString("NAME"));
        company.setName2(rs.getString("NAME2"));
        company.setCity(rs.getString("ORT"));
        company.setZip(rs.getString("PLZ"));
        company.setStreet(rs.getString("STRASSE"));
        company.setPhone(rs.getString("TELEFONNUMMER"));
        return company;
    }
}
