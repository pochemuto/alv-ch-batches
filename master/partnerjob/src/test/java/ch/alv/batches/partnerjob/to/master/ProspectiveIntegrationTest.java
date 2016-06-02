package ch.alv.batches.partnerjob.to.master;

import ch.alv.batches.commons.sql.SqlDataTypesHelper;
import ch.alv.batches.commons.test.SimpleTestApplication;
import ch.alv.batches.commons.test.springbatch.SpringBatchTestHelper;
import ch.alv.batches.partnerjob.to.master.jooq.tables.records.OstePartnerRecord;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.joda.time.LocalDate;
import org.jooq.DSLContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.alv.batches.partnerjob.to.master.batch.ProspectiveJobToPartnerJobConverter.DESC_MAX_LENGTH;
import static ch.alv.batches.partnerjob.to.master.batch.ProspectiveJobToPartnerJobConverter.DESC_TRUNCATE_SUFFIX;
import static ch.alv.batches.partnerjob.to.master.config.PartnerJobToMasterConfiguration.BATCH_JOB_PARAMETER_PARTNER_CODE;
import static ch.alv.batches.partnerjob.to.master.jooq.Tables.OSTE_PARTNER;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SimpleTestApplication.class)
@IntegrationTest
public class ProspectiveIntegrationTest {

    @Resource
    private Job importPartnerjobsJob;

    @Resource
    private SpringBatchTestHelper springBatchHelper;

    @Resource
    private DSLContext jooq;

    private static final String PARTNER_CODE_1 = "partner1";
    private static final String PARTNER_CODE_2 = "partner2";

    private static Server server;

    private List<OstePartnerRecord> checkPartner1Jobs;
    private List<OstePartnerRecord> checkPartner2Jobs;


    @BeforeClass
    public static void initStaticObjects() throws Exception {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8180);
        server.addConnector(connector);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("src/test/resources");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
        server.start();
    }

    @AfterClass
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Before
    public void initTestObjects() {
        checkPartner1Jobs = new ArrayList<>();
        checkPartner1Jobs.add(initJob1());
        checkPartner1Jobs.add(initJob2());
        checkPartner1Jobs.add(initJob3());
        checkPartner1Jobs.add(initJob4());
        checkPartner1Jobs.add(initJob5());

        checkPartner2Jobs = new ArrayList<>();
        checkPartner2Jobs.add(initJob6());
    }

    @Test
    public void testThatAPartnerImportIsComplete() throws
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            IOException,
            SQLException {
        
        assertNotNull(importPartnerjobsJob);

        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(BATCH_JOB_PARAMETER_PARTNER_CODE, new JobParameter(PARTNER_CODE_1));

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importPartnerjobsJob, parameters));

        List<OstePartnerRecord> fetchedJobs = jooq.selectFrom(OSTE_PARTNER)
                .where(OSTE_PARTNER.QUELLE.equal(PARTNER_CODE_1))
                .fetch()
                .sortAsc(OSTE_PARTNER.URL_DETAIL);

        assertEquals(5, fetchedJobs.size());

        ArrayList<String> uuidList = new ArrayList<>();
        
        while (!fetchedJobs.isEmpty() && !checkPartner1Jobs.isEmpty()) {
            OstePartnerRecord result = fetchedJobs.remove(0);
            OstePartnerRecord check = checkPartner1Jobs.remove(0);

            // the random UUID is missing in the validation object
            assertEquals(-1, result.compareTo(check));

            String id = result.getId();
            assertEquals(36, id.length());
            assertFalse(uuidList.contains(id));
            uuidList.add(id);
        }
    }

    @Test
    public void testThatImportsIsolated() throws
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            IOException,
            SQLException {
        Integer partner1CountBeforeImport = jooq.fetchCount(
                jooq.selectFrom(OSTE_PARTNER).where(OSTE_PARTNER.QUELLE.equal(PARTNER_CODE_1))
        );

        Map<String, JobParameter> validParameters = new HashMap<>();
        validParameters.put(BATCH_JOB_PARAMETER_PARTNER_CODE, new JobParameter(PARTNER_CODE_2));

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importPartnerjobsJob, validParameters));

        Map<String, JobParameter> wrongParameters = new HashMap<>();
        wrongParameters.put(BATCH_JOB_PARAMETER_PARTNER_CODE, new JobParameter("unknown_code"));
        assertEquals(ExitStatus.FAILED, springBatchHelper.runJob(importPartnerjobsJob, wrongParameters));

        Integer partner1CountAfterImport = jooq.fetchCount(
                jooq.selectFrom(OSTE_PARTNER).where(OSTE_PARTNER.QUELLE.equal(PARTNER_CODE_1))
        );

        assertEquals(partner1CountBeforeImport, partner1CountAfterImport);

        assertEquals(1, jooq.fetchCount(jooq.selectFrom(OSTE_PARTNER).
                where(OSTE_PARTNER.QUELLE.equal(PARTNER_CODE_2)))
        );
    }

    @Test
    public void testJobDescriptionTruncating() throws
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException,
            IOException,
            SQLException {
        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(BATCH_JOB_PARAMETER_PARTNER_CODE, new JobParameter(PARTNER_CODE_2));

        assertEquals(ExitStatus.COMPLETED, springBatchHelper.runJob(importPartnerjobsJob, parameters));

        OstePartnerRecord fetchedJob = jooq.selectFrom(OSTE_PARTNER)
                .where(OSTE_PARTNER.QUELLE.equal(PARTNER_CODE_2))
                .and(OSTE_PARTNER.BEZEICHNUNG.equal("Teststelle Partner2"))
                .fetchAny();

        assertEquals(DESC_MAX_LENGTH, fetchedJob.getBeschreibung().length());
        assertEquals(DESC_TRUNCATE_SUFFIX, fetchedJob.getBeschreibung().substring(DESC_MAX_LENGTH - DESC_TRUNCATE_SUFFIX.length()));
    }

    private OstePartnerRecord initJob1() {
        OstePartnerRecord job1 = new OstePartnerRecord();
        job1.setQuelle(PARTNER_CODE_1);
        job1.setBezeichnung("Teststelle Partner1");
        job1.setBeschreibung("Eidgenössisches Departement für<br/>auswärtige Angelegenheiten EDA<br/>\n" +
                "<b>Direktion für europäische Angelegenheiten DEA</b> Ihre Bewerbung senden Sie bitte an<br/>folgende Adresse:<br/>Direktion für europäische Angelegenheiten DEA, Freiburgstrasse 130, 3003 Bern<br/>\n" +
                "<br/>Ergänzende Auskünfte erteilt Ihnen gerne (individuelle Eingabe)");
        job1.setBerufsgruppe(10);
        job1.setUntName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        job1.setArbeitsortPlz("3002");
        job1.setPensumVon(80);
        job1.setPensumBis(100);
        job1.setUrlDetail("http://oh.merkur.prospective.ch/?view=E2589F54-0FEB-BEF7-6B440689168A5A71");
        job1.setAnmeldeDatum(SqlDataTypesHelper.fromJodaLocalDate(LocalDate.parse("2015-06-11")));
        job1.setSprache("de");
        return job1;
    }

    private OstePartnerRecord initJob2() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setQuelle(PARTNER_CODE_1);
        j.setBezeichnung("Grenzwächter/Grenzwächterin mit eidgenössischem Fachausweis");
        j.setBeschreibung("Wollen Sie einen Beitrag für die Wirtschaft, die Sicherheit und Gesundheit der Schweizer Bevölkerung leisten?<br/>\n" +
                "<br/>Dann bewerben Sie sich als Wir bieten Ihnen eine umfassende und abwechslungsreiche Ausbildung. Eine Arbeit, die echte Herausforderungen und interessante Entwicklungsmöglichkeiten bietet. Fortschrittliche Arbeitsbedingungen und gute Sozialleistungen. Die Ausbildungen beginnen jeweils im Januar und im Juli.<br/>\n" +
                "<br/>\n" +
                "<b>Anforderungen:</b>&shy;<ul>\n" +
                "<li>Schweizer Bürger/in oder Doppelbürger/in&shy;</li>\n" +
                "<li>Abgeschlossene dreijährige Berufslehre oder gleichwertige Ausbildung mit eidge-nössischem Fähigkeitszeugnis oder gleichwertigem Ausweis&shy;</li>\n" +
                "<li>Alter zwischen 20 und 35 Jahren&shy;</li>\n" +
                "<li>Mindestgrösse von 168 cm für Bewerber bzw.160 cm für Bewerberinnen&shy;</li>\n" +
                "<li>Führerausweis der Kategorie B.</li>\n" +
                "</ul>\n" +
                "<br/>Verfügen Sie ausserdem über persönliche Eigenschaften wie Teamfähigkeit, Kontaktfreudigkeit, rasche Auffassungsgabe, Selbstständigkeit, Zuverlässigkeit und Durchsetzungsvermögen?<br/>\n" +
                "<br/>\n" +
                "<b>Informationsveranstaltungen zum Grenzwachtberuf im Ausbildungszentrum des Schweizer Zolls in Liestal. Infos unter: www.gwk.ch (Ausbildung).</b>\n" +
                "<br/>\n" +
                "<br/>Dann freuen wir uns auf die Zustellung Ihrer Bewerbungsunterlagen per E-Mail an: hr-center-basel@ezv.admin.ch Eidgenössisches Finanzdepartement EFD<br/>\n" +
                "<b>Eidgenössische Zollverwaltung EZV</b> Fachliche Auskünfte erteilt Ihnen gerne:<br/>\n" +
                "<br/>Eidgenössische Zollverwaltung<br/>HR-Center Basel<br/>Elisabethenstrasse 31<br/>4010 Basel<br/>Telefon: +41 58 469 11 30<br/>www.gwk.ch");
        j.setBerufsgruppe(11);
        j.setUntName("Eidgenössische Zollverwaltung EZV");
        j.setArbeitsortPlz("8038");
        j.setPensumVon(80);
        j.setPensumBis(80);
        j.setUrlDetail("http://oh.merkur.prospective.ch/?view=E259D738-0559-E806-E9C3077F15A49B16");
        j.setAnmeldeDatum(SqlDataTypesHelper.fromJodaLocalDate(LocalDate.parse("2015-06-11")));
        j.setSprache("de");
        return j;
    }

    private OstePartnerRecord initJob3() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setQuelle(PARTNER_CODE_1);
        j.setBezeichnung("Collaboratore/trice scientifico/a, Sezione Affari economici e finanziari, DAE");
        j.setBeschreibung("Il Dipartimento federale degli affari esteri DFAE tutela gli interessi di politica estera della Svizzera.<br/>\n" +
                "<br/>La Direzione degli affari europei (DAE) è responsabile dell'organizzazione e del coordinamento delle relazioni della Svizzera con l'Unione europea (UE).<br/>\n" +
                "<br/>In seno alla DAE, la Sezione Affari economici e finanziari si occupa di tutti gli aspetti di carattere economico, commerciale, finanziario e fiscale. <b>Compiti</b>\n" +
                "<br/>Nel dinamico contesto della politica europea, il/la titolare del posto sarà l'esperto/a della DAE per tutte le questioni fiscali legate all'Unione europea, in stretta collaborazione con la Segreteria di Stato per le questioni finanziarie internazionali (SFI). In seno alla DAE, sarà inoltre responsabile di tutte le questioni generali legate alla politica della concorrenza e agli aiuti di Stato, dell'accordo bilaterale sulla lotta contro la frode, nonché dei dossier di politica regionale e della cooperazione transfrontaliera.<br/>\n" +
                "<br/>Monitorerà autonomamente gli sviluppi registrati nell'UE nel settore di sua competenza: in particolare esaminerà la compatibilità delle bozze di atti giuridici e degli affari del Consiglio federale con gli impegni della Svizzera nell'ambito della politica europea, delineerà opzioni operative ed elaborerà la posizione della DAE. Il/la titolare del posto rappresenterà la DAE in differenti comitati e gruppi di lavoro dell'Amministrazione, nonché nel quadro dei negoziati con l'UE. All'occorrenza effettuerà viaggi a Bruxelles e opererà a stretto contatto con la Missione della Svizzera presso l'Unione europea. <b>Requisiti</b>\n" +
                "<br/>Comprovato interesse per la politica europea della Svizzera e, nel caso ideale, prime esperienze professionali e conoscenze nel campo d'attività previsto dal posto. Diploma universitario (idealmente diritto, scienze politiche o ambiti affini) e capacità di ragionare in modo interdisciplinare, tenendo conto di considerazioni giuridiche, economiche, nonché di politica este...");
        j.setBerufsgruppe(10);
        j.setUntName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        j.setArbeitsortPlz("8032");
        j.setPensumVon(40);
        j.setPensumBis(50);
        j.setUrlDetail("http://oh.merkur.prospective.ch/?view=E69C6818-B845-086C-C7C94F50F21A3A7E");
        j.setAnmeldeDatum(SqlDataTypesHelper.fromJodaLocalDate(LocalDate.parse("2015-06-12")));
        j.setSprache("it");
        return j;
    }

    private OstePartnerRecord initJob4() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setQuelle(PARTNER_CODE_1);
        j.setBezeichnung("Collaborateur/trice scientifique au sein de la Section Affaires économiques et financières, DAE");
        j.setBeschreibung("Le Département fédéral des affaires étrangères DFAE sauvegarde les intérêts de la Suisse en matière de politique extérieure.<br/>\n" +
                "<br/>La Direction des affaires européennes (DAE) est responsable de l'orientation et de la coordination des relations de la Suisse avec l'Union européenne (UE).<br/>\n" +
                "<br/>Au sein de la DAE, la Section Affaires économiques et financières est en charge de toutes les questions politiques de nature économique, commerciale, financière et fiscale. <b>Tâches</b>\n" +
                "<br/>Dans le contexte dynamique de la politique européenne, vous serez l'expert/e de la DAE pour toutes les questions fiscales en lien avec l'Union européenne, en étroite collaboration avec le Secrétariat d'Etat aux questions financières internationales (SIF). Au sein de la DAE, vous serez également responsable de toutes les questions générales liées à la politique de la concurrence et aux aides d'Etat, de l'accord bilatéral sur la lutte contre la fraude, ainsi que des dossiers de la politique régionale et de la coopération transfrontalière.<br/>\n" +
                "<br/>Vous suivrez de manière autonome les développements au sein de l'UE dans votre domaine de compétence, contrôlerez notamment des projets d'actes juridiques et d'affaires du Conseil fédéral sous l'angle de leur compatibilité avec les engagements pris par la Suisse en matière de politique européenne, esquisserez les actions envisageables et élaborerez les prises de position de la DAE. Vous représenterez cette dernière dans différents comités et groupes de travail de l'administration ainsi que dans les négociations avec l'UE. Le cas échéant, vous serez amené/e à voyager à Bruxelles et serez en contact régulier avec la Mission de la Suisse auprès de l'UE. <b>Profil demandé</b>\n" +
                "<br/>Vous vous intéressez vivement à la politique européenne de la Suisse et justifiez, idéalement, de premières expériences professionnelles et de connaissances dans le domaine d'activité qui vous sera confié. Titulaire d'un diplôme universitaire (idéalement en droit,...");
        j.setBerufsgruppe(10);
        j.setUntName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        j.setArbeitsortPlz("8032");
        j.setPensumVon(80);
        j.setPensumBis(100);
        j.setUrlDetail("http://oh.merkur.prospective.ch/?view=E69C6A4A-AA9A-1576-51595322160A0B4F");
        j.setAnmeldeDatum(SqlDataTypesHelper.fromJodaLocalDate(LocalDate.parse("2015-06-12")));
        j.setSprache("fr");
        return j;
    }

    private OstePartnerRecord initJob5() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setQuelle(PARTNER_CODE_1);
        j.setBezeichnung("Wissenschaftliche/r Mitarbeiter/in, Sektion Wirtschafts-und Finanzfragen, DEA");
        j.setBeschreibung("Das Eidgenössische Departement für auswärtige Angelegenheiten EDA wahrt die aussenpolitischen Interessen der Schweiz.<br/>\n" +
                "<br/>Die Direktion für europäische Angelegenheiten (DEA) ist für die Gestaltung und Koordination der Beziehungen der Schweiz zur Europäischen Union (EU) verantwortlich.<br/>\n" +
                "<br/>Innerhalb der DEA betreut die Sektion Wirtschafts- und Finanzfragen alle wirtschafts-, handels-, finanz- und steuerpolitischen Fragen. <b>Aufgaben</b>\n" +
                "<br/>Im dynamischen Umfeld der Europapolitik sind Sie in der DEA die zentrale Ansprechperson für alle Steuerfragen im Zusammenhang mit der EU. Dabei arbeiten Sie eng mit dem Staatssekretariat für internationale Finanzfragen (SIF) zusammen. Sie sind ausserdem zuständig für allgemeine Wettbewerbs- und Beihilfenfragen und betreuen das bilaterale Betrugsbekämpfungsabkommen sowie die Dossiers Regionalpolitik und grenzüberschreitende Zusammenarbeit seitens der DEA.<br/>\n" +
                "<br/>Sie verfolgen selbständig die Entwicklungen in der EU im Zuständigkeitsbereich ihrer Dossiers, prüfen u.a. Entwürfe für Rechtserlasse und Bundesratsgeschäfte hinsichtlich ihrer Kompatibilität mit den europapolitischen Verpflichtungen der Schweiz, skizzieren Handlungsoptionen und erarbeiten die DEA-Position. Sie vertreten die DEA in Ausschüssen und Arbeitsgruppen innerhalb der Verwaltung und bei Verhandlungen mit der EU. Hierfür reisen Sie gegebenenfalls nach Brüssel und stehen in engem Kontakt mit der dortigen Schweizer Mission. <b>Anforderungen</b>\n" +
                "<br/>Sie haben ein ausgewiesenes Interesse an der schweizerischen Europapolitik und bringen idealerweise erste Arbeitserfahrungen und Kenntnisse im Aufgabenbereich der Stelle mit. Sie verfügen über einen Hochschulabschluss (idealerweise Recht, Politikwissenschaft oder ähnlich) und zeichnen sich durch die Fähigkeit zu interdisziplinärem Denken im Spannungsfeld zwischen (innen- und aussen-)politischen, juristischen und wirtschaftlichen Gesichtspunkten aus.<br/>\n" +
                "<br/>Wir richten uns an eine belastbare, flexible...");
        j.setBerufsgruppe(10);
        j.setUntName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        j.setArbeitsortPlz("8032");
        j.setPensumVon(80);
        j.setPensumBis(80);
        j.setUrlDetail("http://oh.merkur.prospective.ch/?view=E69C6B83-9607-C2FE-2BA8743DC442BC94");
        j.setAnmeldeDatum(SqlDataTypesHelper.fromJodaLocalDate(LocalDate.parse("2015-06-12")));
        j.setSprache("de");
        return j;
    }

    private OstePartnerRecord initJob6() {
        OstePartnerRecord j = new OstePartnerRecord();
        j.setQuelle(PARTNER_CODE_2);
        j.setBezeichnung("Teststelle Partner2");
        j.setBeschreibung("Eidgenössisches Departement für<br/>auswärtige Angelegenheiten EDA<br/>\n" +
                "<b>Direktion für europäische Angelegenheiten DEA</b> Ihre Bewerbung senden Sie bitte an<br/>folgende Adresse:<br/>Direktion für europäische Angelegenheiten DEA, Freiburgstrasse 130, 3003 Bern<br/>\n" +
                "<br/>Ergänzende Auskünfte erteilt Ihnen gerne (individuelle Eingabe)");
        j.setBerufsgruppe(10);
        j.setUntName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        j.setArbeitsortPlz("3002");
        j.setPensumVon(80);
        j.setPensumBis(100);
        j.setUrlDetail("http://oh.merkur.prospective.ch/?view=E2589F54-0FEB-BEF7-6B440689168A5A71");
        j.setAnmeldeDatum(SqlDataTypesHelper.fromJodaLocalDate(LocalDate.parse("2015-06-11")));
        j.setSprache("de");
        return j;
    }

}
