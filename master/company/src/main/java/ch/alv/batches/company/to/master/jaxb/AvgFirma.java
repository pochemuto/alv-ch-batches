//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2015.07.28 um 07:43:43 AM CEST
//


package ch.alv.batches.company.to.master.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
/**
 * This class is a java representation of the AVAMPSTS xml used to deliver
 * company master data. Originally it has been generated with
 * help of the maven-jaxb plugin and then it has adopted to match convenience and
 * spring batch needs.
*/
/**
 * <p>Java-Klasse für avgbetrieb complex type.
 * <p/>
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p/>
 * <pre>
 * &lt;complexType name="avgbetrieb">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bezeichnung">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="150"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Strasse" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PLZ">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Ort">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Telefonnummer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Email" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GStelle" type="{}avggstelle" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Leiter" type="{}avgleiter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "avgbetrieb", propOrder = {
        "id",
        "bezeichnung",
        "strasse",
        "plz",
        "ort",
        "telefonnummer",
        "email",
        "gStelle",
        "leiter"
})
@XmlRootElement(name = "Betrieb")
public class AvgFirma {

    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "Bezeichnung", required = true)
    protected String bezeichnung;
    @XmlElement(name = "Strasse")
    protected String strasse;
    @XmlElement(name = "PLZ", required = true)
    protected String plz;
    @XmlElement(name = "Ort", required = true)
    protected String ort;
    @XmlElement(name = "Telefonnummer")
    protected String telefonnummer;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "GStelle")
    protected List<Avggstelle> gStelle;
    @XmlElement(name = "Leiter")
    protected List<Avgleiter> leiter;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der bezeichnung-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBezeichnung() {
        return bezeichnung;
    }

    /**
     * Legt den Wert der bezeichnung-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBezeichnung(String value) {
        this.bezeichnung = value;
    }

    /**
     * Ruft den Wert der strasse-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Legt den Wert der strasse-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStrasse(String value) {
        this.strasse = value;
    }

    /**
     * Ruft den Wert der plz-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPLZ() {
        return plz;
    }

    /**
     * Legt den Wert der plz-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPLZ(String value) {
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
     * Ruft den Wert der telefonnummer-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTelefonnummer() {
        return telefonnummer;
    }

    /**
     * Legt den Wert der telefonnummer-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTelefonnummer(String value) {
        this.telefonnummer = value;
    }

    /**
     * Ruft den Wert der email-Eigenschaft ab.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail() {
        return email;
    }

    /**
     * Legt den Wert der email-Eigenschaft fest.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the gStelle property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gStelle property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGStelle().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Avggstelle }
     */
    public List<Avggstelle> getGStelle() {
        if (gStelle == null) {
            gStelle = new ArrayList<Avggstelle>();
        }
        return this.gStelle;
    }

    /**
     * Gets the value of the leiter property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leiter property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeiter().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Avgleiter }
     */
    public List<Avgleiter> getLeiter() {
        if (leiter == null) {
            leiter = new ArrayList<Avgleiter>();
        }
        return this.leiter;
    }

}
