package org.spring.projections.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spring.projections.dto.EmployeeRequest;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String position;

    private double salary;


    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.ALL})
    @JoinColumn(name = "department_id")
    private Department department;

    public static EmployeeRequest toEmployeeRequest(Employee employee) {
        EmployeeRequest employeeRequest = new EmployeeRequest(employee.getFirstName(),
                employee.getLastName(),
                employee.getPosition(),
                employee.getSalary(),
                employee.getDepartment().getId());

        return employeeRequest;
    }
}
