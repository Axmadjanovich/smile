package uz.fincube.smile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "demographics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Demography {

    @Id
    @Column(name = "cust_id")
    private Long customerId;   // customer ID

    @Column(name = "age")
    private Integer age;

    @Column(name = "annual_income")
    private Double annualIncome;

    @Column(name = "employment_length")
    private Double employmentLength;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "education")
    private String education;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "num_dependents")
    private Integer numDependents;
}
