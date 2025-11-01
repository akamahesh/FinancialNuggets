package com.maheshbhatt.financialnuggets.service.impl;

import com.maheshbhatt.financialnuggets.entity.EmployeeEntity;
import com.maheshbhatt.financialnuggets.model.Employee;
import com.maheshbhatt.financialnuggets.repository.EmployeeRepository;
import com.maheshbhatt.financialnuggets.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeV2ServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee save(Employee employee) {
        if (employee.getEmployeeId() == null || employee.getEmployeeId().isEmpty()) {
            employee.setEmployeeId(UUID.randomUUID().toString());
        }
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employee, employeeEntity);
        employeeRepository.save(employeeEntity);
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        List<Employee> employees = employeeEntities.stream().map(entity -> {
            Employee emp = new Employee();
            BeanUtils.copyProperties(entity, emp);
            return emp;
        }).toList();

        return employees;
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).get();
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeEntity, employee);
        return employee;
    }

    @Override
    public String deleteEmployeeById(String employeeId) {
        employeeRepository.deleteById(employeeId);
        return "Employee deleted with the id: " + employeeId;
    }
}
