package com.blogstack.service.impl;

import com.blogstack.beans.requests.AnswerMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackAnswerMaster;
import com.blogstack.entities.BlogStackQuestionMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackAnswerMasterEntityPojoMapper;
import com.blogstack.entity.pojo.mapper.IBlogStackQuestionMasterEntityPojoMapper;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        LOGGER.warn("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

        if(blogStackAnswerMasterOptional.isPresent())
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackMessageConstants.INSTANCE.ALREADY_EXIST).build());

        String answerId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.ANSWER_ID.getValue());
        LOGGER.warn("AnswerId :: {}", answerId);

        answerMasterRequestBean.setAnswerId(answerId);
        answerMasterRequestBean.setCreatedBy(this.springApplicationName);
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.warn("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        Optional<BlogStackQuestionMaster> blogStackQuestionsAnswerMasterOptional = blogStackQuestionMasterOptional.map(question -> {
            question.getBlogStackAnswerMasterList().add(IBlogStackAnswerMasterPojoEntityMapper.INSTANCE.answerMasterRequestToAnswerMasterEntity(answerMasterRequestBean));

//            return this.blogStackAnswerMasterRepository.saveAndFlush(IBlogStackAnswerMasterPojoEntityMapper.INSTANCE.answerMasterRequestToAnswerMasterEntity(answerMasterRequestBean));
            return this.blogStackQuestionMasterRepository.saveAndFlush(question);
        });
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionsAnswerMasterOptional.get())).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAllAnswer(Integer page, Integer size) {
        Page<BlogStackAnswerMaster> blogStackAnswerMasterPage = this.blogStackAnswerMasterRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackAnswerMasterPage :: {}", blogStackAnswerMasterPage);

        if(CollectionUtils.isEmpty(blogStackAnswerMasterPage.toList()))
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
        LOGGER.warn("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        List<BlogStackAnswerMaster> answers = new ArrayList<>(blogStackQuestionMasterOptional.get().getBlogStackAnswerMasterList());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackAnswerMasterEntityPojoMapper.mapAnswerMasterEntityListToPojoListMapping.apply(answers)).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAnswerById(String answerId) {
        Optional<BlogStackAnswerMaster> blogStackAnswerMasterOptional = this.blogStackAnswerMasterRepository.findByBsamAnswerId(answerId);
        LOGGER.warn("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

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
        LOGGER.warn("BlogStackAnswerMasterOptional :: {}", blogStackAnswerMasterOptional);

        if(blogStackAnswerMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        this.blogStackAnswerMasterRepository.delete(blogStackAnswerMasterOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.INSTANCE.DATA_DELETED).build());
    }

    @Override
    public Optional<ServiceResponseBean> deleteAllAnswerByQuestionId(String questionId) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.warn("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);


        Set<BlogStackAnswerMaster> blogStackAnswerMasterList = blogStackQuestionMasterOptional.get().getBlogStackAnswerMasterList();
        LOGGER.warn("BlogStackAnswerMasterList :: {}", blogStackAnswerMasterList);
        blogStackQuestionMasterOptional.get().getBlogStackAnswerMasterList().clear();
        this.blogStackQuestionMasterRepository.saveAndFlush(blogStackQuestionMasterOptional.get());

        this.blogStackAnswerMasterRepository.deleteAll(blogStackAnswerMasterList);

//        List<Set<BlogStackAnswerMaster>> mappedAnswerList = blogStackQuestionMasterOptional.stream().map(BlogStackQuestionMaster::getBlogStackAnswerMasterList).toList();
//        mappedAnswerList.forEach(blogStackAnswerMastersList -> this.blogStackAnswerMasterRepository.deleteAll(blogStackAnswerMastersList));

//        blogStackQuestionMasterOptional
//                .stream()
//                .flatMap(questionMaster -> questionMaster.getBlogStackAnswerMasterList().stream())
//                .forEach(blogStackAnswerMasterRepository::delete);


//        blogStackQuestionMasterOptional.get().setBlogStackAnswerMasterList(null);
//        blogStackQuestionMasterOptional.get().setBsqmModifiedDate(LocalDateTime.now());
//        blogStackQuestionMasterOptional.get().setBsqmModifiedBy(this.springApplicationName);

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.INSTANCE.DATA_DELETED).build());
    }
}