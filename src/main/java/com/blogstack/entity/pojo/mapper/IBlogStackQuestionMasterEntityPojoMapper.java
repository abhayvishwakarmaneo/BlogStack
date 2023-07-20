package com.blogstack.entity.pojo.mapper;

import com.blogstack.beans.responses.QuestionMasterResponseBean;
import com.blogstack.entities.BlogStackQuestionMaster;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IBlogStackQuestionMasterEntityPojoMapper {
    public static Function<BlogStackQuestionMaster, QuestionMasterResponseBean> mapQuestionMasterEntityPojoMapping = blogStackQuestionMaster -> QuestionMasterResponseBean.builder()
            .questionId(blogStackQuestionMaster.getBsqmQuestionId())
            .question(blogStackQuestionMaster.getBsqmQuestion())
            .userId(blogStackQuestionMaster.getBsqmUserId())
            .tagId(blogStackQuestionMaster.getBsqmTagId())
            .codeSnippet(blogStackQuestionMaster.getBsqmCodeSnippet())
            .categoryId(blogStackQuestionMaster.getBsqmCategoryId())
            .subCategoryId(blogStackQuestionMaster.getBsqmSubCategoryId())
            .blogStackAnswers(blogStackQuestionMaster.getBlogStackAnswerMasterList())
            .status(blogStackQuestionMaster.getBsqmStatus())
            .addedOn(blogStackQuestionMaster.getBsqmCreatedDate())
            .build();

    public static Function<List<BlogStackQuestionMaster>, List<QuestionMasterResponseBean>> mapQuestionMasterEntityListToPojoListMapping = blogStackQuestionMasterList -> blogStackQuestionMasterList.stream()
            .map(blogStackQuestionMaster -> {
                QuestionMasterResponseBean.QuestionMasterResponseBeanBuilder questionMasterResponseBeanBuilder = QuestionMasterResponseBean.builder();
                questionMasterResponseBeanBuilder.questionId(blogStackQuestionMaster.getBsqmQuestionId())
                        .question(blogStackQuestionMaster.getBsqmQuestion())
                        .userId(blogStackQuestionMaster.getBsqmUserId())
                        .tagId(blogStackQuestionMaster.getBsqmTagId())
                        .codeSnippet(blogStackQuestionMaster.getBsqmCodeSnippet())
                        .categoryId(blogStackQuestionMaster.getBsqmCategoryId())
                        .subCategoryId(blogStackQuestionMaster.getBsqmSubCategoryId())
                        .blogStackAnswers(blogStackQuestionMaster.getBlogStackAnswerMasterList())
                        .status(blogStackQuestionMaster.getBsqmStatus())
                        .addedOn(blogStackQuestionMaster.getBsqmCreatedDate());
                return questionMasterResponseBeanBuilder.build();
            }).collect(Collectors.toList());
}