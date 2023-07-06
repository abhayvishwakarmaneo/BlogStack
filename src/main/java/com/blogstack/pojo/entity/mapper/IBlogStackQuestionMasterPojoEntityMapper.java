package com.blogstack.pojo.entity.mapper;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.entities.BlogStackQuestionMaster;
import com.blogstack.enums.QuestionMasterStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, QuestionMasterStatusEnum.class})
public interface IBlogStackQuestionMasterPojoEntityMapper {

    IBlogStackQuestionMasterPojoEntityMapper INSTANCE = Mappers.getMapper(IBlogStackQuestionMasterPojoEntityMapper.class);

    @Mappings({
            @Mapping(target = "bsqmQuestionId", source = "questionMasterRequestBean.questionId"),
            @Mapping(target = "bsqmQuestion", source = "questionMasterRequestBean.question"),
            @Mapping(target = "bsqmUserId", source = "questionMasterRequestBean.userId"),
            @Mapping(target = "bsqmCodeSnippet", source = "questionMasterRequestBean.codeSnippet"),
            @Mapping(target = "bsqmTagId", source = "questionMasterRequestBean.tagId"),
            @Mapping(target = "bsqmCategoryId", source = "questionMasterRequestBean.categoryId"),
            @Mapping(target = "bsqmSubCategoryId", source = "questionMasterRequestBean.subCategoryId"),
            @Mapping(target = "bsqmStatus", expression = "java(QuestionMasterStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "bsqmCreatedBy", source = "questionMasterRequestBean.createdBy"),
            @Mapping(target = "bsqmCreatedDate", expression = "java(LocalDateTime.now())")
    })
    BlogStackQuestionMaster questionMasterRequestToQuestionMasterEntity(QuestionMasterRequestBean questionMasterRequestBean);

    public static BiFunction<QuestionMasterRequestBean, BlogStackQuestionMaster, BlogStackQuestionMaster> updateQuestionMaster = (questionMasterRequestBean, blogStackQuestionMaster) -> {
        blogStackQuestionMaster.setBsqmQuestionId(questionMasterRequestBean.getQuestionId() != null ? questionMasterRequestBean.getQuestionId() : blogStackQuestionMaster.getBsqmQuestionId());
        blogStackQuestionMaster.setBsqmQuestion(questionMasterRequestBean.getQuestion() != null ? questionMasterRequestBean.getQuestion() : blogStackQuestionMaster.getBsqmQuestion());
        blogStackQuestionMaster.setBsqmUserId(questionMasterRequestBean.getUserId() != null ? questionMasterRequestBean.getUserId() : blogStackQuestionMaster.getBsqmUserId());
        blogStackQuestionMaster.setBsqmCodeSnippet(questionMasterRequestBean.getCodeSnippet() != null ? questionMasterRequestBean.getCodeSnippet() : blogStackQuestionMaster.getBsqmCodeSnippet());
        blogStackQuestionMaster.setBsqmTagId(questionMasterRequestBean.getTagId() != null ? questionMasterRequestBean.getTagId() : blogStackQuestionMaster.getBsqmTagId());
        blogStackQuestionMaster.setBsqmCategoryId(questionMasterRequestBean.getCategoryId() != null ? questionMasterRequestBean.getCategoryId() : blogStackQuestionMaster.getBsqmCategoryId());
        blogStackQuestionMaster.setBsqmSubCategoryId(questionMasterRequestBean.getSubCategoryId() != null ? questionMasterRequestBean.getSubCategoryId() : blogStackQuestionMaster.getBsqmSubCategoryId());
        blogStackQuestionMaster.setBsqmStatus(questionMasterRequestBean.getStatus() != null ? questionMasterRequestBean.getStatus() : blogStackQuestionMaster.getBsqmStatus());
        blogStackQuestionMaster.setBsqmModifiedBy(questionMasterRequestBean.getModifiedBy());
        blogStackQuestionMaster.setBsqmModifiedDate(LocalDateTime.now());
        return blogStackQuestionMaster;
    };
}