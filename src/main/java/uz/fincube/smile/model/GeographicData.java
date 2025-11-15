package uz.fincube.smile.model;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "geographic_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeographicData {

    @XmlElement(name = "customer")
    public List<GeographicCustomer> customers;
}
