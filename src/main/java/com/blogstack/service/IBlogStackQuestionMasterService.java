package com.blogstack.service;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import reactor.core.publisher.Mono;

public interface IBlogStackQuestionMasterService {

    Mono<?> addQuestion(QuestionMasterRequestBean questionMasterRequestBean);

    Mono<?> fetchAllQuestion(String filterCriteria, String sortCriteria, Integer page, Integer size, String... args);

    Mono<?> fetchQuestionById(String questionId);

    Mono<?> updateQuestion(QuestionMasterRequestBean questionMasterRequestBean);

    Mono<?> deleteQuestion(String questionId);
}