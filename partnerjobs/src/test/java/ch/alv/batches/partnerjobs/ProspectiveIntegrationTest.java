package ch.alv.batches.partnerjobs;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: put some useful comment.
 *
 * @since: 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class ProspectiveIntegrationTest {

    @Resource
    private Job importAdminJobsJob;

    @Resource
    private JobLauncher jobLauncher;

    @Resource
    private DataSource dataSource;

    private Server server;

    private List<PartnerJob> jobs;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void initTableAndJobObjects() throws ParseException, SQLException {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8180);
        server.addConnector(connector);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("src/test/resources");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Connection con = null;
        PreparedStatement pstmt;
        con = dataSource.getConnection();
        con.setAutoCommit(true);

        try {
            pstmt = con.prepareStatement("SELECT 1 FROM OSTE_ADMIN LIMIT 1");
            pstmt.execute();
        } catch (Exception e){
            pstmt = con.prepareStatement("CREATE TABLE OSTE_ADMIN (" +
                    "ID VARCHAR(255), " +
                    "BEZEICHNUNG VARCHAR(2000), " +
                    "BESCHREIBUNG VARCHAR(2000), " +
                    "BERUFSGRUPPE INTEGER, " +
                    "UNT_NAME VARCHAR(255), " +
                    "ARBEITSORT_PLZ VARCHAR(10), " +
                    "PENSUM_VON INTEGER, " +
                    "PENSUM_BIS INTEGER, " +
                    "URL_DETAIL VARCHAR(2000), " +
                    "ANMELDE_DATUM DATE, " +
                    "SPRACHE VARCHAR(2))");
            pstmt.execute();
        } finally {
            con.close();
        }


        jobs = new ArrayList<>();
        jobs.add(initJob1());
        jobs.add(initJob2());
        jobs.add(initJob3());
        jobs.add(initJob4());
        jobs.add(initJob5());
    }

    @Test
    public void runTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException, SQLException {
        Assert.assertNotNull(importAdminJobsJob);
        JobExecution exec = jobLauncher.run(importAdminJobsJob, new JobParameters());
        Assert.assertEquals("COMPLETED", exec.getExitStatus().getExitCode());

        Connection con;
        PreparedStatement pstmt;
        try {
            con = dataSource.getConnection();
            con.setAutoCommit(true);
            pstmt = con.prepareStatement("SELECT * FROM OSTE_ADMIN ORDER BY URL_DETAIL");
            ResultSet result = pstmt.executeQuery();
            int counter = 0;
            while(result.next()) {
                PartnerJob currentJob = jobs.get(counter);
                Assert.assertTrue(StringUtils.isNotEmpty(result.getString("ID")));
                Assert.assertEquals(currentJob.getUrlDetail(), result.getString("URL_DETAIL"));
                Assert.assertEquals(currentJob.getTitle(), result.getString("BEZEICHNUNG"));
                Assert.assertEquals(currentJob.getDescription(), result.getString("BESCHREIBUNG"));
                Assert.assertEquals(currentJob.getJobGroup(), result.getInt("BERUFSGRUPPE"));
                Assert.assertEquals(currentJob.getCompanyName(), result.getString("UNT_NAME"));
                Assert.assertEquals(currentJob.getJobLocation(), result.getString("ARBEITSORT_PLZ"));
                Assert.assertEquals(currentJob.getQuotaFrom(), result.getInt("PENSUM_VON"));
                Assert.assertEquals(currentJob.getQuotaTo(), result.getInt("PENSUM_BIS"));
                Assert.assertEquals(currentJob.getOnlineSince(), result.getDate("ANMELDE_DATUM"));
                Assert.assertEquals(currentJob.getLanguage(), result.getString("SPRACHE"));
                counter++;
            }
            Assert.assertEquals(5, counter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private PartnerJob initJob1() throws ParseException {
        PartnerJob job1 = new PartnerJob();
        job1.setTitle("Teststelle");
        job1.setDescription("Eidgenössisches Departement für<br/>auswärtige Angelegenheiten EDA<br/>\n" +
                "<b>Direktion für europäische Angelegenheiten DEA</b> Ihre Bewerbung senden Sie bitte an<br/>folgende Adresse:<br/>Direktion für europäische Angelegenheiten DEA, Freiburgstrasse 130, 3003 Bern<br/>\n" +
                "<br/>Ergänzende Auskünfte erteilt Ihnen gerne (individuelle Eingabe)");
        job1.setJobGroup(10);
        job1.setCompanyName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        job1.setJobLocation("3002");
        job1.setQuotaFrom(80);
        job1.setQuotaTo(100);
        job1.setUrlDetail("http://oh.merkur.prospective.ch/?view=E2589F54-0FEB-BEF7-6B440689168A5A71");
        job1.setOnlineSince(sdf.parse("2015-06-11"));
        job1.setLanguage("de");
        return job1;
    }

    private PartnerJob initJob2() throws ParseException {
        PartnerJob job2 = new PartnerJob();
        job2.setTitle("Grenzwächter/Grenzwächterin mit eidgenössischem Fachausweis");
        job2.setDescription("Wollen Sie einen Beitrag für die Wirtschaft, die Sicherheit und Gesundheit der Schweizer Bevölkerung leisten?<br/>\n" +
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
        job2.setJobGroup(11);
        job2.setCompanyName("Eidgenössische Zollverwaltung EZV");
        job2.setJobLocation("8038");
        job2.setQuotaFrom(80);
        job2.setQuotaTo(80);
        job2.setUrlDetail("http://oh.merkur.prospective.ch/?view=E259D738-0559-E806-E9C3077F15A49B16");
        job2.setOnlineSince(sdf.parse("2015-06-11"));
        job2.setLanguage("de");
        return job2;
    }

    private PartnerJob initJob3() throws ParseException {
        PartnerJob job3 = new PartnerJob();
        job3.setTitle("Collaboratore/trice scientifico/a, Sezione Affari economici e finanziari, DAE");
        job3.setDescription("Il Dipartimento federale degli affari esteri DFAE tutela gli interessi di politica estera della Svizzera.<br/>\n" +
                "<br/>La Direzione degli affari europei (DAE) è responsabile dell'organizzazione e del coordinamento delle relazioni della Svizzera con l'Unione europea (UE).<br/>\n" +
                "<br/>In seno alla DAE, la Sezione Affari economici e finanziari si occupa di tutti gli aspetti di carattere economico, commerciale, finanziario e fiscale. <b>Compiti</b>\n" +
                "<br/>Nel dinamico contesto della politica europea, il/la titolare del posto sarà l'esperto/a della DAE per tutte le questioni fiscali legate all'Unione europea, in stretta collaborazione con la Segreteria di Stato per le questioni finanziarie internazionali (SFI). In seno alla DAE, sarà inoltre responsabile di tutte le questioni generali legate alla politica della concorrenza e agli aiuti di Stato, dell'accordo bilaterale sulla lotta contro la frode, nonché dei dossier di politica regionale e della cooperazione transfrontaliera.<br/>\n" +
                "<br/>Monitorerà autonomamente gli sviluppi registrati nell'UE nel settore di sua competenza: in particolare esaminerà la compatibilità delle bozze di atti giuridici e degli affari del Consiglio federale con gli impegni della Svizzera nell'ambito della politica europea, delineerà opzioni operative ed elaborerà la posizione della DAE. Il/la titolare del posto rappresenterà la DAE in differenti comitati e gruppi di lavoro dell'Amministrazione, nonché nel quadro dei negoziati con l'UE. All'occorrenza effettuerà viaggi a Bruxelles e opererà a stretto contatto con la Missione della Svizzera presso l'Unione europea. <b>Requisiti</b>\n" +
                "<br/>Comprovato interesse per la politica europea della Svizzera e, nel caso ideale, prime esperienze professionali e conoscenze nel campo d'attività previsto dal posto. Diploma universitario (idealmente diritto, scienze politiche o ambiti affini) e capacità di ragionare in modo interdisciplinare, tenendo conto di considerazioni giuridiche, economiche, nonché di politica este...");
        job3.setJobGroup(10);
        job3.setCompanyName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        job3.setJobLocation("8032");
        job3.setQuotaFrom(40);
        job3.setQuotaTo(50);
        job3.setUrlDetail("http://oh.merkur.prospective.ch/?view=E69C6818-B845-086C-C7C94F50F21A3A7E");
        job3.setOnlineSince(sdf.parse("2015-06-12"));
        job3.setLanguage("it");
        return job3;
    }

    private PartnerJob initJob4() throws ParseException {
        PartnerJob job4 = new PartnerJob();
        job4.setTitle("Collaborateur/trice scientifique au sein de la Section Affaires économiques et financières, DAE");
        job4.setDescription("Le Département fédéral des affaires étrangères DFAE sauvegarde les intérêts de la Suisse en matière de politique extérieure.<br/>\n" +
                "<br/>La Direction des affaires européennes (DAE) est responsable de l'orientation et de la coordination des relations de la Suisse avec l'Union européenne (UE).<br/>\n" +
                "<br/>Au sein de la DAE, la Section Affaires économiques et financières est en charge de toutes les questions politiques de nature économique, commerciale, financière et fiscale. <b>Tâches</b>\n" +
                "<br/>Dans le contexte dynamique de la politique européenne, vous serez l'expert/e de la DAE pour toutes les questions fiscales en lien avec l'Union européenne, en étroite collaboration avec le Secrétariat d'Etat aux questions financières internationales (SIF). Au sein de la DAE, vous serez également responsable de toutes les questions générales liées à la politique de la concurrence et aux aides d'Etat, de l'accord bilatéral sur la lutte contre la fraude, ainsi que des dossiers de la politique régionale et de la coopération transfrontalière.<br/>\n" +
                "<br/>Vous suivrez de manière autonome les développements au sein de l'UE dans votre domaine de compétence, contrôlerez notamment des projets d'actes juridiques et d'affaires du Conseil fédéral sous l'angle de leur compatibilité avec les engagements pris par la Suisse en matière de politique européenne, esquisserez les actions envisageables et élaborerez les prises de position de la DAE. Vous représenterez cette dernière dans différents comités et groupes de travail de l'administration ainsi que dans les négociations avec l'UE. Le cas échéant, vous serez amené/e à voyager à Bruxelles et serez en contact régulier avec la Mission de la Suisse auprès de l'UE. <b>Profil demandé</b>\n" +
                "<br/>Vous vous intéressez vivement à la politique européenne de la Suisse et justifiez, idéalement, de premières expériences professionnelles et de connaissances dans le domaine d'activité qui vous sera confié. Titulaire d'un diplôme universitaire (idéalement en droit,...");
        job4.setJobGroup(10);
        job4.setCompanyName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        job4.setJobLocation("8032");
        job4.setQuotaFrom(80);
        job4.setQuotaTo(100);
        job4.setUrlDetail("http://oh.merkur.prospective.ch/?view=E69C6A4A-AA9A-1576-51595322160A0B4F");
        job4.setOnlineSince(sdf.parse("2015-06-12"));
        job4.setLanguage("fr");
        return job4;
    }

    private PartnerJob initJob5() throws ParseException {
        PartnerJob job5 = new PartnerJob();
        job5.setTitle("Wissenschaftliche/r Mitarbeiter/in, Sektion Wirtschafts-und Finanzfragen, DEA");
        job5.setDescription("Das Eidgenössische Departement für auswärtige Angelegenheiten EDA wahrt die aussenpolitischen Interessen der Schweiz.<br/>\n" +
                "<br/>Die Direktion für europäische Angelegenheiten (DEA) ist für die Gestaltung und Koordination der Beziehungen der Schweiz zur Europäischen Union (EU) verantwortlich.<br/>\n" +
                "<br/>Innerhalb der DEA betreut die Sektion Wirtschafts- und Finanzfragen alle wirtschafts-, handels-, finanz- und steuerpolitischen Fragen. <b>Aufgaben</b>\n" +
                "<br/>Im dynamischen Umfeld der Europapolitik sind Sie in der DEA die zentrale Ansprechperson für alle Steuerfragen im Zusammenhang mit der EU. Dabei arbeiten Sie eng mit dem Staatssekretariat für internationale Finanzfragen (SIF) zusammen. Sie sind ausserdem zuständig für allgemeine Wettbewerbs- und Beihilfenfragen und betreuen das bilaterale Betrugsbekämpfungsabkommen sowie die Dossiers Regionalpolitik und grenzüberschreitende Zusammenarbeit seitens der DEA.<br/>\n" +
                "<br/>Sie verfolgen selbständig die Entwicklungen in der EU im Zuständigkeitsbereich ihrer Dossiers, prüfen u.a. Entwürfe für Rechtserlasse und Bundesratsgeschäfte hinsichtlich ihrer Kompatibilität mit den europapolitischen Verpflichtungen der Schweiz, skizzieren Handlungsoptionen und erarbeiten die DEA-Position. Sie vertreten die DEA in Ausschüssen und Arbeitsgruppen innerhalb der Verwaltung und bei Verhandlungen mit der EU. Hierfür reisen Sie gegebenenfalls nach Brüssel und stehen in engem Kontakt mit der dortigen Schweizer Mission. <b>Anforderungen</b>\n" +
                "<br/>Sie haben ein ausgewiesenes Interesse an der schweizerischen Europapolitik und bringen idealerweise erste Arbeitserfahrungen und Kenntnisse im Aufgabenbereich der Stelle mit. Sie verfügen über einen Hochschulabschluss (idealerweise Recht, Politikwissenschaft oder ähnlich) und zeichnen sich durch die Fähigkeit zu interdisziplinärem Denken im Spannungsfeld zwischen (innen- und aussen-)politischen, juristischen und wirtschaftlichen Gesichtspunkten aus.<br/>\n" +
                "<br/>Wir richten uns an eine belastbare, flexible...");
        job5.setJobGroup(10);
        job5.setCompanyName("Eidgenössisches Departement für auswärtige Angelegenheiten");
        job5.setJobLocation("8032");
        job5.setQuotaFrom(80);
        job5.setQuotaTo(80);
        job5.setUrlDetail("http://oh.merkur.prospective.ch/?view=E69C6B83-9607-C2FE-2BA8743DC442BC94");
        job5.setOnlineSince(sdf.parse("2015-06-12"));
        job5.setLanguage("de");
        return job5;
    }

    @After
    public void stopServer() throws Exception {
        server.stop();
    }

}
