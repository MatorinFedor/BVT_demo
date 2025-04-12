package ru.mtuci.bvt_demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.bvt_demo.entities.Department;
import ru.mtuci.bvt_demo.entities.Employee;
import ru.mtuci.bvt_demo.repository.DepartmentRepository;


@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            for (Employee employee : department.getEmployees()) {
                employee.setDepartment(department);
            }
        }
        return departmentRepository.save(department);
    }
}
