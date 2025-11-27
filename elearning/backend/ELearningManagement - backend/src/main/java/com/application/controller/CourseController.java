package com.application.controller;

import java.util.List;

import com.application.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.model.Course;
import com.application.services.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepo;

    // Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
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
        // Ici, il faudrait une méthode findById dans service/repository
        // Ajoutons un exemple simple:
        return courseService.getAllCourses()
                .stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a course (full update)
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseRepo.findById(id)
                .map(existingCourse -> {
                    existingCourse.setCoursename(courseDetails.getCoursename());
                    existingCourse.setCoursetype(courseDetails.getCoursetype());
                    existingCourse.setSkilllevel(courseDetails.getSkilllevel());
                    existingCourse.setLanguage(courseDetails.getLanguage());
                    existingCourse.setDescription(courseDetails.getDescription());
                    // ✅ corriger ici (orthographe + bon getter/setter)
                    existingCourse.setIsPremium(courseDetails.getIsPremium());

                    Course updated = courseRepo.save(existingCourse);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // Delete a course by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        List<Course> courses = courseService.getAllCourses();

        boolean exists = courses.stream().anyMatch(c -> c.getId() == id);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        // ✅ suppression directe via le service
        courseService.deleteCourseById(id);

        return ResponseEntity.noContent().build();
    }


    // Recherche par coursename (exemple)
    @GetMapping("/search/byName")
    public ResponseEntity<Course> getCourseByCoursename(@RequestParam String coursename) {
        Course course = courseService.fetchCourseByCoursename(coursename);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    // Autres méthodes de recherche similaires peuvent être ajoutées ici

}
