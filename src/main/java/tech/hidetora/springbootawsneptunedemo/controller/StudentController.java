package tech.hidetora.springbootawsneptunedemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.hidetora.springbootawsneptunedemo.entity.Student;
import tech.hidetora.springbootawsneptunedemo.service.StudentService;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<String> saveStudent(@RequestBody Student student) {
        String serviceStudent = studentService.createStudent(student);
        return ResponseEntity.ok(serviceStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getStudent(@PathVariable String id) throws JsonProcessingException {
        String s = studentService.readStudent(id);
        String value = objectMapper.writeValueAsString(s);
        return ResponseEntity.ok(value);
    }
}
