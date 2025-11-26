package com.application.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.model.Chapter;
import com.application.model.Course;
import com.application.model.Professor;
import com.application.model.User;
import com.application.model.Wishlist;
import com.application.services.ChapterService;
import com.application.services.CourseService;
import com.application.services.ProfessorService;
import com.application.services.WishlistService;

@RestController
public class ProfessorController 
{	
	@Value("${app.upload.dir:uploads}")
	private String uploadDir;
	@Autowired
	private ProfessorService professorService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private WishlistService wishlistService;
	
	@GetMapping("/professorlist")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Professor>> getProfessorList() throws Exception
	{
		List<Professor> professors = professorService.getAllProfessors();
		return new ResponseEntity<List<Professor>>(professors, HttpStatus.OK);
	}
	
	@GetMapping("/youtubecourselist")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Course>> getYoutubeCourseList() throws Exception
	{
		List<Course> youtubeCourseList = courseService.fetchByCoursetype("Youtube");
//		for(Course list:youtubeCourseList)
//		{
//			System.out.println(list.getYoutubeurl());
//		}
		return new ResponseEntity<List<Course>>(youtubeCourseList, HttpStatus.OK);
	}
	
	@GetMapping("/websitecourselist")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Course>> getWebsiteCourseList() throws Exception
	{
		List<Course> websiteCourseList = courseService.fetchByCoursetype("Website");
		return new ResponseEntity<List<Course>>(websiteCourseList, HttpStatus.OK);
	}
	
	@GetMapping("/courselistbyname/{coursename}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Course>> getCourseListByName(@PathVariable String coursename) throws Exception
	{
		Course courseList = courseService.fetchCourseByCoursename(coursename);
		System.out.println(courseList.getCoursename()+" ");
		List<Course> courselist = new ArrayList<>();
		courselist.add(courseList);
		return new ResponseEntity<List<Course>>(courselist, HttpStatus.OK);
	}

	@GetMapping("/listcourses")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Course>> getAllCourses() throws Exception
	{
		List<Course> courselist = courseService.getAllCourses();
		return new ResponseEntity<List<Course>>(courselist, HttpStatus.OK);
	}
	
	@GetMapping("/professorlistbyemail/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Professor>> getProfessorListByEmail(@PathVariable String email) throws Exception
	{
		List<Professor> professors = professorService.getProfessorsByEmail(email);
		return new ResponseEntity<List<Professor>>(professors, HttpStatus.OK);
	}

	@PostMapping("/addProfessor")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> registerProfessor(@RequestBody Professor professor) throws Exception {
		try {
			// ✅ Générer un ID unique
			professor.setProfessorid(getNewID());

			// ✅ Enregistrer avec mot de passe encodé
			Professor savedProfessor = professorService.saveProfessor(professor);

			// ✅ Mettre le status à "active"
			professorService.updateStatus(professor.getEmail());

			return ResponseEntity.ok(savedProfessor);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Erreur lors de l'ajout du professeur : " + e.getMessage());
		}
	}


	@PostMapping("/addCourse")
	@CrossOrigin(origins = "http://localhost:4200")
	public Course addNewCourse(@RequestBody Course course, Authentication authentication) throws Exception
	{
		Course courseObj = null;
		String newID = getNewID();
		// auto generate courseid if missing/blank
		if (course.getCourseid() == null || course.getCourseid().isBlank()) {
			course.setCourseid(newID);
		}
		if ((course.getInstructorEmail() == null || course.getInstructorEmail().isBlank()) && authentication != null) {
			course.setInstructorEmail(authentication.getName());
		}
		
		courseObj = courseService.addNewCourse(course);
		return courseObj;
	}
	
	@PostMapping("/addnewchapter")
	@CrossOrigin(origins = "http://localhost:4200")
	public Chapter addNewChapters(@RequestBody Chapter chapter) throws Exception
	{
		Chapter chapterObj = null;
		chapterObj = chapterService.addNewChapter(chapter);
		return chapterObj;
	}
	
	@GetMapping("/acceptstatus/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<String>> updateStatus(@PathVariable String email) throws Exception
	{
		professorService.updateStatus(email);
		List<String> al=new ArrayList<>();
		al.add("accepted");
		return new ResponseEntity<List<String>>(al,HttpStatus.OK);
	}
	
	@GetMapping("/rejectstatus/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<String>> rejectStatus(@PathVariable String email) throws Exception
	{
		professorService.rejectStatus(email);
		List<String> al=new ArrayList<>();
		al.add("rejected");
		return new ResponseEntity<List<String>>(al,HttpStatus.OK);
	}
	
	@GetMapping("/professorprofileDetails/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Professor>> getProfileDetails(@PathVariable String email) throws Exception
	{
		List<Professor> professors = professorService.fetchProfileByEmail(email);
		return new ResponseEntity<List<Professor>>(professors, HttpStatus.OK);
	}
	
	@PutMapping("/updateprofessor")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<Professor> updateProfessorProfile(@RequestBody Professor professor) throws Exception
	{
		Professor professorobj = professorService.updateProfessorProfile(professor);
		return new ResponseEntity<Professor>(professorobj, HttpStatus.OK);
	}
	
	@GetMapping("/gettotalprofessors")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Integer>> getTotalProfessors() throws Exception
	{
		List<Professor> professors = professorService.getAllProfessors();
		List<Integer> professorsCount = new ArrayList<>();
		professorsCount.add(professors.size());
		return new ResponseEntity<List<Integer>>(professorsCount, HttpStatus.OK);
	}
	
	@GetMapping("/gettotalchapters")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Integer>> getTotalChapters() throws Exception
	{
		List<Chapter> chapters = chapterService.getAllChapters();
		List<Integer> chaptersCount = new ArrayList<>();
		chaptersCount.add(chapters.size());
		return new ResponseEntity<List<Integer>>(chaptersCount, HttpStatus.OK);
	}
	
	@GetMapping("/gettotalcourses")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Integer>> getTotalCourses() throws Exception
	{
		List<Course> courses = courseService.getAllCourses();
		List<Integer> coursesCount = new ArrayList<>();
		coursesCount.add(courses.size());
		return new ResponseEntity<List<Integer>>(coursesCount, HttpStatus.OK);
	}
	
	@GetMapping("/gettotalwishlist")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Integer>> getTotalWishlist() throws Exception
	{
		List<Wishlist> wishlists = wishlistService.getAllLikedCourses();
		List<Integer> wishlistCount = new ArrayList<>();
		wishlistCount.add(wishlists.size());
		return new ResponseEntity<List<Integer>>(wishlistCount, HttpStatus.OK);
	}
  
  @GetMapping("/getcoursenames")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<String>> getCourseNames() throws Exception
	{
		List<Course> courses = courseService.getAllCourses();
		List<String> coursenames = new ArrayList<>();
		for(Course obj : courses)
		{
			coursenames.add(obj.getCoursename());
		}
		return new ResponseEntity<List<String>>(coursenames, HttpStatus.OK);
	}
	
	public String getNewID()
	{
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) 
        {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
	}

	@DeleteMapping("professor/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
    	boolean deleted = professorService.deleteUserByEmail(id);
		if (!deleted) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Professor not found or could not be deleted");
		}
		return ResponseEntity.ok().body("Professor deleted");
    }


	@GetMapping("/professors")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Professor>> getAllProfessors() {
		List<Professor> professors = professorService.getAllProfessors();
		return new ResponseEntity<>(professors, HttpStatus.OK);
	}

	@PutMapping("/professors/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<Professor> updateProfessor(@PathVariable String email, @RequestBody Professor updatedProfessor) {
		Professor professor = professorService.fetchProfessorByEmail(email);
		if (professor == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		professor.setProfessorname(updatedProfessor.getProfessorname());
		professor.setProfessorid(updatedProfessor.getProfessorid());
		professor.setDegreecompleted(updatedProfessor.getDegreecompleted());
		professor.setInstitutionname(updatedProfessor.getInstitutionname());
		professor.setDepartment(updatedProfessor.getDepartment());
		professor.setExperience(updatedProfessor.getExperience());
		professor.setMobile(updatedProfessor.getMobile());
		professor.setGender(updatedProfessor.getGender());
		professor.setPassword(updatedProfessor.getPassword());
		professor.setStatus(updatedProfessor.getStatus());

		Professor saved = professorService.updateProfessorProfile(professor);
		return new ResponseEntity<>(saved, HttpStatus.OK);
	}
	@DeleteMapping("/professors/{email}")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<String> deleteProfessor(@PathVariable String email) {
		boolean deleted = professorService.deleteUserByEmail(email);
		if (!deleted) {
			return new ResponseEntity<>("Professor not found or could not be deleted", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Professor deleted successfully", HttpStatus.OK);
	}

	@PostMapping("/professors/{email}/avatar")
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<?> uploadProfessorAvatar(@PathVariable String email, @RequestParam("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is empty");
		}
		Professor professor = professorService.fetchProfessorByEmail(email);
		if (professor == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		String originalName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
		String extension = "";
		int dotIndex = originalName.lastIndexOf('.');
		if (dotIndex >= 0) {
			extension = originalName.substring(dotIndex);
		}
		String filename = UUID.randomUUID().toString() + extension;
		Path avatarDir = Paths.get(uploadDir, "professors").toAbsolutePath().normalize();
		Files.createDirectories(avatarDir);
		Path target = avatarDir.resolve(filename);
		Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

		String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/uploads/professors/")
				.path(filename)
				.toUriString();

		professorService.updateProfessorAvatar(email, fileUrl);
		return ResponseEntity.ok(java.util.Map.of("avatarUrl", fileUrl));
	}


}
