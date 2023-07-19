package com.blogstack.service;

import com.blogstack.beans.requests.AnswerMasterRequestBean;
import com.blogstack.beans.responses.ServiceResponseBean;


import java.util.Optional;

public interface IBlogStackAnswerMasterService {
    Optional<ServiceResponseBean> addAnswer(String questionId, AnswerMasterRequestBean answerMasterRequestBean);

    Optional<ServiceResponseBean> fetchAllAnswer(Integer page, Integer size);

    Optional<ServiceResponseBean> fetchAllAnswerByQuestionId(String questionId);

    Optional<ServiceResponseBean> fetchAnswerById(String answerId);

    Optional<ServiceResponseBean> updateAnswer(AnswerMasterRequestBean answerMasterRequestBean);

    Optional<ServiceResponseBean> deleteAnswer(String answerId);

    Optional<ServiceResponseBean> deleteAllAnswer(String questionId);
}