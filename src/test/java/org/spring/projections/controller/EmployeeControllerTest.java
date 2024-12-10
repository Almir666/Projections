package org.spring.projections.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.projections.dto.EmployeeRequest;
import org.spring.projections.model.Department;
import org.spring.projections.model.Employee;
import org.spring.projections.projection.EmployeeProjection;
import org.spring.projections.service.EmployeeService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void getAllEmployees() throws Exception {
        List<Employee> mockEmployees = List.of(
                new Employee(1L, "Almir", "Shammasov", "Developer", 5000.0, null),
                new Employee(2L, "Alexandr", "Shirkov", "Designer", 4000.0, null)
        );
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Almir"));
    }

    @Test
    void testGetProjections() throws Exception {
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

                when(employeeService.getProjections(1L)).thenReturn(mockProjections);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Almir Shammasov"));

        verify(employeeService, times(1)).getProjections(1L);
    }

    @Test
    void testCreateEmployee() throws Exception {
        String employee = """
                           {
                           "firstName": "Almir",
                           "lastName": "Shammasov",
                           "position": "Developer",
                           "salary": 5000.0,
                           "departmentId": 1
                           }
                            """;
        Department department = new Department(1L, "IT");
        Employee mockEmployee = new Employee(1L, "Almir", "Shammasov", "Developer", 5000.0, department);

        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(mockEmployee);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Almir"))
                .andExpect(jsonPath("$.position").value("Developer"));
    }



    @Test
    void testUpdateEmployee() throws Exception {
        Employee mockEmployee = new Employee(1L, "Almir", "Shammasov", "Developer", 6000.0, null);
        String employee = """
                            {
                              "id": 1,
                              "firstName": "Almir",
                              "lastName": "Shammasov",
                              "position": "Developer",
                              "salary": 6000.0
                            }
                            """;

        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(mockEmployee);

        mockMvc.perform(put("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(6000.0));

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Employee mockEmployee = new Employee(1L, "Almir", "Shammasov", "Developer", 5000.0, null);

        when(employeeService.deleteEmployee(1L)).thenReturn(mockEmployee);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Almir"));

        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
