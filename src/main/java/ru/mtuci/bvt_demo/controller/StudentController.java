package ru.mtuci.bvt_demo.controller;

import org.springframework.web.bind.annotation.*;
import ru.mtuci.bvt_demo.TestService;
import ru.mtuci.bvt_demo.entities.Course;
import ru.mtuci.bvt_demo.entities.Student;
import ru.mtuci.bvt_demo.repository.CoursesRepository;
import ru.mtuci.bvt_demo.repository.StudentRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final StudentRepository studentRepository;
    private final CoursesRepository coursesRepository;
    private final TestService studentService;

    public StudentController(StudentRepository studentRepository,
                             CoursesRepository coursesRepository, TestService studentService) {
        this.studentRepository = studentRepository;
        this.coursesRepository = coursesRepository;
        this.studentService = studentService;
    }

    @PostMapping("/courses")
    public Course createCourse(@RequestBody Course course) {
        return coursesRepository.save(course);
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return coursesRepository.findAll();
    }

    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        var savedStudent = studentService.saveStudent(student);
        return studentRepository.findById(savedStudent.getId()).get();
    }

    @GetMapping("/Student")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
