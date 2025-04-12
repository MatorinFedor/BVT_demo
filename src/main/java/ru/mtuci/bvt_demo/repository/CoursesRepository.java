package ru.mtuci.bvt_demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.bvt_demo.entities.Course;

@Repository
public interface CoursesRepository extends JpaRepository<Course, Long> {
}
