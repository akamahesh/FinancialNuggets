package com.maheshbhatt.financialnuggets.service;

import com.maheshbhatt.financialnuggets.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee save(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(String employeeId);
    String deleteEmployeeById(String employeeId);
}
