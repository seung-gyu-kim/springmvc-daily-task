package com.nhnacademy.springmvc.repository.impl;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.repository.StudentRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class StudentRepositoryImpl implements StudentRepository {
    private final Map<Long, Student> students = new HashMap<>();

    @Override
    public boolean exists(long id) {
        return students.containsKey(id);
    }

    @Override
    public Student register(String name, String email, int score, String comment) {
        long id = students.keySet()
                .stream()
                .max(Comparator.comparing(Function.identity()))
                .map(l -> l + 1)
                .orElse(1L);

        Student student = Student.create(id);
        student.setName(name);
        student.setEmail(email);
        student.setScore(score);
        student.setComment(comment);

        students.put(id, student);

        return student;
    }

    @Override
    public Student getStudent(long id) {
        return exists(id) ? students.get(id) : null;
    }

    @Override
    public void modify(Student student) {
        Student dbStudent = getStudent(student.getId());
        if(Objects.isNull(dbStudent)){
            throw new StudentNotFoundException();
        }

        dbStudent.setName(student.getName());
        dbStudent.setEmail(student.getEmail());
        dbStudent.setScore(student.getScore());
        dbStudent.setComment(student.getComment());
    }
}
