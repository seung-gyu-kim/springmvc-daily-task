package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentRestRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/students")
public class StudentRestController {
    private final StudentRepository studentRepository;

    public StudentRestController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> viewStudent(@PathVariable("studentId") long studentId) {
        Student student = studentRepository.getStudent(studentId);
        if(Objects.isNull(student)) {
            throw new StudentNotFoundException();
        }

        return ResponseEntity.ok(student);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> modifyStudent(@PathVariable("studentId") long studentId,
                                                 @Valid @RequestBody StudentRestRequest studentRequest,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        Student student = studentRepository.getStudent(studentId);
        if(Objects.isNull(student)) {
            throw new StudentNotFoundException();
        }

        student.setName(studentRequest.getName());
        student.setEmail(studentRequest.getEmail());
        student.setScore(studentRequest.getScore());
        student.setComment(studentRequest.getComment());
        studentRepository.modify(student);

        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody StudentRestRequest studentRequest,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        Student student = studentRepository.register(studentRequest.getName(), studentRequest.getEmail(), studentRequest.getScore(), studentRequest.getComment());
        return ResponseEntity.created(URI.create("/students/" + student.getId())).body(student);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<StudentNotFoundException> notFound(StudentNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ValidationFailedException> validationFailed(ValidationFailedException ex) {
        return ResponseEntity.badRequest().body(ex);
    }
}
