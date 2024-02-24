package com.nhnacademy.springmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentRestRequest;
import com.nhnacademy.springmvc.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

class StudentRestControllerTest {
    private MockMvc mockMvc;
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        StudentRestController controller = new StudentRestController(studentRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void viewStudent() throws Exception {
        // given
        Student expectedStudent = Student.create(1L);
        expectedStudent.setName("John");
        expectedStudent.setScore(100);
        expectedStudent.setEmail("john@do.com");
        expectedStudent.setComment("Good");
        Mockito.when(studentRepository.getStudent(Mockito.anyLong())).thenReturn(expectedStudent);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/students/1")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // then
        ObjectMapper objectMapper = new ObjectMapper();
        String contentAsString = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Student actualstudent = objectMapper.readValue(contentAsString, Student.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualstudent.getId()).isEqualTo(expectedStudent.getId());
            softly.assertThat(actualstudent.getName()).isEqualTo(expectedStudent.getName());
            softly.assertThat(actualstudent.getScore()).isEqualTo(expectedStudent.getScore());
            softly.assertThat(actualstudent.getEmail()).isEqualTo(expectedStudent.getEmail());
            softly.assertThat(actualstudent.getComment()).isEqualTo(expectedStudent.getComment());
        });
    }

    @Test
    void viewStudent_notFound() throws Exception {
        // given
        Mockito.when(studentRepository.getStudent(Mockito.anyLong())).thenReturn(null);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1")
                        .accept("application/json"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    void modifyStudent() throws Exception {
        // given
        Student originStudent = Student.create(1L);
        originStudent.setName("John");
        originStudent.setScore(100);
        originStudent.setEmail("john@do.com");
        originStudent.setComment("Good");
        Mockito.when(studentRepository.getStudent(Mockito.anyLong())).thenReturn(originStudent);

        StudentRestRequest request = new StudentRestRequest("Harry", "potter@hogwarts.uk", 0, "Bad");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                        .contentType("application/json")
                        .content(content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())   // is http code ok(200)
                .andReturn();

        // then
        // is http code ok(200)
        Assertions.assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void modifyStudent_notFound() throws Exception {
        // given
        Mockito.when(studentRepository.getStudent(Mockito.anyLong())).thenReturn(null);

        StudentRestRequest request = new StudentRestRequest("Harry", "potter@hogwarts.uk", 0, "Bad");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                        .contentType("application/json")
                        .content(content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())   // is http code ok(200)
                .andReturn();

        // then
        Mockito.verify(studentRepository, Mockito.never()).modify(Mockito.any(Student.class));
    }

    @Test
    void modifyStudent_validationFailed() throws Exception {
        // given
        StudentRestRequest request = new StudentRestRequest("", "", -100, "");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                        .contentType("application/json")
                        .content(content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())   // is http code ok(200)
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("ObjectName=studentRestRequest,Message=must be greater than or equal to 0,code=Min")))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("ObjectName=studentRestRequest,Message=must not be blank,code=NotBlank")))
                .andReturn();

        // then
        Mockito.verify(studentRepository, Mockito.never()).getStudent(Mockito.anyLong());
        Mockito.verify(studentRepository, Mockito.never()).modify(Mockito.any(Student.class));
    }

    @Test
    void registerStudent() throws Exception {
        // given
        StudentRestRequest request = new StudentRestRequest("Harry", "potter@hogwarts.uk", 0, "Bad");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);
        Mockito.when(studentRepository.register(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString())).thenReturn(Student.create(1L));

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType("application/json")
                        .content(content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/students/1"))
                .andReturn();

        // then
        Assertions.assertThat(mvcResult.getResponse().getHeader("Location")).isEqualTo("/students/1");
    }

    @Test
    void registerStudent_validationFailed() throws Exception {
        // given
        StudentRestRequest request = new StudentRestRequest("", "", -100, "");
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType("application/json")
                        .content(content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("ObjectName=studentRestRequest,Message=must be greater than or equal to 0,code=Min")))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("ObjectName=studentRestRequest,Message=must not be blank,code=NotBlank")))
                .andReturn();

        // then
        Mockito.verify(studentRepository, Mockito.never()).register(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    void viewStudent_xml() throws Exception {
        // given
        Student expectedStudent = Student.create(1L);
        expectedStudent.setName("John");
        expectedStudent.setScore(100);
        expectedStudent.setEmail("john@do.com");
        expectedStudent.setComment("Good");
        Mockito.when(studentRepository.getStudent(Mockito.anyLong())).thenReturn(expectedStudent);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1")
                        .param("format", "xml")
                        .accept("application/xml"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/xml;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("<Student>")))
                .andReturn();
    }
}