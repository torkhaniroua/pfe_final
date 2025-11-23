package com.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.model.Comment;
import com.application.model.Course;
import com.application.model.Professor;
import com.application.model.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCourse(Course course);

    List<Comment> findByParent(Comment parent);

    List<Comment> findByUser(User user);

    List<Comment> findByProfessor(Professor professor);
    
    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.course = :course AND c.rating IS NOT NULL")
    Double findAverageRatingByCourse(@Param("course") Course course);

    void deleteByUser(User user);

    void deleteByProfessor(Professor professor);
}
