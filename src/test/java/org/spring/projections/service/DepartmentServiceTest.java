package org.spring.projections.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.projections.model.Department;
import org.spring.projections.repository.DepartmentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    void createDepartmentTest() {
        Department department = new Department(1L, "IT");
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department testDepartment = departmentService.createDepartment(department);

        assertEquals("IT", testDepartment.getName());
    }
}
