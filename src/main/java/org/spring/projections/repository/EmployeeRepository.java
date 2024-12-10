package org.spring.projections.repository;

import org.spring.projections.model.Employee;
import org.spring.projections.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("""
        SELECT e.firstName || ' ' || e.lastName AS fullName,
               e.position AS position,
               d.name AS departmentName
        FROM Employee e
        JOIN e.department d WHERE e.id = :id
        """)
    List<EmployeeProjection> getProjections(long id);
}
