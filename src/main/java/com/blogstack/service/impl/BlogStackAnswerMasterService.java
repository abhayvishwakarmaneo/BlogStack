package com.blogstack.service.impl;

import com.blogstack.beans.requests.AnswerMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackAnswerMaster;
import com.blogstack.entities.BlogStackQuestionMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackAnswerMasterEntityPojoMapper;
import com.blogstack.enums.AnswerMasterStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogstackDataNotFoundException;
import com.blogstack.pojo.entity.mapper.IBlogStackAnswerMasterPojoEntityMapper;
import com.blogstack.repository.IBlogStackAnswerMasterRepository;
import com.blogstack.repository.IBlogStackQuestionMasterRepository;
import com.blogstack.service.IBlogStackAnswerMasterService;
import com.blogstack.utils.BlogStackCommonUtils;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogStackAnswerMasterService implements IBlogStackAnswerMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackAnswerMasterService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackAnswerMasterRepository blogStackAnswerMasterRepository;

    @Autowired
    private IBlogStackQuestionMasterRepository blogStackQuestionMasterRepository;

    @Override
    public Optional<ServiceResponseBean> addAnswer(String questionId, AnswerMasterRequestBean answerMasterRequestBean) {
        Optional<BlogStackAnswerMaster> blogStackAnswerMasterOptional = this.blogStackAnswerMasterRepository.findByBsamAnswerIgnoreCase(answerMasterRequestBean.getAnswer());
        LOGGER.info("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

        if(blogStackAnswerMasterOptional.isPresent())
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INSTANCE.ALREADY_EXIST).build());

        String answerId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ANSWER_ID.getValue());
        LOGGER.info("AnswerId :: {}", answerId);

        answerMasterRequestBean.setAnswerId(answerId);
        answerMasterRequestBean.setCreatedBy(this.springApplicationName);
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        Optional<BlogStackAnswerMaster> blogStackQuestionsAnswerMasterOptional = blogStackQuestionMasterOptional.map(question -> {
            question.getBlogStackAnswerMasterList().add(IBlogStackAnswerMasterPojoEntityMapper.INSTANCE.answerMasterRequestToAnswerMasterEntity(answerMasterRequestBean));

            return this.blogStackAnswerMasterRepository.saveAndFlush(IBlogStackAnswerMasterPojoEntityMapper.INSTANCE.answerMasterRequestToAnswerMasterEntity(answerMasterRequestBean));
        });
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackAnswerMasterEntityPojoMapper.mapAnswerMasterEntityPojoMapping.apply(blogStackQuestionsAnswerMasterOptional.get())).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAllAnswer(Integer page, Integer size) {
        Page<BlogStackAnswerMaster> blogStackAnswerMasterPage = this.blogStackAnswerMasterRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackAnswerMasterPage :: {}", blogStackAnswerMasterPage);

        if(CollectionUtils.isNotEmpty(blogStackAnswerMasterPage.toList()))
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackAnswerMasterEntityPojoMapper.mapAnswerMasterEntityListToPojoListMapping.apply(blogStackAnswerMasterPage.toList()))
                        .numberOfElements(blogStackAnswerMasterPage.getNumberOfElements())
                        .pageSize(blogStackAnswerMasterPage.getSize())
                        .totalElements(blogStackAnswerMasterPage.getTotalElements())
                        .totalPages(blogStackAnswerMasterPage.getTotalPages())
                        .currentPage(blogStackAnswerMasterPage.getNumber())
                        .build()).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAllAnswerByQuestionId(String questionId){
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        List<BlogStackAnswerMaster> answers = new ArrayList<>(blogStackQuestionMasterOptional.get().getBlogStackAnswerMasterList());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackAnswerMasterEntityPojoMapper.mapAnswerMasterEntityListToPojoListMapping.apply(answers)).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAnswerById(String answerId) {
        Optional<BlogStackAnswerMaster> blogStackAnswerMasterOptional = this.blogStackAnswerMasterRepository.findByBsamAnswerId(answerId);
        LOGGER.info("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

        if(blogStackAnswerMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackAnswerMasterEntityPojoMapper.mapAnswerMasterEntityPojoMapping.apply(blogStackAnswerMasterOptional.get())).build());
    }

    @Override
    public Optional<ServiceResponseBean> updateAnswer(AnswerMasterRequestBean answerMasterRequestBean) {
        Optional<BlogStackAnswerMaster> blogStackAnswerMasterOptional = this.blogStackAnswerMasterRepository.findByBsamAnswerId(answerMasterRequestBean.getAnswerId());
        LOGGER.debug("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

        if (blogStackAnswerMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        answerMasterRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackAnswerMaster blogStackAnswerMaster = IBlogStackAnswerMasterPojoEntityMapper.updateAnswerMaster.apply(answerMasterRequestBean, blogStackAnswerMasterOptional.get());
        LOGGER.debug("BlogStackAnswerMaster :: {}", blogStackAnswerMaster);

        this.blogStackAnswerMasterRepository.saveAndFlush(blogStackAnswerMaster);
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackAnswerMasterEntityPojoMapper.mapAnswerMasterEntityPojoMapping.apply(blogStackAnswerMaster)).build());
    }

    @Override
    public Optional<ServiceResponseBean> deleteAnswer(String answerId) {
        Optional<BlogStackAnswerMaster> blogStackAnswerMasterOptional = this.blogStackAnswerMasterRepository.findByBsamAnswerId(answerId);
        LOGGER.info("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

        if(blogStackAnswerMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        blogStackAnswerMasterOptional.get().setBsamStatus(AnswerMasterStatusEnum.DELETED.getValue());
        blogStackAnswerMasterOptional.get().setBsamModifiedBy(springApplicationName);
        this.blogStackAnswerMasterRepository.saveAndFlush(blogStackAnswerMasterOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.INSTANCE.DATA_DELETED).build());
    }

    @Override
    public Optional<ServiceResponseBean> deleteAllAnswer(String questionId) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        blogStackQuestionMasterOptional.get().setBlogStackAnswerMasterList(null);
        blogStackQuestionMasterOptional.get().setBsqmModifiedDate(LocalDateTime.now());
        blogStackQuestionMasterOptional.get().setBsqmModifiedBy(this.springApplicationName);
        this.blogStackQuestionMasterRepository.saveAndFlush(blogStackQuestionMasterOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.INSTANCE.DATA_DELETED).build());
    }
}