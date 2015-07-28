package ch.alv.batches.partnerjobs.jaxb;

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
    @XmlElement(name = "sprache", required = true)
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
     * Ruft den Wert der posterUrl-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * Legt den Wert der posterUrl-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPosterUrl(String value) {
        this.posterUrl = value;
    }

    /**
     * Ruft den Wert der orderId-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Legt den Wert der orderId-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Ruft den Wert der organizationId-Eigenschaft ab.
     */
    public short getOrganizationId() {
        return organizationId;
    }

    /**
     * Legt den Wert der organizationId-Eigenschaft fest.
     */
    public void setOrganizationId(short value) {
        this.organizationId = value;
    }

    /**
     * Ruft den Wert der companyprofileurl-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCOMPANYPROFILEURL() {
        return companyprofileurl;
    }

    /**
     * Legt den Wert der companyprofileurl-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCOMPANYPROFILEURL(String value) {
        this.companyprofileurl = value;
    }

    /**
     * Ruft den Wert der hierarchien-Eigenschaft ab.
     *
     * @return possible object is
     * {@link ProspectiveJob.Hierarchien }
     */
    public ProspectiveJob.Hierarchien getHierarchien() {
        return hierarchien;
    }

    /**
     * Legt den Wert der hierarchien-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Hierarchien }
     */
    public void setHierarchien(ProspectiveJob.Hierarchien value) {
        this.hierarchien = value;
    }

    /**
     * Ruft den Wert der inseratId-Eigenschaft ab.
     */
    public int getInseratId() {
        return inseratId;
    }

    /**
     * Legt den Wert der inseratId-Eigenschaft fest.
     */
    public void setInseratId(int value) {
        this.inseratId = value;
    }

    /**
     * Ruft den Wert der medienname-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMedienname() {
        return medienname;
    }

    /**
     * Legt den Wert der medienname-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMedienname(String value) {
        this.medienname = value;
    }

    /**
     * Ruft den Wert der medienId-Eigenschaft ab.
     */
    public int getMedienId() {
        return medienId;
    }

    /**
     * Legt den Wert der medienId-Eigenschaft fest.
     */
    public void setMedienId(int value) {
        this.medienId = value;
    }

    /**
     * Ruft den Wert der kontoId-Eigenschaft ab.
     */
    public int getKontoId() {
        return kontoId;
    }

    /**
     * Legt den Wert der kontoId-Eigenschaft fest.
     */
    public void setKontoId(int value) {
        this.kontoId = value;
    }

    /**
     * Ruft den Wert der kundenname-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getKundenname() {
        return kundenname;
    }

    /**
     * Legt den Wert der kundenname-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setKundenname(String value) {
        this.kundenname = value;
    }

    /**
     * Ruft den Wert der layout-Eigenschaft ab.
     */
    public int getLayout() {
        return layout;
    }

    /**
     * Legt den Wert der layout-Eigenschaft fest.
     */
    public void setLayout(int value) {
        this.layout = value;
    }

    /**
     * Ruft den Wert der logo-Eigenschaft ab.
     */
    public int getLogo() {
        return logo;
    }

    /**
     * Legt den Wert der logo-Eigenschaft fest.
     */
    public void setLogo(int value) {
        this.logo = value;
    }

    /**
     * Ruft den Wert der sprache-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSprache() {
        return sprache;
    }

    /**
     * Legt den Wert der sprache-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSprache(String value) {
        this.sprache = value;
    }

    /**
     * Ruft den Wert der datumStart-Eigenschaft ab.
     *
     * @return possible object is
     * {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatumStart() {
        return datumStart;
    }

    /**
     * Legt den Wert der datumStart-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setDatumStart(XMLGregorianCalendar value) {
        this.datumStart = value;
    }

    /**
     * Ruft den Wert der datumEnde-Eigenschaft ab.
     *
     * @return possible object is
     * {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatumEnde() {
        return datumEnde;
    }

    /**
     * Legt den Wert der datumEnde-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setDatumEnde(XMLGregorianCalendar value) {
        this.datumEnde = value;
    }

    /**
     * Ruft den Wert der datumMutation-Eigenschaft ab.
     *
     * @return possible object is
     * {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDatumMutation() {
        return datumMutation;
    }

    /**
     * Legt den Wert der datumMutation-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link javax.xml.datatype.XMLGregorianCalendar }
     */
    public void setDatumMutation(XMLGregorianCalendar value) {
        this.datumMutation = value;
    }

    /**
     * Ruft den Wert der kontakt-Eigenschaft ab.
     *
     * @return possible object is
     * {@link ProspectiveJob.Kontakt }
     */
    public ProspectiveJob.Kontakt getKontakt() {
        return kontakt;
    }

    /**
     * Legt den Wert der kontakt-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Kontakt }
     */
    public void setKontakt(ProspectiveJob.Kontakt value) {
        this.kontakt = value;
    }

    /**
     * Ruft den Wert der stellentitel-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStellentitel() {
        return stellentitel;
    }

    /**
     * Legt den Wert der stellentitel-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStellentitel(String value) {
        this.stellentitel = value;
    }

    /**
     * Ruft den Wert der vorspann-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVorspann() {
        return vorspann;
    }

    /**
     * Legt den Wert der vorspann-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVorspann(String value) {
        this.vorspann = value;
    }

    /**
     * Ruft den Wert der texte-Eigenschaft ab.
     *
     * @return possible object is
     * {@link ProspectiveJob.Texte }
     */
    public ProspectiveJob.Texte getTexte() {
        return texte;
    }

    /**
     * Legt den Wert der texte-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Texte }
     */
    public void setTexte(ProspectiveJob.Texte value) {
        this.texte = value;
    }

    /**
     * Ruft den Wert der metadaten-Eigenschaft ab.
     *
     * @return possible object is
     * {@link ProspectiveJob.Metadaten }
     */
    public ProspectiveJob.Metadaten getMetadaten() {
        return metadaten;
    }

    /**
     * Legt den Wert der metadaten-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link ProspectiveJob.Metadaten }
     */
    public void setMetadaten(ProspectiveJob.Metadaten value) {
        this.metadaten = value;
    }

    /**
     * Ruft den Wert der urlBewerber-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUrlBewerber() {
        return urlBewerber;
    }

    /**
     * Legt den Wert der urlBewerber-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUrlBewerber(String value) {
        this.urlBewerber = value;
    }

    /**
     * Ruft den Wert der urlDirektlink-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUrlDirektlink() {
        return urlDirektlink;
    }

    /**
     * Legt den Wert der urlDirektlink-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUrlDirektlink(String value) {
        this.urlDirektlink = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * <p/>
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="hierarchieid" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="hierarchietext" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
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
         * Ruft den Wert der hierarchieid-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getHierarchieid() {
            return hierarchieid;
        }

        /**
         * Legt den Wert der hierarchieid-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setHierarchieid(String value) {
            this.hierarchieid = value;
        }

        /**
         * Ruft den Wert der hierarchietext-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getHierarchietext() {
            return hierarchietext;
        }

        /**
         * Legt den Wert der hierarchietext-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setHierarchietext(String value) {
            this.hierarchietext = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * <p/>
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="firma" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="abteilung" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="anrede" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="titel" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="vorname" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="adresse1" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="adresse2" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="adresse3" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="plz" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *         &lt;element name="ort" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="land" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="land_iso2" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="land_iso3" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="telefon" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="mail" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
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
         * Ruft den Wert der firma-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getFirma() {
            return firma;
        }

        /**
         * Legt den Wert der firma-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setFirma(String value) {
            this.firma = value;
        }

        /**
         * Ruft den Wert der abteilung-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAbteilung() {
            return abteilung;
        }

        /**
         * Legt den Wert der abteilung-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAbteilung(String value) {
            this.abteilung = value;
        }

        /**
         * Ruft den Wert der anrede-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAnrede() {
            return anrede;
        }

        /**
         * Legt den Wert der anrede-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAnrede(String value) {
            this.anrede = value;
        }

        /**
         * Ruft den Wert der titel-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTitel() {
            return titel;
        }

        /**
         * Legt den Wert der titel-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTitel(String value) {
            this.titel = value;
        }

        /**
         * Ruft den Wert der name-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getName() {
            return name;
        }

        /**
         * Legt den Wert der name-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Ruft den Wert der vorname-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getVorname() {
            return vorname;
        }

        /**
         * Legt den Wert der vorname-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setVorname(String value) {
            this.vorname = value;
        }

        /**
         * Ruft den Wert der adresse1-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAdresse1() {
            return adresse1;
        }

        /**
         * Legt den Wert der adresse1-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAdresse1(String value) {
            this.adresse1 = value;
        }

        /**
         * Ruft den Wert der adresse2-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAdresse2() {
            return adresse2;
        }

        /**
         * Legt den Wert der adresse2-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAdresse2(String value) {
            this.adresse2 = value;
        }

        /**
         * Ruft den Wert der adresse3-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getAdresse3() {
            return adresse3;
        }

        /**
         * Legt den Wert der adresse3-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setAdresse3(String value) {
            this.adresse3 = value;
        }

        /**
         * Ruft den Wert der plz-Eigenschaft ab.
         */
        public short getPlz() {
            return plz;
        }

        /**
         * Legt den Wert der plz-Eigenschaft fest.
         */
        public void setPlz(short value) {
            this.plz = value;
        }

        /**
         * Ruft den Wert der ort-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getOrt() {
            return ort;
        }

        /**
         * Legt den Wert der ort-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setOrt(String value) {
            this.ort = value;
        }

        /**
         * Ruft den Wert der land-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getLand() {
            return land;
        }

        /**
         * Legt den Wert der land-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLand(String value) {
            this.land = value;
        }

        /**
         * Ruft den Wert der landIso2-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getLandIso2() {
            return landIso2;
        }

        /**
         * Legt den Wert der landIso2-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLandIso2(String value) {
            this.landIso2 = value;
        }

        /**
         * Ruft den Wert der landIso3-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getLandIso3() {
            return landIso3;
        }

        /**
         * Legt den Wert der landIso3-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setLandIso3(String value) {
            this.landIso3 = value;
        }

        /**
         * Ruft den Wert der telefon-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTelefon() {
            return telefon;
        }

        /**
         * Legt den Wert der telefon-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTelefon(String value) {
            this.telefon = value;
        }

        /**
         * Ruft den Wert der mail-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getMail() {
            return mail;
        }

        /**
         * Legt den Wert der mail-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setMail(String value) {
            this.mail = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * <p/>
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="xcontracttype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="contracttype" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="xjobstatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="jobstatus" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="xindustries" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="industries" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="xplz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="plz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="xjoblocationcity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="joblocationcity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="xlocations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="locations" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/>
     *         &lt;element name="xcareerlevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="careerlevel" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="xjobsummary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="jobsummary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="xkeywords" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="keywords" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="rubrik" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="xrubrik" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="xtmp_10" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="tmp_10" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="xtmp_20" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="tmp_20" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="xtmp_30" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="tmp_30" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "xcontracttype",
            "contracttype",
            "xjobstatus",
            "jobstatus",
            "xindustries",
            "industries",
            "xplz",
            "plz",
            "xjoblocationcity",
            "joblocationcity",
            "xlocations",
            "locations",
            "xcareerlevel",
            "careerlevel",
            "xjobsummary",
            "jobsummary",
            "xkeywords",
            "keywords",
            "rubrik",
            "xrubrik",
            "xtmp10",
            "tmp10",
            "xtmp20",
            "tmp20",
            "xtmp30",
            "tmp30"
    })
    public static class Metadaten {

        protected String xcontracttype;
        protected Integer contracttype;
        protected String xjobstatus;
        protected Integer jobstatus;
        protected String xindustries;
        protected Integer industries;
        protected String xplz;
        protected String plz;
        protected String xjoblocationcity;
        protected String joblocationcity;
        protected String xlocations;
        protected Short locations;
        protected String xcareerlevel;
        protected Integer careerlevel;
        protected String xjobsummary;
        protected String jobsummary;
        protected String xkeywords;
        protected String keywords;
        protected String rubrik;
        protected String xrubrik;
        @XmlElement(name = "xtmp_10", required = true)
        protected String xtmp10;
        @XmlElement(name = "tmp_10", required = true)
        protected String tmp10;
        @XmlElement(name = "xtmp_20", required = true)
        protected String xtmp20;
        @XmlElement(name = "tmp_20", required = true)
        protected String tmp20;
        @XmlElement(name = "xtmp_30", required = true)
        protected String xtmp30;
        @XmlElement(name = "tmp_30", required = true)
        protected String tmp30;

        /**
         * Ruft den Wert der xcontracttype-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXcontracttype() {
            return xcontracttype;
        }

        /**
         * Legt den Wert der xcontracttype-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXcontracttype(String value) {
            this.xcontracttype = value;
        }

        /**
         * Ruft den Wert der contracttype-Eigenschaft ab.
         *
         * @return possible object is
         * {@link Integer }
         */
        public Integer getContracttype() {
            return contracttype;
        }

        /**
         * Legt den Wert der contracttype-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setContracttype(Integer value) {
            this.contracttype = value;
        }

        /**
         * Ruft den Wert der xjobstatus-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXjobstatus() {
            return xjobstatus;
        }

        /**
         * Legt den Wert der xjobstatus-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXjobstatus(String value) {
            this.xjobstatus = value;
        }

        /**
         * Ruft den Wert der jobstatus-Eigenschaft ab.
         *
         * @return possible object is
         * {@link Integer }
         */
        public Integer getJobstatus() {
            return jobstatus;
        }

        /**
         * Legt den Wert der jobstatus-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setJobstatus(Integer value) {
            this.jobstatus = value;
        }

        /**
         * Ruft den Wert der xindustries-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXindustries() {
            return xindustries;
        }

        /**
         * Legt den Wert der xindustries-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXindustries(String value) {
            this.xindustries = value;
        }

        /**
         * Ruft den Wert der industries-Eigenschaft ab.
         *
         * @return possible object is
         * {@link Integer }
         */
        public Integer getIndustries() {
            return industries;
        }

        /**
         * Legt den Wert der industries-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setIndustries(Integer value) {
            this.industries = value;
        }

        /**
         * Ruft den Wert der xplz-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXplz() {
            return xplz;
        }

        /**
         * Legt den Wert der xplz-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXplz(String value) {
            this.xplz = value;
        }

        /**
         * Ruft den Wert der plz-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getPlz() {
            return plz;
        }

        /**
         * Legt den Wert der plz-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setPlz(String value) {
            this.plz = value;
        }

        /**
         * Ruft den Wert der xjoblocationcity-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXjoblocationcity() {
            return xjoblocationcity;
        }

        /**
         * Legt den Wert der xjoblocationcity-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXjoblocationcity(String value) {
            this.xjoblocationcity = value;
        }

        /**
         * Ruft den Wert der joblocationcity-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getJoblocationcity() {
            return joblocationcity;
        }

        /**
         * Legt den Wert der joblocationcity-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setJoblocationcity(String value) {
            this.joblocationcity = value;
        }

        /**
         * Ruft den Wert der xlocations-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXlocations() {
            return xlocations;
        }

        /**
         * Legt den Wert der xlocations-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXlocations(String value) {
            this.xlocations = value;
        }

        /**
         * Ruft den Wert der locations-Eigenschaft ab.
         *
         * @return possible object is
         * {@link Short }
         */
        public Short getLocations() {
            return locations;
        }

        /**
         * Legt den Wert der locations-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link Short }
         */
        public void setLocations(Short value) {
            this.locations = value;
        }

        /**
         * Ruft den Wert der xcareerlevel-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXcareerlevel() {
            return xcareerlevel;
        }

        /**
         * Legt den Wert der xcareerlevel-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXcareerlevel(String value) {
            this.xcareerlevel = value;
        }

        /**
         * Ruft den Wert der careerlevel-Eigenschaft ab.
         *
         * @return possible object is
         * {@link Integer }
         */
        public Integer getCareerlevel() {
            return careerlevel;
        }

        /**
         * Legt den Wert der careerlevel-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link Integer }
         */
        public void setCareerlevel(Integer value) {
            this.careerlevel = value;
        }

        /**
         * Ruft den Wert der xjobsummary-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXjobsummary() {
            return xjobsummary;
        }

        /**
         * Legt den Wert der xjobsummary-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXjobsummary(String value) {
            this.xjobsummary = value;
        }

        /**
         * Ruft den Wert der jobsummary-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getJobsummary() {
            return jobsummary;
        }

        /**
         * Legt den Wert der jobsummary-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setJobsummary(String value) {
            this.jobsummary = value;
        }

        /**
         * Ruft den Wert der xkeywords-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXkeywords() {
            return xkeywords;
        }

        /**
         * Legt den Wert der xkeywords-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXkeywords(String value) {
            this.xkeywords = value;
        }

        /**
         * Ruft den Wert der keywords-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getKeywords() {
            return keywords;
        }

        /**
         * Legt den Wert der keywords-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setKeywords(String value) {
            this.keywords = value;
        }

        /**
         * Ruft den Wert der rubrik-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getRubrik() {
            return rubrik;
        }

        /**
         * Legt den Wert der rubrik-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setRubrik(String value) {
            this.rubrik = value;
        }

        /**
         * Ruft den Wert der xrubrik-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXrubrik() {
            return xrubrik;
        }

        /**
         * Legt den Wert der xrubrik-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXrubrik(String value) {
            this.xrubrik = value;
        }

        /**
         * Ruft den Wert der xtmp10-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXtmp10() {
            return xtmp10;
        }

        /**
         * Legt den Wert der xtmp10-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXtmp10(String value) {
            this.xtmp10 = value;
        }

        /**
         * Ruft den Wert der tmp10-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTmp10() {
            return tmp10;
        }

        /**
         * Legt den Wert der tmp10-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTmp10(String value) {
            this.tmp10 = value;
        }

        /**
         * Ruft den Wert der xtmp20-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXtmp20() {
            return xtmp20;
        }

        /**
         * Legt den Wert der xtmp20-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXtmp20(String value) {
            this.xtmp20 = value;
        }

        /**
         * Ruft den Wert der tmp20-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTmp20() {
            return tmp20;
        }

        /**
         * Legt den Wert der tmp20-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTmp20(String value) {
            this.tmp20 = value;
        }

        /**
         * Ruft den Wert der xtmp30-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getXtmp30() {
            return xtmp30;
        }

        /**
         * Legt den Wert der xtmp30-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setXtmp30(String value) {
            this.xtmp30 = value;
        }

        /**
         * Ruft den Wert der tmp30-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getTmp30() {
            return tmp30;
        }

        /**
         * Legt den Wert der tmp30-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTmp30(String value) {
            this.tmp30 = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     * <p/>
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     * <p/>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="text_1" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="text_2" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="text_3" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="text_4" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="text_5" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "text1",
            "text2",
            "text3",
            "text4",
            "text5"
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

        /**
         * Ruft den Wert der text1-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getText1() {
            return text1;
        }

        /**
         * Legt den Wert der text1-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText1(String value) {
            this.text1 = value;
        }

        /**
         * Ruft den Wert der text2-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getText2() {
            return text2;
        }

        /**
         * Legt den Wert der text2-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText2(String value) {
            this.text2 = value;
        }

        /**
         * Ruft den Wert der text3-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getText3() {
            return text3;
        }

        /**
         * Legt den Wert der text3-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText3(String value) {
            this.text3 = value;
        }

        /**
         * Ruft den Wert der text4-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getText4() {
            return text4;
        }

        /**
         * Legt den Wert der text4-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText4(String value) {
            this.text4 = value;
        }

        /**
         * Ruft den Wert der text5-Eigenschaft ab.
         *
         * @return possible object is
         * {@link String }
         */
        public String getText5() {
            return text5;
        }

        /**
         * Legt den Wert der text5-Eigenschaft fest.
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setText5(String value) {
            this.text5 = value;
        }

    }
}
