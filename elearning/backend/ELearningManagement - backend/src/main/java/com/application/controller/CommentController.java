package com.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.application.model.Comment;
import com.application.model.Course;
import com.application.model.Professor;
import com.application.model.User;
import com.application.repository.CommentRepository;
import com.application.repository.CourseRepository;
import com.application.repository.ProfessorRepository;
import com.application.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course resolveCourse(String courseId) {
        if (courseId == null || courseId.isBlank()) {
            throw new RuntimeException("CourseId is required");
        }
        // Try courseid
        Course byId = courseRepository.findByCourseid(courseId);
        if (byId != null) {
            return byId;
        }
        Course first = courseRepository.findFirstByCourseid(courseId);
        if (first != null) {
            return first;
        }
        // Fallback: try coursename if courseId was actually a name
        Course byName = courseRepository.findByCoursename(courseId);
        if (byName != null) {
            return byName;
        }
        throw new RuntimeException("Course not found");
    }

    private Professor resolveProfessor(String email) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Professor email is required");
        }
        // Prefer case-insensitive single match
        try {
            Professor professor = professorRepository.findByEmailIgnoreCase(email);
            if (professor != null) {
                return professor;
            }
        } catch (Exception ignored) {
            // fallback below
        }
        // Fallback: first element from list to avoid NonUniqueResultException
        List<Professor> list = professorRepository.findProfessorListByEmail(email);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        throw new RuntimeException("Professor not found");
    }

    @PostMapping
    public Comment createComment(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String professorId,
            @RequestParam String courseId,
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Integer rating,
            @RequestBody String content
    ) {
        if ((userId == null && professorId == null) || (userId != null && professorId != null)) {
            throw new RuntimeException("Exactly one of userId or professorId must be provided.");
        }

        Course course = resolveCourse(courseId);

        Comment comment = new Comment();
        comment.setCourse(course);
        comment.setContent(content);
        comment.setCreatedAt(java.time.LocalDateTime.now());
        comment.setRating(rating);

        if (userId != null) {
            User user = userRepository.findByEmail(userId);
            if (user == null)
             new RuntimeException("User not found");
            comment.setUser(user);
        } else {
            comment.setProfessor(resolveProfessor(professorId));
        }

        if (parentId != null) {
            Optional<Comment> parent = commentRepository.findById(parentId);
            if(parent == null){
                new RuntimeException("Parent comment not found");
                
            }
            
            comment.setParent(parent.get());
        }

        return commentRepository.save(comment);
    }

    @GetMapping("/course/{courseId}")
    public List<Comment> getCommentsByCourse(@PathVariable String courseId) {
        Course course = resolveCourse(courseId);
        return commentRepository.findByCourse(course);
    }

    @GetMapping("/replies/{parentId}")
    public List<Comment> getReplies(@PathVariable Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        return commentRepository.findByParent(parent);
    }

    @GetMapping("/user/{userId}")
    public List<Comment> getCommentsByUser(@PathVariable String userId) {
        User user = userRepository.findByEmail(userId);
        if(user == null)
            new RuntimeException("User not found");
        
        return commentRepository.findByUser(user);
    }

    @GetMapping("/professor/{professorId}")
    public List<Comment> getCommentsByProfessor(@PathVariable String professorId) {
        Professor professor = professorRepository.findByEmail(professorId);
        if( professor == null)
             new RuntimeException("Professor not found");

        return commentRepository.findByProfessor(professor);
    }

    @GetMapping("/{commentId}")
    public Comment getCommentById(@PathVariable Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    @GetMapping("/course/{courseId}/rating")
    public Double getAverageCourseRating(@PathVariable String courseId) {
        Course course = courseRepository.findByCourseid(courseId);
        if (course == null)
            new RuntimeException("Course not found");

        Double avg = commentRepository.findAverageRatingByCourse(course);
        return avg != null ? avg : 0.0;
    }
}
