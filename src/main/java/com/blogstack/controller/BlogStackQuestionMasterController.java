package com.blogstack.controller;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.service.IBlogStackQuestionMasterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping("/question")
public class BlogStackQuestionMasterController {

    @Autowired
    private IBlogStackQuestionMasterService blogStackQuestionMasterService;

    @PostMapping("/")
    public Mono<?> addQuestion(@Valid @RequestBody QuestionMasterRequestBean questionMasterRequestBean){
        return this.blogStackQuestionMasterService.addQuestion(questionMasterRequestBean);
    }

    @GetMapping("/")
    public Mono<?> fetchAllQuestion(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                    @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "2147483647") Integer size){
        return this.blogStackQuestionMasterService.fetchAllQuestion(filterCriteria, sortCriteria, page, size);
    }

    @GetMapping("/{question_id}")
    public Mono<?> fetchQuestionById(@PathVariable(value = "question_id") @NotBlank(message = "Question Id can not be empty.") String questionId){
        return this.blogStackQuestionMasterService.fetchQuestionById(questionId);
    }

    @PutMapping("/")
    public Mono<?> updateQuestion(@Valid @RequestBody QuestionMasterRequestBean questionMasterRequestBean){
        return this.blogStackQuestionMasterService.updateQuestion(questionMasterRequestBean);
    }

    @DeleteMapping("/{question_id}")
    public Mono<?> deleteQuestion(@PathVariable(value = "question_id") @NotBlank(message = "Question Id can not be empty.") String questionId){
        return this.blogStackQuestionMasterService.deleteQuestion(questionId);
    }
}