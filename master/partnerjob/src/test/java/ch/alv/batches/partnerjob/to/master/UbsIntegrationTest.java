package ch.alv.batches.partnerjob.to.master;

import ch.alv.batches.commons.test.SimpleTestApplication;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import ch.alv.batches.partnerjob.to.master.config.Partner;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.IMPORT_UBSJOBS_JOB;
import static ch.alv.batches.partnerjob.to.master.jooq.Tables.OSTE_PARTNER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestApplication.class)
@IntegrationTest
public class UbsIntegrationTest {

    private static final String UBS_CODE = Partner.Mode.UBS.toString();

    @Resource(name = IMPORT_UBSJOBS_JOB)
    private Job importUbsJob;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Resource
    private DSLContext jooq;

    private Set<OstePartnerRecord> checkUbsJobs;

    @Before
    public void initTestObjects() {
        checkUbsJobs = new TreeSet<>();
        checkUbsJobs.add(initJob1());
        checkUbsJobs.add(initJob2());
        checkUbsJobs.add(initJob3());
        checkUbsJobs.add(initJob4());
    }

    @Test
    public void testThatAPartnerImportIsComplete() throws
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            IOException,
            SQLException {

        assertNotNull(checkUbsJobs);

        Map<String, JobParameter> parameters = new HashMap<>();

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importUbsJob, parameters));

        List<OstePartnerRecord> fetchedJobs = jooq.selectFrom(OSTE_PARTNER)
                .where(OSTE_PARTNER.QUELLE.equal(UBS_CODE))
                .fetch()
                .sortAsc(OSTE_PARTNER.URL_DETAIL);

        assertEquals(13, fetchedJobs.size());
        assertEquals(checkUbsJobs, new TreeSet<>(fetchedJobs.subList(0, 4)));
    }

    private OstePartnerRecord initJob1() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("207c1783-3db0-37c0-9947-a057528ecb97");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("Java JSQ GQ");
        j.setBeschreibung(
                "Are you a great with numbers? Do you have an analytic mind? We’re looking for someone like that to help us:– prepare and analyze financial and management reports– provide action-oriented analysis and advice, supporting the business in making sound decisions– coordinate financial planning and forecasting activities\n" +
                "You’ll be working in the [title] team in [Business Location]. Our role is to [for example, support line managers and employees across all business divisions and other human resources colleagues]. We help by [for example, giving advice on various regional reward products and policies].\n" +
                "You have:<BR>&nbsp;<UL dir=ltr><LI>a university degree</LI><LI>some investment banking experience or familiarity with investment products</LI></UL>You are:<BR>&nbsp;<UL dir=ltr><LI>well organized, with a sharp eye for detail</LI><LI>a critical thinker who communicates clearly (you are very logical, but still very human)</LI><LI>fluent in [language(s)]</LI></UL>\n" +
                "Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the background support. That's what we do. And we do it for private clients, institutions and corporations around the world. We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
                "<FONT style=\"FONT-SIZE: medium\"><FONT style=\"FONT-FAMILY: frutiger 45 light; size: undefined\">Together. That’s how we do things. We offer talented people around the world a supportive, stimulating and diverse working environment. We’ll value your passion and commitment. And reward your performance.</FONT></FONT>\n" +
                "Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
                "UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Zürich");
        j.setArbeitsortLand("CH");
        j.setPensumVon(100);
        j.setPensumBis(100);
        j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49739BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49739BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(false);
        // j.setSprache("de");
        return j;
    }

    private OstePartnerRecord initJob2() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("b403e5f5-6c7c-3560-88d9-d3eb3869b1b7");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("Sales Role");
        j.setBeschreibung(
                "<S1>Your profile</S1>Are you a great with numbers? Do you have an analytic mind? We’re looking for someone like that to help us:– prepare and analyze financial and management reports– provide action-oriented analysis and advice, supporting the business in making sound decisions– coordinate financial planning and forecasting activities\n" +
                "You’ll be working in the [title] team in [Business Location]. Our role is to [for example, support line managers and employees across all business divisions and other human resources colleagues]. We help by [for example, giving advice on various regional reward products and policies].\n" +
                "You have:<BR> <UL dir=ltr><LI>a university degree</LI><LI>some investment banking experience or familiarity with investment products</LI></UL>You are:<BR> <UL dir=ltr><LI>well organized, with a sharp eye for detail</LI><LI>a critical thinker who communicates clearly (you are very logical, but still very human)</LI><LI>fluent in [language(s)]</LI></UL>\n" +
                "Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the support functions. That's what we do. And we do it for private and institutional clients as well as corporations around the world.<br><br>We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
                "Together. That’s how we do things. We offer talented people around the world a supportive, stimulating and diverse working environment. We’ll value your passion and commitment. And reward your performance.<BR><BR>Why UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
                "UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce.<br><br>"
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("United Kingdom");
        j.setArbeitsortLand(null);  // identification of foreign countries is not supported yet
        j.setPensumVon(0);
        j.setPensumBis(100);
        j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49740BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49740BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("de");
        return j;
    }

    private OstePartnerRecord initJob3() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("9a9cc1f4-b64d-358a-8663-e4a8ff93c6cc");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("Test Job Room Role");
        j.setBeschreibung(
                "Do you know how to make things run smoothly?  Are you a detailed orientated multitasker?  We are looking for someone like that who can provide executive support to the Vice Chairman and their office.You'll:-  manage the Vice Chairman's daily schedule from calendar planning to responding to emails ­- organize meetings, conferences and calls: prepare materials, briefings and meet and greet guests­- attend meetings and produce minutes (you'll need to be fluent in English, Mandarin and German)- schedule and organize international travel arrangements including transport, accommodation and visas ­- manage the personal correspondence of the Vice Chairman from  responding to invitations, congratulations and credit card expenses ­- support the Vice Chairman's management team and deputize for the Business Manager during absences\n" +
                "You'll be working in the Vice Chairman's Office as part of a small team of Executive Assistants, senior executives and experts. You will work closely with the Business Manager to support the Vice Chairman and their office in daily work. This is an exciting opportunity to closely work with the Vice Chairman of a globally operating bank. You'll get to collaborate globally across the bank's divisions.\n" +
                "You have:­- Experience working at a top-executive level ­- Great communication skills with the ability to interact with senior stakeholders ­- Outstanding organizational skills (you thrive under pressure and juggling tasks is a breeze)­- Microsoft Office skills (Excel, Word, PowerPoint, Outlook) and a typing speed of 75 WPM­- A good understanding of the Swiss and international financial market environmentYou are:­- Flexible, proactive and a strong team player­- Solution oriented with a calm personality and high stress resilience­- Fluent in English, Mandarin and German\n" +
                "Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the support functions. That's what we do. And we do it for private and institutional clients as well as corporations around the world.<br><br>We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
                "Together. That’s how we do things. We offer talented people around the world a supportive, stimulating and diverse working environment. We’ll value your passion and commitment. And reward your performance.<BR><BR>Why UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
                "UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Hong Kong");
        j.setArbeitsortLand(null); // identification of foreign countries is not supported yet
        j.setPensumVon(100);
        j.setPensumBis(100);
        j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49822BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49822BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("it");
        return j;
    }

    private OstePartnerRecord initJob4() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("45b7ffd6-e0b2-3a83-aea0-2d166bd59288");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("Executive Assistant to the Vice Chairman PL09");
        j.setBeschreibung(
            "Do you know how to make things run smoothly?  Are you a detailed orientated multitasker?  We are looking for someone like that who can provide executive support to the Vice Chairman and their office.You'll:-  manage the Vice Chairman's daily schedule from calendar planning to responding to emails ­- organize meetings, conferences and calls: prepare materials, briefings and meet and greet guests­- attend meetings and produce minutes (you'll need to be fluent in English, Mandarin and German)- schedule and organize international travel arrangements including transport, accommodation and visas ­- manage the personal correspondence of the Vice Chairman from  responding to invitations, congratulations and credit card expenses ­- support the Vice Chairman's management team and deputize for the Business Manager during absences\n" +
            "You'll be working in the Vice Chairman's Office as part of a small team of Executive Assistants, senior executives and experts. You will work closely with the Business Manager to support the Vice Chairman and their office in daily work. This is an exciting opportunity to closely work with the Vice Chairman of a globally operating bank. You'll get to collaborate globally across the bank's divisions.\n" +
            "You have:­- Experience working at a top-executive level ­- Great communication skills with the ability to interact with senior stakeholders ­- Outstanding organizational skills (you thrive under pressure and juggling tasks is a breeze)­- Microsoft Office skills (Excel, Word, PowerPoint, Outlook) and a typing speed of 75 WPM­- A good understanding of the Swiss and international financial market environmentYou are:­- Flexible, proactive and a strong team player­- Solution oriented with a calm personality and high stress resilience­- Fluent in English, Mandarin and German\n" +
            "Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the support functions. That's what we do. And we do it for private and institutional clients as well as corporations around the world.<br><br>We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
            "Together. That’s how we do things. We offer talented people around the world a supportive, stimulating and diverse working environment. We’ll value your passion and commitment. And reward your performance.<BR><BR>Why UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
            "Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
            "UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce."
        );
        j.setBerufsgruppe(1);
        j.setUntName("UBS");
        j.setArbeitsortText("Poland");
        j.setArbeitsortLand(null); // identification of foreign countries is not supported yet
        j.setPensumVon(0);
        j.setPensumBis(100);
        j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49832BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49832BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(false);
        // j.setSprache("fr");
        return j;
    }

}
