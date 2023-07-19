package com.blogstack.service.impl;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackQuestionMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackQuestionMasterEntityPojoMapper;
import com.blogstack.enums.AnswerMasterStatusEnum;
import com.blogstack.enums.QuestionMasterStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogstackDataNotFoundException;
import com.blogstack.pojo.entity.mapper.IBlogStackQuestionMasterPojoEntityMapper;
import com.blogstack.repository.IBlogStackAnswerMasterRepository;
import com.blogstack.repository.IBlogStackQuestionMasterRepository;
import com.blogstack.service.IBlogStackQuestionMasterService;
import com.blogstack.utils.BlogStackCommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BlogStackQuestionMasterService implements IBlogStackQuestionMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackQuestionMasterService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackQuestionMasterRepository blogStackQuestionMasterRepository;

    @Autowired
    private IBlogStackAnswerMasterRepository blogStackAnswerMasterRepository;

    @Override
    public Optional<ServiceResponseBean> addQuestion(QuestionMasterRequestBean questionMasterRequestBean) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionIgnoreCase(questionMasterRequestBean.getQuestion());
        LOGGER.warn("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if(blogStackQuestionMasterOptional.isPresent())
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INSTANCE.ALREADY_EXIST).build());

        String questionId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.QUESTION_ID.getValue());
        LOGGER.warn("QuestionId :: {}", questionId);

        questionMasterRequestBean.setQuestionId(questionId);
        questionMasterRequestBean.setCreatedBy(springApplicationName);
        BlogStackQuestionMaster blogStackQuestionMaster = this.blogStackQuestionMasterRepository.saveAndFlush(IBlogStackQuestionMasterPojoEntityMapper.INSTANCE.questionMasterRequestToQuestionMasterEntity(questionMasterRequestBean));
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionMaster)).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAllQuestion(Integer page, Integer size) {
        Page<BlogStackQuestionMaster> blogStackQuestionMasterPage = this.blogStackQuestionMasterRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackQuestionMasterPage);

        if (CollectionUtils.isEmpty(blogStackQuestionMasterPage.toList()))
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityListToPojoListMapping.apply(blogStackQuestionMasterPage.toList()))
                        .numberOfElements(blogStackQuestionMasterPage.getNumberOfElements())
                        .pageSize(blogStackQuestionMasterPage.getSize())
                        .totalElements(blogStackQuestionMasterPage.getTotalElements())
                        .totalPages(blogStackQuestionMasterPage.getTotalPages())
                        .currentPage(blogStackQuestionMasterPage.getNumber())
                        .build()).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchQuestionById(String questionId) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.warn("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if(blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionMasterOptional.get())).build());
    }

    @Override
    public Optional<ServiceResponseBean> updateQuestion(QuestionMasterRequestBean questionMasterRequestBean) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionMasterRequestBean.getQuestionId());
        LOGGER.debug("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        questionMasterRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackQuestionMaster blogStackQuestionMaster = IBlogStackQuestionMasterPojoEntityMapper.updateQuestionMaster.apply(questionMasterRequestBean, blogStackQuestionMasterOptional.get());
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackQuestionMaster);

        this.blogStackQuestionMasterRepository.saveAndFlush(blogStackQuestionMaster);
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionMaster)).build());
    }

    @Override
    public Optional<ServiceResponseBean> deleteQuestion(String questionId) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.warn("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if(blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        this.blogStackQuestionMasterRepository.delete(blogStackQuestionMasterOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.INSTANCE.DATA_DELETED).build());
    }
}