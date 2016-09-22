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
        checkUbsJobs.add(initJob5InEnglish());
        checkUbsJobs.add(initJob5InGerman());
        checkUbsJobs.add(initJob6InEnglish());
        checkUbsJobs.add(initJob6InGerman());
        checkUbsJobs.add(initJob6InFrench());
        checkUbsJobs.add(initJob6InItalian());
        checkUbsJobs.add(initJob7());
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
                .sortAsc(OSTE_PARTNER.BEZEICHNUNG);

        assertEquals(14, fetchedJobs.size());

// Debugging Helper:
//        for (OstePartnerRecord c: checkUbsJobs) {
//            OstePartnerRecord d = jooq.selectFrom(OSTE_PARTNER).where(OSTE_PARTNER.ID.equal(c.getId())).fetchOne();
//            if (c.compareTo(d) != 0) {
//                System.err.println("CHECK: " + c.toString());
//                System.err.println("DB: " + d.toString());
//                System.err.println("GAP: " + c.compareTo(d));
//            }
//        }

        assertEquals(checkUbsJobs, new TreeSet<>(fetchedJobs.subList(0, 11)));
    }

    private OstePartnerRecord initJob1() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("207c1783-3db0-37c0-9947-a057528ecb97");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("1. Java JSQ GQ");
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
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49739BR&Codes=Ijob-room");
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
        j.setBezeichnung("2. Sales Role");
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
        j.setArbeitsortText("United Kingdom - London");
        j.setArbeitsortLand(null);  // identification of foreign countries is not supported yet
        j.setPensumVon(0);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49740BR&Codes=Ijob-room");
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
        j.setBezeichnung("3. Test Job Room Role");
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
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49822BR&Codes=Ijob-room");
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
        j.setBezeichnung("4. Executive Assistant to the Vice Chairman PL09");
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
        j.setArbeitsortText("Poland - Kraków");
        j.setArbeitsortLand(null); // identification of foreign countries is not supported yet
        j.setPensumVon(0);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49832BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49832BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(false);
        // j.setSprache("fr");
        return j;
    }

    private OstePartnerRecord initJob5InEnglish() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("e95a36ff-30a1-3fc0-9f7e-6cc3d45f2255");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("5. Test Job Room Role *English* and German in Hong Kong");
        j.setBeschreibung(
                "Do you know how to make things run smoothly?  Are you a detailed orientated multitasker?  We are looking for someone like that who can provide executive support to the Vice Chairman and their office.You'll:-  manage the Vice Chairman's daily schedule from calendar planning to responding to emails ­- organize meetings, conferences and calls: prepare materials, briefings and meet and greet guests­- attend meetings and produce minutes (you'll need to be fluent in English, Mandarin and German)- schedule and organize international travel arrangements including transport, accommodation and visas ­- manage the personal correspondence of the Vice Chairman from  responding to invitations, congratulations and credit card expenses ­- support the Vice Chairman's management team and deputize for the Business Manager during absences\n" +
                "Sie arbeiten im Team PKI in Emmenbrücke. Wir sind ein motiviertes und dynamisches Team.\n" +
                "Sie verfügen über:– eine kaufmännische Ausbildung, idealerweise mit einer Weiterbildung im Bank- oder Finanzwesen– lokale oder regionale Erfahrung in der Kundenbetreuung im Bankensektor– Kenntnisse in den Bereichen Anlagen, Hypotheken und Altersvorsorge– die Fähigkeit, die lokale Marktentwicklung zu antizipieren sowie Veranstaltungen aktiv zu gestalten (gezielte Kunden-Events sind Ihr Lieblingsrevier)– Leidenschaft für Verkauf und NeukundengewinnungSie:– sind Kundenberater aus Leidenschaft (und Ihr Kunde spürt das sofort)– sind erfahren im Erkennen von Kundenbedürfnissen und im Umsetzen dieser Bedürfnisse in Lösungen– sprechen fliessend Deutsch\n" +
                "Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the support functions. That's what we do. And we do it for private and institutional clients as well as corporations around the world.<br><br>We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
                "Gemeinsam. So packen wir die Dinge an. Wir bieten qualifizierten Menschen weltweit ein unterstützendes, herausforderndes und vielfältiges Arbeitsumfeld. Wir schätzen Leidenschaft und Einsatz. Und belohnen Leistung.<BR><BR>Warum UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
                "UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Hong Kong");
        j.setArbeitsortLand(null); // identification of foreign countries is not supported yet
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49881BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49881BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("en");
        return j;
    }

    private OstePartnerRecord initJob5InGerman() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("a8483c1b-f06c-3d24-b1d8-cb69ead67e3c");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("5. Test Job Room Role English and *German* in Hong Kong");
        j.setBeschreibung(
                "Do you know how to make things run smoothly?  Are you a detailed orientated multitasker?  We are looking for someone like that who can provide executive support to the Vice Chairman and their office.You'll:-  manage the Vice Chairman's daily schedule from calendar planning to responding to emails ­- organize meetings, conferences and calls: prepare materials, briefings and meet and greet guests­- attend meetings and produce minutes (you'll need to be fluent in English, Mandarin and German)- schedule and organize international travel arrangements including transport, accommodation and visas ­- manage the personal correspondence of the Vice Chairman from  responding to invitations, congratulations and credit card expenses ­- support the Vice Chairman's management team and deputize for the Business Manager during absences\n" +
                "Sie arbeiten im Team PKI in Emmenbrücke. Wir sind ein motiviertes und dynamisches Team.\n" +
                "Sie verfügen über:– eine kaufmännische Ausbildung, idealerweise mit einer Weiterbildung im Bank- oder Finanzwesen– lokale oder regionale Erfahrung in der Kundenbetreuung im Bankensektor– Kenntnisse in den Bereichen Anlagen, Hypotheken und Altersvorsorge– die Fähigkeit, die lokale Marktentwicklung zu antizipieren sowie Veranstaltungen aktiv zu gestalten (gezielte Kunden-Events sind Ihr Lieblingsrevier)– Leidenschaft für Verkauf und NeukundengewinnungSie:– sind Kundenberater aus Leidenschaft (und Ihr Kunde spürt das sofort)– sind erfahren im Erkennen von Kundenbedürfnissen und im Umsetzen dieser Bedürfnisse in Lösungen– sprechen fliessend Deutsch\n" +
                "Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the support functions. That's what we do. And we do it for private and institutional clients as well as corporations around the world.<br><br>We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
                "Gemeinsam. So packen wir die Dinge an. Wir bieten qualifizierten Menschen weltweit ein unterstützendes, herausforderndes und vielfältiges Arbeitsumfeld. Wir schätzen Leidenschaft und Einsatz. Und belohnen Leistung.<BR><BR>Warum UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
                "UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Hong Kong");
        j.setArbeitsortLand(null); // identification of foreign countries is not supported yet
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5050&areq=49881BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5050&areq=49881BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-11-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("de");
        return j;
    }

    private OstePartnerRecord initJob6InEnglish() {
        // Reference 49926BR
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("8495ffce-e4d8-3462-a5b5-c2cd14ba8294");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("6. English Job Title");
        j.setBeschreibung(
                "<S1>Your role</S1>Do you know how to make things run smoothly?  Are you a detailed orientated multitasker?  We are looking for someone like that who can provide executive support to the Vice Chairman and their office.You'll:-  manage the Vice Chairman's daily schedule from calendar planning to responding to emails ­- organize meetings, conferences and calls: prepare materials, briefings and meet and greet guests­- attend meetings and produce minutes (you'll need to be fluent in English, Mandarin and German)- schedule and organize international travel arrangements including transport, accommodation and visas ­- manage the personal correspondence of the Vice Chairman from  responding to invitations, congratulations and credit card expenses ­- support the Vice Chairman's management team and deputize for the Business Manager during absences\n" +
                "<S1>Your team</S1>You'll be working in the Vice Chairman's Office as part of a small team of Executive Assistants, senior executives and experts. You will work closely with the Business Manager to support the Vice Chairman and their office in daily work. This is an exciting opportunity to closely work with the Vice Chairman of a globally operating bank. You'll get to collaborate globally across the bank's divisions.\n" +
                "<S1>Your experience and skills</S1>You have:­- Experience working at a top-executive level ­- Great communication skills with the ability to interact with senior stakeholders ­- Outstanding organizational skills (you thrive under pressure and juggling tasks is a breeze)­- Microsoft Office skills (Excel, Word, PowerPoint, Outlook) and a typing speed of 75 WPM­- A good understanding of the Swiss and international financial market environmentYou are:­- Flexible, proactive and a strong team player­- Solution oriented with a calm personality and high stress resilience­- Fluent in English, Mandarin and German\n" +
                "<S1>About us</S1>Expert advice. Wealth management. Investment banking. Asset management. Retail banking in Switzerland. And all the support functions. That's what we do. And we do it for private and institutional clients as well as corporations around the world.<br><br>We are about 60,000 employees in all major financial centers, in almost 900 offices and more than 50 countries. Do you want to be one of us?\n" +
                "<S1>What we offer</S1>Together. That’s how we do things. We offer talented people around the world a supportive, stimulating and diverse working environment. We’ll value your passion and commitment. And reward your performance.<BR><BR>Why UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "<S1>Take the next step</S1>Are you truly collaborative? Succeeding at UBS means respecting, understanding and trusting colleagues and clients. Challenging others and being challenged in return. Being passionate about what you do. Driving yourself forward, always wanting to do things the right way. Does that sound like you? Then you have the right stuff to join us. Apply now.\n" +
                "<S1>Disclaimer / Policy Statements</S1>UBS is an Equal Opportunity Employer. We respect and seek to empower each individual and support the diverse cultures, perspectives, skills and experiences within our workforce."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Bern");
        j.setArbeitsortLand("CH");
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49926BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5012&areq=49926BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-20-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("en");
        return j;
    }

    private OstePartnerRecord initJob6InGerman() {
        // Reference 49926BR
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("3b404649-3dc2-3b4f-aa46-351cec44e7a8");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("6. German Job Title");
        j.setBeschreibung(
                "<S1>Ihre Rolle</S1>Laufen Sie zur Hochform auf, wenn Sie Kunden beraten und langfristige Beziehungen aufbauen können? Ist Ihre Anlageberatung herausragend? Wir suchen eine neue Kollegin oder einen neuen Kollegen, die / der uns bei folgenden Aufgaben unterstützt:– Pflegen langfristiger Kundenbeziehungen sowie Neukundenakquise– Anwenden des umfassenden vierstufigen Beratungsansatzes zur Entwicklung ergebnisorientierter Kundenlösungen– Bereitstellen von Kunden-, Produkt- und Marktdaten für Vorgesetzte– Einhalten der KYC-(Know-Your-Client-) und Compliance-Regeln sowie der vorhandenen internen Richtlinien und rechtlichen Bestimmungen\n" +
                "<S1>Ihr Team</S1>Sie arbeiten im Team PKI in Emmenbrücke. Wir sind ein motiviertes und dynamisches Team.\n" +
                "<S1>Ihre Kenntnisse und Erfahrungen</S1>Sie verfügen über:– eine kaufmännische Ausbildung, idealerweise mit einer Weiterbildung im Bank- oder Finanzwesen– lokale oder regionale Erfahrung in der Kundenbetreuung im Bankensektor– Kenntnisse in den Bereichen Anlagen, Hypotheken und Altersvorsorge– die Fähigkeit, die lokale Marktentwicklung zu antizipieren sowie Veranstaltungen aktiv zu gestalten (gezielte Kunden-Events sind Ihr Lieblingsrevier)– Leidenschaft für Verkauf und NeukundengewinnungSie:– sind Kundenberater aus Leidenschaft (und Ihr Kunde spürt das sofort)– sind erfahren im Erkennen von Kundenbedürfnissen und im Umsetzen dieser Bedürfnisse in Lösungen– sprechen fliessend Deutsch\n" +
                "<S1>Über uns</S1>Kompetente Beratung. Vermögensverwaltung. Investment Banking. Asset Management. Privatkundengeschäft in der Schweiz. Und alles, was man dazu braucht. Das machen wir. Für Privat-, Firmen- und institutionelle Kunden weltweit.<br><br>Wir sind rund 60 000 Mitarbeiter - in allen wichtigen Finanzzentren, an fast 900 Standorten, in mehr als 50 Ländern. Wollen Sie dazugehören?\n" +
                "<S1>Was wir bieten</S1>Gemeinsam. So packen wir die Dinge an. Wir bieten qualifizierten Menschen weltweit ein unterstützendes, herausforderndes und vielfältiges Arbeitsumfeld. Wir schätzen Leidenschaft und Einsatz. Und belohnen Leistung.<BR><BR>Warum UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "<S1>Machen Sie den nächsten Schritt</S1>Sind sie ein echter Teamplayer? Erfolg heisst bei UBS, Kollegen und Kunden zu respektieren, zu verstehen und zu vertrauen. Andere fordern und gefordert werden. Mit Leidenschaft bei der Sache sein. Sich selbst vorantreiben und immer das Richtige wollen. Sind Sie das? Dann sind Sie bei uns richtig. Bewerben Sie sich. Jetzt.\n" +
                "<S1>Rechtshinweis</S1>UBS ist ein Arbeitgeber, der Chancengleichheit fördert. Wir respektieren jeden Mitarbeiter als Individuum sowie unterschiedliche Kulturen, Perspektiven, Fähigkeiten und Erfahrungen unseres Personals."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Bern");
        j.setArbeitsortLand("CH");
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5050&areq=49926BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5050&areq=49926BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-20-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("de");
        return j;
    }

    private OstePartnerRecord initJob6InFrench() {
        // Reference 49926BR
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("e9c61f2b-39e8-31fc-99ca-10fb76168dd9");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("6. French Job Title");
        j.setBeschreibung(
                "<S1>Votre fonction</S1>Vous cherchez un emploi à temps partiel à Renens? Vous aimez le contact clientèle et savez convaincre ? Une écoute active et la reconnaissance des signaux font parties de vos points forts? Votre talent est demandé pour :- contacter téléphoniquement des entreprises de différents secteurs et régions dans le cadre de campagnes bancaires et spécifiques à leurs besoins- identifier les opportunités parmi nos clients et prospects- répondre aux besoins des clients en utilisant des processus de conseil standardisé- atteindre et dépasser les objectifs en matière de ventes\n" +
                "<S1>Votre équipe</S1>Vous travaillerez au sein d’une nouvelle équipe jeune et dynamique.\n" +
                "<S1>Votre expérience et vos compétences</S1>Vous avez :- besoin d’un temps partiel pour concilier vos études, votre famille ou une réinsertion dans la vie professionnelle- une passion pour la vente et la conquête de nouveaux clients- la capacité d’apprendre rapidement et de vous adapter aux procédures internesVous êtes :- un(e) communicant(e) d’exception avec une diction impeccable- multitâche, bien organisé(e) et capable de vous surpasser- de langue maternelle française avec de bonnes connaissances d’anglais, l’allemand étant un grand atout\n" +
                "<S1>A propos de nous</S1>Conseil expert. Gestion de fortune. Investment banking. Gestion d’actifs. Banque de détail en Suisse. Et toutes les fonctions de soutien à ces domaines. C'est ce que nous faisons pour des clients privés, institutionnels, et des entreprises du monde entier.<br><br>Nous sommes quelque 60 000 employés travaillant dans les principaux centres financiers, dans près de 900 bureaux situés dans plus de 50 pays. Voulez-vous faire partie de notre équipe?\n" +
                "<S1>Nous offrons</S1>Ensemble. Voilà comment nous travaillons. Nous offrons aux talents du monde entier un cadre de travail favorable, stimulant et diversifié. Nous apprécierons votre passion et votre engagement et récompenserons votre performance.<BR><BR>Pourquoi UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Vidéo</A>\n" +
                "<S1>Passez à l’étape suivante</S1>La collaboration n’est pas un vain mot pour vous? Réussir chez UBS c'est respecter, comprendre et faire confiance à vos collègues et aux clients. C'est aussi être prêt à défier les autres constructivement et à l'être soi-même en retour. C'est être passionné par ce  que vous faites. Toujours chercher à progresser, tout  en agissant correctement. Vous reconnnaissez-vous dans ce portrait? Alors vous avez ce qu'il faut pour être des nôtres. Postulez maintenant.\n" +
                "<S1>Disclaimer / Policy Statements</S1>UBS offre à tous les mêmes opportunités professionnelles. Nous respectons ainsi la culture de chacun et favorisons le développement, les perspectives, les compétences ainsi que l'expérience de nos collaborateurs.<br><br>"
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Berne");
        j.setArbeitsortLand("CH");
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5049&areq=49926BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5049&areq=49926BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-20-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("fr");
        return j;
    }

    private OstePartnerRecord initJob6InItalian() {
        // Reference 49926BR
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("bce0e8e0-ebbc-30bc-a601-36b3aa11e1ea");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("6. Italian Job Title");
        j.setBeschreibung(
                "<S1>Il vostro ruolo</S1>Sapete ascoltare? Sapete come prendervi cura delle persone? Siamo alla ricerca di una persona dotata di tali capacità che possa fungere da primo punto di contatto per i clienti di piccole e medie imprese (PMI). Voi:– sarete il punto di riferimento della vostra clientela – esplicherete le mansioni amministrative e operative legate al vostro portafolio clienti– svilupperete le relazioni con i clienti nuovi ed esistenti creando opportunità di vendita– promuoverete l’acquisizione di conoscenze specialistiche (come Fast Credit o FX) – vi atterrete alle norme KYC (know your client) e alle regole di compliance pertinenti, evitando qualsiasi tipo di rischio- dimostrerete uno spirito proattivo di collaborazione con i partner interni\n" +
                "<S1>Il vostro team</S1>Il team PMI Sopraceneri ha 2 location: Locarno e Bellinzona. La posizione sarà nel dinamico team di Locarno.\n" +
                "<S1>La vostra esperienza e le vostre competenze</S1>Possedete:– una formazione commerciale, preferibilmente con un perfezionamento nel settore bancario o finanziario– esperienza locale nell’assistenza ai clienti in ambito bancario– esperienza nella consulenza alla clientela, preferibilmente nel segmento dei clienti aziendali– una passione per la vendita e l’acquisizione di clientiSiete:– entusiasti dell’esperienza che offrite al cliente e dotati di spiccate competenze interpersonali– in grado di svolgere diverse attività contemporaneamente, ben organizzati e capaci di fornire ottime prestazioni anche quando non c’è un giorno uguale all’altro– sempre professionali (pur coltivando l’aspetto umano)– fluenti in tedesco e francese\n" +
                "<S1>Chi siamo</S1>Consulenza competente. Gestione patrimoniale. Investment banking. Asset management. Retail banking in Svizzera. E tutti i servizi di supporto connessi. Questo è ciò che facciamo.  E lo facciamo per clienti privati, istituti e società in tutto il mondo.<br><br>Siamo circa 60 000 collaboratori sparsi in tutti i grandi centri finanziari del mondo, in circa 900 sedi e oltre 50 paesi. Volete far parte del nostro team?\n" +
                "<S1>Cosa offriamo</S1>Insieme. Questo è il modo in cui lavoriamo. Offriamo a persone di talento in tutto il mondo un ambiente di lavoro caratterizzato dal sostegno reciproco, stimolante e variegato. Valorizziamo la passione e l'impegno. E ricompensiamo la vostra performance.<BR><BR>Perché UBS? <A tabIndex=4000 href=\"http://link.brightcove.com/services/player/bcpid3906205150001?bckey=AQ~~,AAABFr5dtuk~,GrdDxOUKpFDr99hBT2q0jKbWCdz8Q7uQ&amp;bctid=3781828116001\" target=_blank>Video</A>\n" +
                "<S1>Fate il prossimo passo</S1>Lavorate davvero bene in squadra? Rispettare, comprendere e avere fiducia nei colleghi e clienti sono le chiavi giuste per avere successo in UBS. Mettere alla prova se stessi e gli altri. Lavorare con passione. Cercare di migliorare costantemente e voler sempre fare le cose nel modo giusto. Vi riconoscete in questo profilo? Allora avete tutto ciò che è necessario per far parte di noi. Candidatevi.\n" +
                "<S1>Disclaimer / Policy Statements</S1>UBS sostiene e garantisce le Pari Opportunità. Nutre un profondo rispetto per i propri collaboratori, ha cura di valorizzare ciascuno di loro e di promuovere le diverse culture, prospettive, capacità professionali ed esperienze che coesistono all'interno della propria realtà.<br><br>"
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Berna");
        j.setArbeitsortLand("CH");
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5048&areq=49926BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5048&areq=49926BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-20-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("it");
        return j;
    }

    private OstePartnerRecord initJob7() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setId("e3ccab11-dec1-3c60-96ea-105fa815f3bf");
        j.setQuelle(UBS_CODE);
        j.setBezeichnung("7. Fusion Testing Professional FR/2");
        j.setBeschreibung(
                "<S1>Votre fonction</S1><P>Client Advisor</P>\n" +
                "<S1>Votre équipe</S1>\n" +
                "<S1>Votre expérience et vos compétences</S1>\n" +
                "<S1>A propos de nous</S1>\n" +
                "<S1>Nous offrons</S1>UBS bietet Ihnen ein leistungsorientiertes Umfeld, attraktive Karrierechancen und eine offene Unternehmenskultur, die den Beitrag jedes Einzelnen schätzt und belohnt.\n" +
                "<S1>Passez à l’étape suivante</S1>Vous sentez-vous concerné(e)? N'hésitez pas à nous envoyer votre candidature en ligne\n" +
                "<S1>Disclaimer / Policy Statements</S1>UBS ist ein Arbeitgeber, der Chancengleichheit fördert. Wir respektieren jeden Mitarbeiter als Individuum sowie unterschiedliche Kulturen, Perspektiven, Fähigkeiten und Erfahrungen unseres Personals."
        );
        j.setBerufsgruppe(2);
        j.setUntName("UBS");
        j.setArbeitsortText("Central Plateau - Herzogenbuchsee");
        j.setArbeitsortLand("CH");
        j.setPensumVon(100);
        j.setPensumBis(100);
        // j.setUrlDetail("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5049&areq=49391BR&Codes=Ijob-room");
        j.setUrlBewerbung("https://test.host.com/webapp/cim_jobdetail.asp?partnerid=25008&siteid=5049&areq=49391BR&Codes=Ijob-room");
        j.setAnmeldeDatum("2016-08-29-00.00.00.000000");
        j.setUnbefristetB(true);
        // j.setSprache("fr");
        return j;
    }
}
