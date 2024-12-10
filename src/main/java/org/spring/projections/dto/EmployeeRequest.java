package org.spring.projections.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spring.projections.model.Employee;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String firstName;

    private String lastName;

    private String position;

    private double salary;

    private long departmentId;

    public static Employee toEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());

        return employee;
    }
}
