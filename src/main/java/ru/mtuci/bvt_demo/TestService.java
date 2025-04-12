package ru.mtuci.bvt_demo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.mtuci.bvt_demo.entities.Student;
import ru.mtuci.bvt_demo.repository.StudentRepository;

@Service
public class TestService {
    private final StudentRepository studentRepository;

    public TestService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }
}
