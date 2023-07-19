package com.blogstack.controller;

import com.blogstack.beans.requests.AnswerMasterRequestBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.service.IBlogStackAnswerMasterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/answer")
@AllArgsConstructor
public class BlogStackAnswerMasterController {
    
    private final IBlogStackAnswerMasterService blogStackAnswerMasterService;

    @PostMapping("/question/{questionId}")
    public ResponseEntity<?> addAnswer(@PathVariable("questionId") @NotBlank(message = "Question Id can not be blank") String questionId, @Valid @RequestBody AnswerMasterRequestBean answerMasterRequestBean){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.addAnswer(questionId, answerMasterRequestBean));
    }

    @GetMapping("/")
    public ResponseEntity<?> fetchAllAnswer(@RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.fetchAllAnswer(page, size));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> fetchAllAnswerByQuestionId(@PathVariable("questionId") String questionId){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.fetchAllAnswerByQuestionId(questionId));
    }

    @GetMapping("/{answer_id}")
    public ResponseEntity<?> fetchAnswerById(@PathVariable(value = "answer_id") @NotBlank(message = "Answer Id can not be empty.") String answerId){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.fetchAnswerById(answerId));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateAnswer(@Valid @RequestBody AnswerMasterRequestBean answerMasterRequestBean){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.updateAnswer( answerMasterRequestBean));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable(value = "answerId") @NotBlank(message = "Answer Id can not be empty.") String answerId){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.deleteAnswer(answerId));
    }

    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<?> deleteAllAnswer(@PathVariable(value = "questionId") @NotBlank(message = "Answer Id can not be empty.") String questionId){
        return ResponseEntity.ok(this.blogStackAnswerMasterService.deleteAllAnswer(questionId));
    }
}