package com.maheshbhatt.financialnuggets.controller;

import com.maheshbhatt.financialnuggets.model.Employee;
import com.maheshbhatt.financialnuggets.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Qualifier("employeeServiceImpl")
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/id/")
    public Employee findEmployeeById(@RequestParam String employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @DeleteMapping
    public String deleteEmployeeById(@RequestParam String employeeId) {
        return employeeService.deleteEmployeeById(employeeId);
    }

    @GetMapping("/info")
    public String getEmployeeInfo() {
        return "Employee Info Endpoint";
    }

}
