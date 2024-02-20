package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @ModelAttribute("student")
    public Student getUser(@PathVariable("studentId") long studentId) {
        return studentRepository.getStudent(studentId);
    }

    @GetMapping("/{studentId}")
    public ModelAndView viewStudent(@ModelAttribute("student") Student student) {
        ModelAndView mav = new ModelAndView("studentView");
        mav.addObject(student);
        return mav;
    }

    @GetMapping(path="/{studentId}", params="hideScore=yes")
    public ModelAndView viewStudentHideScore(@ModelAttribute("student") Student student) {
        ModelAndView mav = new ModelAndView("studentViewHideScore");
        mav.addObject(student);
        return mav;
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm() {
        return "studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyStudent() {
        return "studentView";
    }

}
