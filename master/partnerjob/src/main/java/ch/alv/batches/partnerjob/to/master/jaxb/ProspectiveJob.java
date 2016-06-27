//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.11 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2016.06.26 à 09:20:28 AM CEST 
//


package ch.alv.batches.partnerjob.to.master.jaxb;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This class is a java representation of prospective's xml used to deliver
 * joboffers of the federal administration. Originally it has been generated with
 * help of the maven-jaxb plugin and then it has adopted to match convenience and
 * spring batch needs.
 *
 * @since: 1.0.0
 */
@XmlRootElement(name = "inserat")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProspectiveJob {

    @XmlElement(name = "poster_url", required = true)
    protected String posterUrl;
    @XmlElement(name = "order_id", required = true)
    protected String orderId;
    @XmlElement(name = "organization_id")
    protected short organizationId;
    @XmlElement(name = "COMPANY_PROFILE_URL", required = true)
    protected String companyprofileurl;
    @XmlElement(required = true)
    protected ProspectiveJob.Hierarchien hierarchien;
    @XmlElement(name = "inserat_id")
    protected int inseratId;
    @XmlElement(required = true)
    protected String medienname;
    @XmlElement(name = "medien_id")
    protected int medienId;
    @XmlElement(name = "konto_id")
    protected int kontoId;
    @XmlElement(required = true)
    protected String kundenname;
    protected int layout;
    protected int logo;
    @XmlElement(required = true)
    protected String sprache;
    @XmlElement(name = "datum_start", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumStart;
    @XmlElement(name = "datum_ende", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumEnde;
    @XmlElement(name = "datum_mutation", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumMutation;
    @XmlElement(required = true)
    protected ProspectiveJob.Kontakt kontakt;
    @XmlElement(required = true)
    protected String stellentitel;
    @XmlElement(required = true)
    protected String vorspann;
    @XmlElement(required = true)
    protected ProspectiveJob.Texte texte;
    @XmlElement(required = true)
    protected ProspectiveJob.Metadaten metadaten;
    @XmlElement(name = "url_bewerber", required = true)
    protected String urlBewerber;
    @XmlElement(name = "url_direktlink", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String urlDirektlink;

    /**
     * Obtient la valeur de la propriété posterUrl.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Définit la valeur de la propriété posterUrl.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPosterUrl(String value) {
        this.posterUrl = value;
    }

    /**
     * Obtient la valeur de la propriété orderId.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Définit la valeur de la propriété orderId.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Obtient la valeur de la propriété organizationId.
     */
    public short getOrganizationId() {
        return organizationId;
    }

    /**
     * Définit la valeur de la propriété organizationId.
     */
    public void setOrganizationId(short value) {
        this.organizationId = value;
    }

    /**
     * Obtient la valeur de la propriété companyprofileurl.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCOMPANYPROFILEURL() {
        return companyprofileurl;
    }

    /**
     * Définit la valeur de la propriété companyprofileurl.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCOMPANYPROFILEURL(String value) {
        this.companyprofileurl = value;
    }

    /**
     * Obtient la valeur de la propriété hierarchien.
     *
     * @return possible object is
     *         {@link ProspectiveJob.Hierarchien }
     */
    public ProspectiveJob.Hierarchien getHierarchien() {
        return hierarchien;
    }

    /**
     * Définit la valeur de la propriété hierarchien.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Hierarchien }
     */
    public void setHierarchien(ProspectiveJob.Hierarchien value) {
        this.hierarchien = value;
    }

    /**
     * Obtient la valeur de la propriété inseratId.
     */
    public int getInseratId() {
        return inseratId;
    }

    /**
     * Définit la valeur de la propriété inseratId.
     */
    public void setInseratId(int value) {
        this.inseratId = value;
    }

    /**
     * Obtient la valeur de la propriété medienname.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMedienname() {
        return medienname;
    }

    /**
     * Définit la valeur de la propriété medienname.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMedienname(String value) {
        this.medienname = value;
    }

    /**
     * Obtient la valeur de la propriété medienId.
     */
    public int getMedienId() {
        return medienId;
    }

    /**
     * Définit la valeur de la propriété medienId.
     */
    public void setMedienId(int value) {
        this.medienId = value;
    }

    /**
     * Obtient la valeur de la propriété kontoId.
     */
    public int getKontoId() {
        return kontoId;
    }

    /**
     * Définit la valeur de la propriété kontoId.
     */
    public void setKontoId(int value) {
        this.kontoId = value;
    }

    /**
     * Obtient la valeur de la propriété kundenname.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getKundenname() {
        return kundenname;
    }

    /**
     * Définit la valeur de la propriété kundenname.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKundenname(String value) {
        this.kundenname = value;
    }

    /**
     * Obtient la valeur de la propriété layout.
     */
    public int getLayout() {
        return layout;
    }

    /**
     * Définit la valeur de la propriété layout.
     */
    public void setLayout(int value) {
        this.layout = value;
    }

    /**
     * Obtient la valeur de la propriété logo.
     */
    public int getLogo() {
        return logo;
    }

    /**
     * Définit la valeur de la propriété logo.
     */
    public void setLogo(int value) {
        this.logo = value;
    }

    /**
     * Obtient la valeur de la propriété sprache.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSprache() {
        return sprache;
    }

    /**
     * Définit la valeur de la propriété sprache.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSprache(String value) {
        this.sprache = value;
    }

    /**
     * Obtient la valeur de la propriété datumStart.
     *
     * @return possible object is
     *         {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatumStart() {
        return datumStart;
    }

    /**
     * Définit la valeur de la propriété datumStart.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDatumStart(XMLGregorianCalendar value) {
        this.datumStart = value;
    }

    /**
     * Obtient la valeur de la propriété datumEnde.
     *
     * @return possible object is
     *         {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatumEnde() {
        return datumEnde;
    }

    /**
     * Définit la valeur de la propriété datumEnde.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDatumEnde(XMLGregorianCalendar value) {
        this.datumEnde = value;
    }

    /**
     * Obtient la valeur de la propriété datumMutation.
     *
     * @return possible object is
     *         {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatumMutation() {
        return datumMutation;
    }

    /**
     * Définit la valeur de la propriété datumMutation.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDatumMutation(XMLGregorianCalendar value) {
        this.datumMutation = value;
    }

    /**
     * Obtient la valeur de la propriété kontakt.
     *
     * @return possible object is
     *         {@link ProspectiveJob.Kontakt }
     */
    public ProspectiveJob.Kontakt getKontakt() {
        return kontakt;
    }

    /**
     * Définit la valeur de la propriété kontakt.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Kontakt }
     */
    public void setKontakt(ProspectiveJob.Kontakt value) {
        this.kontakt = value;
    }

    /**
     * Obtient la valeur de la propriété stellentitel.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getStellentitel() {
        return stellentitel;
    }

    /**
     * Définit la valeur de la propriété stellentitel.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStellentitel(String value) {
        this.stellentitel = value;
    }

    /**
     * Obtient la valeur de la propriété vorspann.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVorspann() {
        return vorspann;
    }

    /**
     * Définit la valeur de la propriété vorspann.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVorspann(String value) {
        this.vorspann = value;
    }

    /**
     * Obtient la valeur de la propriété texte.
     *
     * @return possible object is
     *         {@link ProspectiveJob.Texte }
     */
    public ProspectiveJob.Texte getTexte() {
        return texte;
    }

    /**
     * Définit la valeur de la propriété texte.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Texte }
     */
    public void setTexte(ProspectiveJob.Texte value) {
        this.texte = value;
    }

    /**
     * Obtient la valeur de la propriété metadaten.
     *
     * @return possible object is
     *         {@link ProspectiveJob.Metadaten }
     */
    public ProspectiveJob.Metadaten getMetadaten() {
        return metadaten;
    }

    /**
     * Définit la valeur de la propriété metadaten.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Metadaten }
     */
    public void setMetadaten(ProspectiveJob.Metadaten value) {
        this.metadaten = value;
    }

    /**
     * Obtient la valeur de la propriété urlBewerber.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUrlBewerber() {
        return urlBewerber;
    }

    /**
     * Définit la valeur de la propriété urlBewerber.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUrlBewerber(String value) {
        this.urlBewerber = value;
    }

    /**
     * Obtient la valeur de la propriété urlDirektlink.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUrlDirektlink() {
        return urlDirektlink;
    }

    /**
     * Définit la valeur de la propriété urlDirektlink.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUrlDirektlink(String value) {
        this.urlDirektlink = value;
    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * <p/>
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * <p/>
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="hierarchieid" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="hierarchietext" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "hierarchieid",
            "hierarchietext"
    })
    public static class Hierarchien {

        @XmlElement(required = true)
        protected String hierarchieid;
        @XmlElement(required = true)
        protected String hierarchietext;

        /**
         * Obtient la valeur de la propriété hierarchieid.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getHierarchieid() {
            return hierarchieid;
        }

        /**
         * Définit la valeur de la propriété hierarchieid.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setHierarchieid(String value) {
            this.hierarchieid = value;
        }

        /**
         * Obtient la valeur de la propriété hierarchietext.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getHierarchietext() {
            return hierarchietext;
        }

        /**
         * Définit la valeur de la propriété hierarchietext.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setHierarchietext(String value) {
            this.hierarchietext = value;
        }

    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * <p/>
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * <p/>
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="firma" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="abteilung" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="anrede" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="titel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="vorname" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="adresse1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="adresse2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="adresse3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="plz" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
     *         &lt;element name="ort" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="land" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="land_iso2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="land_iso3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="telefon" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="mail" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "firma",
            "abteilung",
            "anrede",
            "titel",
            "name",
            "vorname",
            "adresse1",
            "adresse2",
            "adresse3",
            "plz",
            "ort",
            "land",
            "landIso2",
            "landIso3",
            "telefon",
            "mail"
    })
    public static class Kontakt {

        @XmlElement(required = true)
        protected String firma;
        @XmlElement(required = true)
        protected String abteilung;
        @XmlElement(required = true)
        protected String anrede;
        @XmlElement(required = true)
        protected String titel;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String vorname;
        @XmlElement(required = true)
        protected String adresse1;
        @XmlElement(required = true)
        protected String adresse2;
        @XmlElement(required = true)
        protected String adresse3;
        protected short plz;
        @XmlElement(required = true)
        protected String ort;
        @XmlElement(required = true)
        protected String land;
        @XmlElement(name = "land_iso2", required = true)
        protected String landIso2;
        @XmlElement(name = "land_iso3", required = true)
        protected String landIso3;
        @XmlElement(required = true)
        protected String telefon;
        @XmlElement(required = true)
        protected String mail;

        /**
         * Obtient la valeur de la propriété firma.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getFirma() {
            return firma;
        }

        /**
         * Définit la valeur de la propriété firma.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFirma(String value) {
            this.firma = value;
        }

        /**
         * Obtient la valeur de la propriété abteilung.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getAbteilung() {
            return abteilung;
        }

        /**
         * Définit la valeur de la propriété abteilung.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAbteilung(String value) {
            this.abteilung = value;
        }

        /**
         * Obtient la valeur de la propriété anrede.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getAnrede() {
            return anrede;
        }

        /**
         * Définit la valeur de la propriété anrede.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAnrede(String value) {
            this.anrede = value;
        }

        /**
         * Obtient la valeur de la propriété titel.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getTitel() {
            return titel;
        }

        /**
         * Définit la valeur de la propriété titel.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTitel(String value) {
            this.titel = value;
        }

        /**
         * Obtient la valeur de la propriété name.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getName() {
            return name;
        }

        /**
         * Définit la valeur de la propriété name.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Obtient la valeur de la propriété vorname.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getVorname() {
            return vorname;
        }

        /**
         * Définit la valeur de la propriété vorname.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setVorname(String value) {
            this.vorname = value;
        }

        /**
         * Obtient la valeur de la propriété adresse1.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getAdresse1() {
            return adresse1;
        }

        /**
         * Définit la valeur de la propriété adresse1.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAdresse1(String value) {
            this.adresse1 = value;
        }

        /**
         * Obtient la valeur de la propriété adresse2.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getAdresse2() {
            return adresse2;
        }

        /**
         * Définit la valeur de la propriété adresse2.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAdresse2(String value) {
            this.adresse2 = value;
        }

        /**
         * Obtient la valeur de la propriété adresse3.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getAdresse3() {
            return adresse3;
        }

        /**
         * Définit la valeur de la propriété adresse3.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAdresse3(String value) {
            this.adresse3 = value;
        }

        /**
         * Obtient la valeur de la propriété plz.
         */
        public short getPlz() {
            return plz;
        }

        /**
         * Définit la valeur de la propriété plz.
         */
        public void setPlz(short value) {
            this.plz = value;
        }

        /**
         * Obtient la valeur de la propriété ort.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getOrt() {
            return ort;
        }

        /**
         * Définit la valeur de la propriété ort.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setOrt(String value) {
            this.ort = value;
        }

        /**
         * Obtient la valeur de la propriété land.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getLand() {
            return land;
        }

        /**
         * Définit la valeur de la propriété land.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLand(String value) {
            this.land = value;
        }

        /**
         * Obtient la valeur de la propriété landIso2.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getLandIso2() {
            return landIso2;
        }

        /**
         * Définit la valeur de la propriété landIso2.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLandIso2(String value) {
            this.landIso2 = value;
        }

        /**
         * Obtient la valeur de la propriété landIso3.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getLandIso3() {
            return landIso3;
        }

        /**
         * Définit la valeur de la propriété landIso3.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLandIso3(String value) {
            this.landIso3 = value;
        }

        /**
         * Obtient la valeur de la propriété telefon.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getTelefon() {
            return telefon;
        }

        /**
         * Définit la valeur de la propriété telefon.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTelefon(String value) {
            this.telefon = value;
        }

        /**
         * Obtient la valeur de la propriété mail.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getMail() {
            return mail;
        }

        /**
         * Définit la valeur de la propriété mail.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setMail(String value) {
            this.mail = value;
        }

    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * <p/>
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * <p/>
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="xjobcategory" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="jobcategory" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="xzipcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="zipcode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="xplace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="place" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="xpensumvon" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="pensumvon" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="xpensumbis" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
     *         &lt;element name="pensumbis" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="xjobduration" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="jobduration" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="xeducation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="education" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="xexperience" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="experience" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "xjobcategory",
            "jobcategory",
            "xzipcode",
            "zipcode",
            "xplace",
            "place",
            "xpensumvon",
            "pensumvon",
            "xpensumbis",
            "pensumbis",
            "xjobduration",
            "jobduration",
            "xeducation",
            "education",
            "xexperience",
            "experience"
    })
    public static class Metadaten {

        @XmlElement(required = true)
        protected String xjobcategory;
        protected int jobcategory;
        protected String xzipcode;
        @XmlElement(required = true)
        protected String zipcode;
        protected String xplace;
        @XmlElement(required = true)
        protected String place;
        protected Integer xpensumvon;
        protected int pensumvon;
        protected Integer xpensumbis;
        protected int pensumbis;
        @XmlElement(required = true)
        protected String xjobduration;
        protected int jobduration;
        @XmlElement(required = true)
        protected String xeducation;
        @XmlElement(required = true)
        protected String education;
        @XmlElement(required = true)
        protected String xexperience;
        @XmlElement(required = true)
        protected String experience;

        /**
         * Obtient la valeur de la propriété xjobcategory.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getXjobcategory() {
            return xjobcategory;
        }

        /**
         * Définit la valeur de la propriété xjobcategory.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXjobcategory(String value) {
            this.xjobcategory = value;
        }

        /**
         * Obtient la valeur de la propriété jobcategory.
         */
        public int getJobcategory() {
            return jobcategory;
        }

        /**
         * Définit la valeur de la propriété jobcategory.
         */
        public void setJobcategory(int value) {
            this.jobcategory = value;
        }

        /**
         * Obtient la valeur de la propriété xzipcode.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getXzipcode() {
            return xzipcode;
        }

        /**
         * Définit la valeur de la propriété xzipcode.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXzipcode(String value) {
            this.xzipcode = value;
        }

        /**
         * Obtient la valeur de la propriété zipcode.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getZipcode() {
            return zipcode;
        }

        /**
         * Définit la valeur de la propriété zipcode.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setZipcode(String value) {
            this.zipcode = value;
        }

        /**
         * Obtient la valeur de la propriété xplace.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getXplace() {
            return xplace;
        }

        /**
         * Définit la valeur de la propriété xplace.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXplace(String value) {
            this.xplace = value;
        }

        /**
         * Obtient la valeur de la propriété place.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getPlace() {
            return place;
        }

        /**
         * Définit la valeur de la propriété place.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPlace(String value) {
            this.place = value;
        }

        /**
         * Obtient la valeur de la propriété xpensumvon.
         *
         * @return possible object is
         *         {@link Integer }
         */
        public Integer getXpensumvon() {
            return xpensumvon;
        }

        /**
         * Définit la valeur de la propriété xpensumvon.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setXpensumvon(Integer value) {
            this.xpensumvon = value;
        }

        /**
         * Obtient la valeur de la propriété pensumvon.
         */
        public int getPensumvon() {
            return pensumvon;
        }

        /**
         * Définit la valeur de la propriété pensumvon.
         */
        public void setPensumvon(int value) {
            this.pensumvon = value;
        }

        /**
         * Obtient la valeur de la propriété xpensumbis.
         *
         * @return possible object is
         *         {@link Integer }
         */
        public Integer getXpensumbis() {
            return xpensumbis;
        }

        /**
         * Définit la valeur de la propriété xpensumbis.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setXpensumbis(Integer value) {
            this.xpensumbis = value;
        }

        /**
         * Obtient la valeur de la propriété pensumbis.
         */
        public int getPensumbis() {
            return pensumbis;
        }

        /**
         * Définit la valeur de la propriété pensumbis.
         */
        public void setPensumbis(int value) {
            this.pensumbis = value;
        }

        /**
         * Obtient la valeur de la propriété xjobduration.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getXjobduration() {
            return xjobduration;
        }

        /**
         * Définit la valeur de la propriété xjobduration.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXjobduration(String value) {
            this.xjobduration = value;
        }

        /**
         * Obtient la valeur de la propriété jobduration.
         */
        public int getJobduration() {
            return jobduration;
        }

        /**
         * Définit la valeur de la propriété jobduration.
         */
        public void setJobduration(int value) {
            this.jobduration = value;
        }

        /**
         * Obtient la valeur de la propriété xeducation.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getXeducation() {
            return xeducation;
        }

        /**
         * Définit la valeur de la propriété xeducation.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXeducation(String value) {
            this.xeducation = value;
        }

        /**
         * Obtient la valeur de la propriété education.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getEducation() {
            return education;
        }

        /**
         * Définit la valeur de la propriété education.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setEducation(String value) {
            this.education = value;
        }

        /**
         * Obtient la valeur de la propriété xexperience.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getXexperience() {
            return xexperience;
        }

        /**
         * Définit la valeur de la propriété xexperience.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXexperience(String value) {
            this.xexperience = value;
        }

        /**
         * Obtient la valeur de la propriété experience.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getExperience() {
            return experience;
        }

        /**
         * Définit la valeur de la propriété experience.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setExperience(String value) {
            this.experience = value;
        }

    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * <p/>
     * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
     * <p/>
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="text_1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="text_2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="text_3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="text_4" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="text_5" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="text_6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="text_7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "text1",
            "text2",
            "text3",
            "text4",
            "text5",
            "text6",
            "text7"
    })
    public static class Texte {

        @XmlElement(name = "text_1", required = true)
        protected String text1;
        @XmlElement(name = "text_2", required = true)
        protected String text2;
        @XmlElement(name = "text_3", required = true)
        protected String text3;
        @XmlElement(name = "text_4", required = true)
        protected String text4;
        @XmlElement(name = "text_5", required = true)
        protected String text5;
        @XmlElement(name = "text_6")
        protected String text6;
        @XmlElement(name = "text_7")
        protected String text7;

        /**
         * Obtient la valeur de la propriété text1.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText1() {
            return text1;
        }

        /**
         * Définit la valeur de la propriété text1.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText1(String value) {
            this.text1 = value;
        }

        /**
         * Obtient la valeur de la propriété text2.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText2() {
            return text2;
        }

        /**
         * Définit la valeur de la propriété text2.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText2(String value) {
            this.text2 = value;
        }

        /**
         * Obtient la valeur de la propriété text3.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText3() {
            return text3;
        }

        /**
         * Définit la valeur de la propriété text3.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText3(String value) {
            this.text3 = value;
        }

        /**
         * Obtient la valeur de la propriété text4.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText4() {
            return text4;
        }

        /**
         * Définit la valeur de la propriété text4.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText4(String value) {
            this.text4 = value;
        }

        /**
         * Obtient la valeur de la propriété text5.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText5() {
            return text5;
        }

        /**
         * Définit la valeur de la propriété text5.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText5(String value) {
            this.text5 = value;
        }

        /**
         * Obtient la valeur de la propriété text6.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText6() {
            return text6;
        }

        /**
         * Définit la valeur de la propriété text6.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText6(String value) {
            this.text6 = value;
        }

        /**
         * Obtient la valeur de la propriété text7.
         *
         * @return possible object is
         *         {@link String }
         */
        public String getText7() {
            return text7;
        }

        /**
         * Définit la valeur de la propriété text7.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText7(String value) {
            this.text7 = value;
        }

    }

}

