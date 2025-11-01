package com.maheshbhatt.financialnuggets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String department;

}
