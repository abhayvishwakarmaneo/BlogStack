package com.blogstack.entity.pojo.mapper;

import com.blogstack.beans.responses.AnswerMasterResponseBean;
import com.blogstack.entities.BlogStackAnswerMaster;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IBlogStackAnswerMasterEntityPojoMapper {
    public static Function<BlogStackAnswerMaster, AnswerMasterResponseBean> mapAnswerMasterEntityPojoMapping = blogStackAnswerMaster -> AnswerMasterResponseBean.builder()
            .answerId(blogStackAnswerMaster.getBsamAnswerId())
            .answer(blogStackAnswerMaster.getBsamAnswer())
            .status(blogStackAnswerMaster.getBsamStatus())
            .addedOn(blogStackAnswerMaster.getBsamCreatedDate())
            .build();

    public static Function<List<BlogStackAnswerMaster>, List<AnswerMasterResponseBean>> mapAnswerMasterEntityListToPojoListMapping = blogStackAnswerMasterList -> blogStackAnswerMasterList.stream()
            .map(blogStackAnswerMaster -> {
                AnswerMasterResponseBean.AnswerMasterResponseBeanBuilder answerMasterResponseBeanBuilder = AnswerMasterResponseBean.builder();
                answerMasterResponseBeanBuilder.answerId(blogStackAnswerMaster.getBsamAnswerId())
                        .answer(blogStackAnswerMaster.getBsamAnswer())
                        .status(blogStackAnswerMaster.getBsamStatus())
                        .addedOn(blogStackAnswerMaster.getBsamCreatedDate());
                return answerMasterResponseBeanBuilder.build();
            }).collect(Collectors.toList());
}