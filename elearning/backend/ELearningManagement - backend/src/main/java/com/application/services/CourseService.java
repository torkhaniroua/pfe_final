package com.application.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.application.model.Course;
import com.application.model.Question;
import com.application.repository.CourseRepository;
import com.application.repository.CommentRepository;
import com.application.repository.EnrollmentRepository;
import com.application.repository.OptionRepository;
import com.application.repository.QuestionRepository;
import com.application.repository.WishlistRepository;

@Service
public class CourseService 
{
	@Autowired
	public CourseRepository courseRepo;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private EnrollmentRepository enrollmentRepository;

	@Autowired
	private WishlistRepository wishlistRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private OptionRepository optionRepository;
	@Transactional
	public void deleteCourseById(Long id) {
		Course course = courseRepo.findById(id).orElse(null);
		if (course == null) {
			return;
		}
		// Clean related data to avoid FK issues
		commentRepository.deleteByCourse(course);
		wishlistRepository.deleteByCourseid(course.getCourseid());
		enrollmentRepository.deleteByCourseid(course.getCourseid());

		List<Question> questions = questionRepository.findByCourseWithOptions(course);
		questions.forEach(q -> {
			if (q.getOptions() != null) {
				q.getOptions().forEach(opt -> optionRepository.delete(opt));
			}
			questionRepository.delete(q);
		});

		courseRepo.delete(course);
	}
	public Course saveCourse(Course course)
	{
		return courseRepo.save(course);
	}
	
	public Course addNewCourse(Course course)
	{
		return courseRepo.save(course);
	}
	
	public List<Course> getAllCourses()
	{
		return (List<Course>)courseRepo.findAll();
	}
	
	public void updateEnrolledcount(String coursename, int enrolledcount)
	{
		courseRepo.updateEnrolledcount(enrolledcount, coursename);
	}
	
	public Course fetchCourseByCoursename(String coursename)
	{
		return courseRepo.findByCoursename(coursename);
	}
	
	public Course fetchCourseByCourseid(String courseid)
	{
		return courseRepo.findByCourseid(courseid);
	}
	
	public List<Course> fetchByInstructorname(String instructorname)
	{
		return (List<Course>)courseRepo.findByInstructorname(instructorname);
	}
	
	public List<Course> fetchByInstructorinstitution(String instructorinstitution)
	{
		return (List<Course>)courseRepo.findByInstructorinstitution(instructorinstitution);
	}
	
	public List<Course> fetchByEnrolleddate(String enrolleddate)
	{
		return (List<Course>)courseRepo.findByEnrolleddate(enrolleddate);
	}
	
	public List<Course> fetchByCoursetype(String coursetype)
	{
		return (List<Course>)courseRepo.findByCoursetypeIgnoreCase(coursetype);
	}
	
	public List<Course> fetchByYoutubeurl(String youtubeurl)
	{
		return (List<Course>)courseRepo.findByYoutubeurl(youtubeurl);
	}
	
	public List<Course> fetchByWebsiteurl(String websiteurl)
	{
		return (List<Course>)courseRepo.findByWebsiteurl(websiteurl);
	}
	
	public List<Course> fetchBySkilllevel(String skilllevel)
	{
		return (List<Course>)courseRepo.findBySkilllevel(skilllevel);
	}
	
	public List<Course> fetchByLanguage(String language)
	{
		return (List<Course>)courseRepo.findByLanguage(language);
	}
	
}
