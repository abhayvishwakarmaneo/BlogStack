package com.blogstack.service;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IBlogStackQuestionMasterService {

    Optional<ServiceResponseBean> addQuestion(QuestionMasterRequestBean questionMasterRequestBean);

    Optional<ServiceResponseBean> fetchAllQuestion(Integer page, Integer size);

    Optional<ServiceResponseBean> fetchQuestionById(String questionId);

    Optional<ServiceResponseBean> updateQuestion(QuestionMasterRequestBean questionMasterRequestBean);

    Optional<ServiceResponseBean> deleteQuestion(String questionId);
}