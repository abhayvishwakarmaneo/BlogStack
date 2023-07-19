package com.blogstack.service;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.entities.BlogStackQuestionMaster;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IBlogStackQuestionMasterService {

    Optional<?> addQuestion(QuestionMasterRequestBean questionMasterRequestBean);

    Optional<?> fetchAllQuestion(Integer page, Integer size);

    Optional<?> fetchQuestionById(String questionId);

    Optional<?> updateQuestion(QuestionMasterRequestBean questionMasterRequestBean);

    Optional<?> deleteQuestion(String questionId);
}