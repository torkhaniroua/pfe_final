package com.application.controller;

import java.util.List;
import java.util.Optional;

import com.application.repository.CourseRepository;
import com.application.repository.ProfessorRepository;
import com.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.application.model.Course;
import com.application.model.Professor;
import com.application.model.User;
import com.application.services.CourseService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    // Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course, Authentication authentication) {
        if ((course.getInstructorEmail() == null || course.getInstructorEmail().isBlank()) && authentication != null) {
            course.setInstructorEmail(authentication.getName());
        }
        Course savedCourse = courseService.saveCourse(course);
        return ResponseEntity.ok(savedCourse);
    }

    // Get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // Get course by id
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        Optional<Course> courseOpt = courseRepo.findById((long) id);
        return courseOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().<Course>build());
    }

    // Update a course (full update)
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails, Authentication authentication) {
        Optional<Course> existingOpt = courseRepo.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().<Course>build();
        }

        Course existingCourse = existingOpt.get();
        existingCourse.setCoursename(courseDetails.getCoursename());
        existingCourse.setCoursetype(courseDetails.getCoursetype());
        existingCourse.setSkilllevel(courseDetails.getSkilllevel());
        existingCourse.setLanguage(courseDetails.getLanguage());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setIsPremium(courseDetails.getIsPremium());

        if (courseDetails.getInstructorEmail() != null && !courseDetails.getInstructorEmail().isBlank()) {
            existingCourse.setInstructorEmail(courseDetails.getInstructorEmail());
        } else if (existingCourse.getInstructorEmail() == null && authentication != null) {
            existingCourse.setInstructorEmail(authentication.getName());
        }

        Course updated = courseRepo.save(existingCourse);
        return ResponseEntity.ok(updated);
    }


    // Delete a course by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build();
        }

        Optional<Course> courseOpt = courseRepo.findById(id);
        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Course course = courseOpt.get();
        String requesterEmail = authentication.getName();
        if (!canDeleteCourse(course, requesterEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
        }

        courseService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }


    // Recherche par coursename (exemple)
    @GetMapping("/search/byName")
    public ResponseEntity<Course> getCourseByCoursename(@RequestParam String coursename) {
        Course course = courseService.fetchCourseByCoursename(coursename);
        if (course == null) {
            return ResponseEntity.notFound().<Course>build();
        }
        return ResponseEntity.ok(course);
    }

    // Other search methods can be added here

    private boolean canDeleteCourse(Course course, String requesterEmail) {
        if (requesterEmail == null || course == null) {
            return false;
        }
        if (isAdmin(requesterEmail)) {
            return true;
        }
        if (course.getInstructorEmail() != null && course.getInstructorEmail().equalsIgnoreCase(requesterEmail)) {
            return true;
        }
        if (course.getInstructorname() != null && course.getInstructorname().equalsIgnoreCase(requesterEmail)) {
            return true;
        }

        Professor professor = professorRepository.findByEmailIgnoreCase(requesterEmail);
        return professor != null
                && course.getInstructorname() != null
                && course.getInstructorname().equalsIgnoreCase(professor.getProfessorname());
    }

    private boolean isAdmin(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }
}
