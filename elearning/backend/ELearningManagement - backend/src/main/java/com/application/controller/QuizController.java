package com.application.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.application.model.Course;
import com.application.model.Option;
import com.application.model.Question;
import com.application.model.DTO.OptionDTO;
import com.application.model.DTO.QuestionDTO;
import com.application.repository.CourseRepository;
import com.application.repository.OptionRepository;
import com.application.repository.QuestionRepository;
import com.application.services.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private OptionRepository optionRepo;

    @Autowired
    private CourseRepository courseRepo;

    private final QuestionService questionService;

    public QuizController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Ajouter des questions à un cours
    @PostMapping("/questions/add")
    public ResponseEntity<?> createQuestions(
            @RequestParam String courseId,
            @RequestBody List<QuestionDTO> questionDTOList
    ) {
        try {
            System.out.println("CourseId reçu: " + courseId);
            System.out.println("Questions reçues: " + questionDTOList);

            if (questionDTOList == null || questionDTOList.isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body("La liste des questions ne peut pas être vide");
            }

            // Chercher le cours par courseid (String) ou par id numérique
            System.out.println("Recherche du cours avec l'ID: " + courseId);
            Course course = courseRepo.findByCourseid(courseId);
            if (course == null) {
                try {
                    long idAsLong = Long.parseLong(courseId);
                    course = courseRepo.findById(idAsLong).orElse(null);
                } catch (NumberFormatException ignore) {
                    // courseId non numérique
                }
            }
            if (course == null) {
                System.out.println("Cours non trouvé pour l'ID: " + courseId);
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Course with ID '" + courseId + "' not found.");
            }

            List<Question> savedQuestions = new ArrayList<>();

            for (QuestionDTO dto : questionDTOList) {
                System.out.println("Traitement de la question: " + dto.getContent());
                Question question = Question.builder()
                        .content(dto.getContent())
                        .course(course)
                        .createdAt(LocalDateTime.now())
                        .build();

                System.out.println("Sauvegarde de la question");
                question = questionRepo.save(question);

                System.out.println("Traitement des options pour la question: " + question.getId());
                for (OptionDTO opt : dto.getOptions()) {
                    System.out.println("Option: " + opt.getContent() + ", Correcte: " + opt.isCorrect());
                    Option option = Option.builder()
                            .content(opt.getContent())
                            .correct(opt.isCorrect())
                            .question(question)
                            .build();
                    optionRepo.save(option);
                }

                savedQuestions.add(question);
            }

            System.out.println("Toutes les questions ont été sauvegardées avec succès");
            return ResponseEntity.ok(savedQuestions);
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la sauvegarde: " + e.getMessage());
        }
    }

    // Récupérer les questions d'un cours (courseid string ou id numérique)
    @GetMapping("/course/{courseId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByCourse(@PathVariable String courseId) {
        Course course = courseRepo.findByCourseid(courseId);
        if (course == null) {
            try {
                long idAsLong = Long.parseLong(courseId);
                course = courseRepo.findById(idAsLong).orElse(null);
            } catch (NumberFormatException ignore) {
                // not numeric
            }
        }
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Question> questions = questionRepo.findByCourseWithOptions(course);
        return ResponseEntity.ok(questions);
    }

    // Récupérer toutes les questions (optionnel)
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // Récupérer une question spécifique
    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }
}
