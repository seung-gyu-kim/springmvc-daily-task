package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @ModelAttribute("student")
    public Student getUser(@PathVariable("studentId") long studentId) {
        Student student = studentRepository.getStudent(studentId);
        if(Objects.isNull(student)) {
            throw new StudentNotFoundException();
        }
        return student;
    }

    @GetMapping("/{studentId}")
    public ModelAndView viewStudent(@ModelAttribute("student") Student student) {
        ModelAndView mav = new ModelAndView("thymeleaf/studentView");
        mav.addObject(student);
        return mav;
    }

    @GetMapping(path="/{studentId}", params="hideScore=yes")
    public String viewStudentHideScore(@ModelAttribute("student") Student student, Model model) {
//        model.addAttribute(student);      // @ModelAttribute 모델에 자동 주입
        return "thymeleaf/studentViewHideScore";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm() {
        return "thymeleaf/studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyStudent(@Valid @ModelAttribute StudentModifyRequest studentRequest,
                                @ModelAttribute("student") Student student,
                                ModelMap model,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        student.setName(studentRequest.getName());
        student.setEmail(studentRequest.getEmail());
        student.setScore(studentRequest.getScore());
        student.setComment(studentRequest.getComment());
        studentRepository.modify(student);

        model.addAttribute("student", student);

        return "thymeleaf/studentView";
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFound(StudentNotFoundException ex, Model model) {
        model.addAttribute("exception", ex);
        return "thymeleaf/error";
    }
}
