package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.model.Course;
import com.application.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCourse(Course course);

    @Query("select distinct q from Question q left join fetch q.options where q.course = :course")
    List<Question> findByCourseWithOptions(@Param("course") Course course);
}
