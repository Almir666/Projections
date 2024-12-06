package org.spring.projections.service;

import lombok.AllArgsConstructor;
import org.spring.projections.dto.EmployeeRequest;
import org.spring.projections.model.Department;
import org.spring.projections.model.Employee;
import org.spring.projections.projection.EmployeeProjection;
import org.spring.projections.repository.DepartmentRepository;
import org.spring.projections.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<EmployeeProjection> getProjections(long id) {
        return employeeRepository.getProjections(id);
    }

    public Employee createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = EmployeeRequest.toEntity(employeeRequest);
        Department department = departmentRepository.findById(employeeRequest.getDepartmentId()).orElseThrow(RuntimeException::new);
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee employee) {
        Employee newEmployee = employeeRepository.findById(employee.getId()).orElseThrow(RuntimeException::new);
        newEmployee.setFirstName(employee.getFirstName());
        newEmployee.setLastName(employee.getLastName());
        newEmployee.setDepartment(employee.getDepartment());
        newEmployee.setPosition(employee.getPosition());
        newEmployee.setSalary(employee.getSalary());

        return employeeRepository.save(newEmployee);
    }

    public Employee deleteEmployee(long id) {
        Employee deletedEmployee = employeeRepository.findById(id).orElseThrow(RuntimeException::new);
        employeeRepository.deleteById(id);

        return deletedEmployee;
    }
}
