package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.exception.EmployeeNonFoundException;
import com.maheshbhatt.financialnuggets.model.Employee;
import com.maheshbhatt.financialnuggets.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    List<Employee> employees = new ArrayList<>();

    @Override
    public Employee save(Employee employee) {
        if (employee.getEmployeeId() == null || employee.getEmployeeId().isEmpty()) {
            employee.setEmployeeId(UUID.randomUUID().toString());
        }
        employees.add(employee);
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        return employees
                .stream()
                .filter(emp -> emp.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new EmployeeNonFoundException("Employee not found with ID: " + employeeId));
    }

    @Override
    public String deleteEmployeeById(String employeeId) {
        Employee employee = employees.stream().filter(e -> e.getEmployeeId().equals(employeeId)).findFirst()
                .orElseThrow(() -> new EmployeeNonFoundException("Employee not found with ID: " + employeeId));
        employees.remove(employee);
        return "Employee deleted with ID: " + employeeId;
    }
}
