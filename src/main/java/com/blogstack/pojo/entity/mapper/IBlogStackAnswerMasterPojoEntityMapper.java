package com.blogstack.pojo.entity.mapper;

import com.blogstack.beans.requests.AnswerMasterRequestBean;
import com.blogstack.entities.BlogStackAnswerMaster;
import com.blogstack.enums.AnswerMasterStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, AnswerMasterStatusEnum.class})
public interface IBlogStackAnswerMasterPojoEntityMapper {

    IBlogStackAnswerMasterPojoEntityMapper INSTANCE = Mappers.getMapper(IBlogStackAnswerMasterPojoEntityMapper.class);

    @Mappings({
            @Mapping(target = "bsamAnswerId", source = "answerMasterRequestBean.answerId"),
            @Mapping(target = "bsamAnswer", source = "answerMasterRequestBean.answer"),
            @Mapping(target = "bsamStatus", expression = "java(AnswerMasterStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "bsamCreatedBy", source = "answerMasterRequestBean.createdBy"),
            @Mapping(target = "bsamCreatedDate", expression = "java(LocalDateTime.now())")
    })
    BlogStackAnswerMaster answerMasterRequestToAnswerMasterEntity(AnswerMasterRequestBean answerMasterRequestBean);

    public static BiFunction<AnswerMasterRequestBean, BlogStackAnswerMaster, BlogStackAnswerMaster> updateAnswerMaster = (answerMasterRequestBean, blogStackAnswerMaster) -> {
        blogStackAnswerMaster.setBsamAnswerId(answerMasterRequestBean.getAnswerId() != null ? answerMasterRequestBean.getAnswerId() : blogStackAnswerMaster.getBsamAnswerId());
        blogStackAnswerMaster.setBsamAnswer(answerMasterRequestBean.getAnswerId() != null ? answerMasterRequestBean.getAnswer() : blogStackAnswerMaster.getBsamAnswer());
        blogStackAnswerMaster.setBsamStatus(answerMasterRequestBean.getStatus() != null ? answerMasterRequestBean.getStatus() : blogStackAnswerMaster.getBsamStatus());
        blogStackAnswerMaster.setBsamModifiedBy(answerMasterRequestBean.getModifiedBy());
        blogStackAnswerMaster.setBsamModifiedDate(LocalDateTime.now());
        return blogStackAnswerMaster;
    };
}