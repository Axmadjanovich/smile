package uz.fincube.smile.model;

import lombok.Data;

@Data
public class ApplicationMetadata {
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
