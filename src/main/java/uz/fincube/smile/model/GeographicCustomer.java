package uz.fincube.smile.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class GeographicCustomer {

    private int id;

    private String state;

    @XmlElement(name = "regional_unemployment_rate")
    private double regionalUnemploymentRate;

    @XmlElement(name = "regional_median_income")
    private int regionalMedianIncome;

    @XmlElement(name = "regional_median_rent")
    private double regionalMedianRent;

    @XmlElement(name = "housing_price_index")
    private double housingPriceIndex;

    @XmlElement(name = "cost_of_living_index")
    private double costOfLivingIndex;

    @XmlElement(name = "previous_zip_code")
    private String previousZipCode;

    // getters/setters можно оставить, JAXB их игнорирует из-за XmlAccessType.FIELD
}
