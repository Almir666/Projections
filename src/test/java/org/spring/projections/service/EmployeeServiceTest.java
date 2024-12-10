package org.spring.projections.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.projections.dto.EmployeeRequest;
import org.spring.projections.model.Department;
import org.spring.projections.model.Employee;
import org.spring.projections.projection.EmployeeProjection;
import org.spring.projections.repository.DepartmentRepository;
import org.spring.projections.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    void testGetAllEmployees() {
        List<Employee> mockEmployees = List.of(
                new Employee(1L, "Almir", "Shammasov", "Developer", 5000.0, null),
                new Employee(2L, "Alexandr", "Naminof", "HR", 4000.0, null)
        );
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        List<Employee> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetProjections() {
        List<EmployeeProjection> mockProjections = List.of(
                new EmployeeProjection() {
                    @Override
                    public String getFullName() {
                        return "Almir Shammasov";
                    }

                    @Override
                    public String getPosition() {
                        return "Developer";
                    }

                    @Override
                    public String getDepartmentName() {
                        return "IT";
                    }
                }
        );
        when(employeeRepository.getProjections(1L)).thenReturn(mockProjections);

        List<EmployeeProjection> projections = employeeService.getProjections(1L);

        assertEquals(1, projections.size());
        assertEquals("Almir Shammasov", projections.get(0).getFullName());
        verify(employeeRepository, times(1)).getProjections(1L);
    }

    @Test
    void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest("Almir", "Shammasov", "Developer", 5000.0, 1L);
        Department department = new Department(1L, "IT", null);
        Employee employee = EmployeeRequest.toEntity(request);
        employee.setDepartment(department);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee createdEmployee = employeeService.createEmployee(request);

        assertNotNull(createdEmployee);
        assertEquals("Almir", createdEmployee.getFirstName());
        assertEquals(department, createdEmployee.getDepartment());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        Employee existingEmployee = new Employee(1L, "Almir", "Shammasov", "Developer", 5000.0, null);
        Employee updatedEmployee = new Employee(1L, "Alexandr", "Naminof", "HR", 6000.0, null);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(updatedEmployee);

        assertEquals("Alexandr", result.getFirstName());
        assertEquals(6000.0, result.getSalary());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void testDeleteEmployee() {
        Employee employee = new Employee(1L, "Almir", "Shammasov", "Developer", 5000.0, null);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(1L);

        Employee deletedEmployee = employeeService.deleteEmployee(1L);

        assertEquals("Almir", deletedEmployee.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
