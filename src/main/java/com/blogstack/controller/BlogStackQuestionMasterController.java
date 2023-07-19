package com.blogstack.controller;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.service.IBlogStackQuestionMasterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/question")
@AllArgsConstructor
public class BlogStackQuestionMasterController {

    private final IBlogStackQuestionMasterService blogStackQuestionMasterService;

    @PostMapping("/")
    public ResponseEntity<?> addQuestion(@Valid @RequestBody QuestionMasterRequestBean questionMasterRequestBean){
        return ResponseEntity.ok(this.blogStackQuestionMasterService.addQuestion(questionMasterRequestBean));
    }

    @GetMapping("/")
    public ResponseEntity<?> fetchAllQuestion(@RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity.ok(this.blogStackQuestionMasterService.fetchAllQuestion(page, size));
    }

    @GetMapping("/{question_id}")
    public ResponseEntity<?> fetchQuestionById(@PathVariable(value = "question_id") @NotBlank(message = "Question Id can not be empty.") String questionId){
        return ResponseEntity.ok(this.blogStackQuestionMasterService.fetchQuestionById(questionId));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateQuestion(@Valid @RequestBody QuestionMasterRequestBean questionMasterRequestBean){
        return ResponseEntity.ok(this.blogStackQuestionMasterService.updateQuestion(questionMasterRequestBean));
    }

    @DeleteMapping("/{question_id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable(value = "question_id") @NotBlank(message = "Question Id can not be empty.") String questionId){
        return ResponseEntity.ok(this.blogStackQuestionMasterService.deleteQuestion(questionId));
    }
}