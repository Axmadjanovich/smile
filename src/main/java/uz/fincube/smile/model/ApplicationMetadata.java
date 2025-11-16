package uz.fincube.smile.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ApplicationMetadata {
    @Id
    public int customerRef;
    public int applicationId;
    public int applicationHour;
    public int applicationDayOfWeek;
    public int accountOpenYear;
    public String preferredContact;
    public String referralCode;
    public String accountStatusCode;
    public double randomNoise1;
    public int numLoginSessions;
    public int numCustomerServiceCalls;
    public boolean hasMobileApp;
    public boolean paperlessBilling;
    public int defaultFlag;

}
