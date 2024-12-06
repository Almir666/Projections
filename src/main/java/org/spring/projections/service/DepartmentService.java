package org.spring.projections.service;

import lombok.AllArgsConstructor;
import org.spring.projections.model.Department;
import org.spring.projections.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
}
