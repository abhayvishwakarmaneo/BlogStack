package com.blogstack.service.impl;

import com.blogstack.beans.requests.QuestionMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.entities.BlogStackQuestionMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackQuestionMasterEntityPojoMapper;
import com.blogstack.enums.QuestionMasterStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogstackDataNotFoundException;
import com.blogstack.pojo.entity.mapper.IBlogStackQuestionMasterPojoEntityMapper;
import com.blogstack.repository.IBlogStackQuestionMasterRepository;
import com.blogstack.service.IBlogStackQuestionMasterService;
import com.blogstack.utils.BlogStackCommonUtils;
import com.blogstack.utils.BlogStackSpecificationUtils;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BlogStackQuestionMasterService implements IBlogStackQuestionMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackQuestionMasterService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackQuestionMasterRepository blogStackQuestionMasterRepository;

    @Autowired
    private IBlogStackQuestionMasterPojoEntityMapper blogStackQuestionMasterPojoEntityMapper;

    @Override
    public Mono<?> addQuestion(QuestionMasterRequestBean questionMasterRequestBean) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionIgnoreCase(questionMasterRequestBean.getQuestion());
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if(blogStackQuestionMasterOptional.isPresent())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message("Question Already Present.").build());

        String questionId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.QUESTION_ID.getValue());
        LOGGER.info("QuestionId :: {}", questionId);

        questionMasterRequestBean.setQuestionId(questionId);
        questionMasterRequestBean.setCreatedBy(springApplicationName);
        BlogStackQuestionMaster blogStackQuestionMaster = this.blogStackQuestionMasterRepository.saveAndFlush(this.blogStackQuestionMasterPojoEntityMapper.INSTANCE.questionMasterRequestToQuestionMasterEntity(questionMasterRequestBean));
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionMaster)).build());
    }

    @Override
    public Mono<?> fetchAllQuestion(String filterCriteria, String sortCriteria, Integer page, Integer size, String... args) {
        Specification<BlogStackQuestionMaster> specification = null;
        if (StringUtils.isNotEmpty(filterCriteria)) {
            String entityFilterCriteria = BlogStackSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "bsqm");
            LOGGER.debug("EntityFilterCriteria :: {}", entityFilterCriteria);
            specification = BlogStackSpecificationUtils.INSTANCE.buildSpecificaton(entityFilterCriteria, new ArrayList<>());
        }
        Sort sort = StringUtils.isNotEmpty(sortCriteria) ? BlogStackSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "bsqm") : Sort.by("bsqmSeqId").ascending();

        Page<BlogStackQuestionMaster> blogStackQuestionMasterPage = this.blogStackQuestionMasterRepository.findAll(specification, PageRequest.of(page, size, sort));
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackQuestionMasterPage);

        return CollectionUtils.isNotEmpty(blogStackQuestionMasterPage.toList()) ? Mono.just(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityListToPojoListMapping.apply(blogStackQuestionMasterPage.toList()))
                        .numberOfElements(blogStackQuestionMasterPage.getNumberOfElements())
                        .pageSize(blogStackQuestionMasterPage.getSize())
                        .totalElements(blogStackQuestionMasterPage.getTotalElements())
                        .totalPages(blogStackQuestionMasterPage.getTotalPages())
                        .currentPage(blogStackQuestionMasterPage.getNumber())
                        .build()).build())
                : Mono.error(new BlogstackDataNotFoundException("Data not found."));
    }

    @Override
    public Mono<?> fetchQuestionById(String questionId) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if(blogStackQuestionMasterOptional.isEmpty())
            return Mono.error(new BlogstackDataNotFoundException("Question not found."));

        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionMasterOptional.get())).build());
    }

    @Override
    public Mono<?> updateQuestion(QuestionMasterRequestBean questionMasterRequestBean) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionMasterRequestBean.getQuestionId());
        LOGGER.debug("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if (blogStackQuestionMasterOptional.isEmpty())
            return Mono.error(new BlogstackDataNotFoundException("Question not found."));

        questionMasterRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackQuestionMaster blogStackQuestionMaster = this.blogStackQuestionMasterPojoEntityMapper.INSTANCE.updateQuestionMaster.apply(questionMasterRequestBean, blogStackQuestionMasterOptional.get());
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackQuestionMaster);

        this.blogStackQuestionMasterRepository.saveAndFlush(blogStackQuestionMaster);
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackQuestionMasterEntityPojoMapper.mapQuestionMasterEntityPojoMapping.apply(blogStackQuestionMaster)).build());
    }

    @Override
    public Mono<?> deleteQuestion(String questionId) {
        Optional<BlogStackQuestionMaster> blogStackQuestionMasterOptional = this.blogStackQuestionMasterRepository.findByBsqmQuestionId(questionId);
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackQuestionMasterOptional);

        if(blogStackQuestionMasterOptional.isEmpty())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message("Question not found.").build());

        blogStackQuestionMasterOptional.get().setBsqmStatus(QuestionMasterStatusEnum.DELETED.getValue());
        blogStackQuestionMasterOptional.get().setBsqmModifiedBy(springApplicationName);
        this.blogStackQuestionMasterRepository.saveAndFlush(blogStackQuestionMasterOptional.get());
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).message("Question Deleted").build());
    }
}