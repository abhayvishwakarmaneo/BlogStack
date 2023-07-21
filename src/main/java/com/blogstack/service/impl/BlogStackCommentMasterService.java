package com.blogstack.service.impl;

import com.blogstack.beans.requests.CommentMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackCommentMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackCommentMasterEntityPojoMapper;
import com.blogstack.enums.CommentMasterStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogStackCustomException;
import com.blogstack.exceptions.BlogstackDataNotFoundException;
import com.blogstack.pojo.entity.mapper.IBlogStackCommentMasterPojoEntityMapper;
import com.blogstack.repository.IBlogStackCommentMasterRepository;
import com.blogstack.service.IBlogStackCommentMasterService;
import com.blogstack.utils.BlogStackCommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogStackCommentMasterService implements IBlogStackCommentMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackCommentMasterService.class);

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackCommentMasterRepository iBlogStackCommentMasterRepository;
    @Autowired
    private IBlogStackCommentMasterPojoEntityMapper blogStackQuestionMasterPojoEntityMapper;

    @Override
    public Optional <?> addComment(CommentMasterRequestBean commentMasterRequestBean) {
        Optional<BlogStackCommentMaster> blogStackCommentMasterOptional = this.iBlogStackCommentMasterRepository.findByBscmCommentId(commentMasterRequestBean.getCommentId());
        LOGGER.info("BlogStackCommentMasterOptional::{}", blogStackCommentMasterOptional);

        if (blogStackCommentMasterOptional.isPresent())
            throw new BlogStackCustomException(BlogStackMessageConstants.INSTANCE.ALREADY_EXIST);

        String commentId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.COMMENT_ID.getValue());
        LOGGER.info("CommentId :: {}", commentId);

        commentMasterRequestBean.setCommentId(commentId);
        commentMasterRequestBean.setCreatedBy(springApplicationName);
        BlogStackCommentMaster blogStackCommentMaster = this.iBlogStackCommentMasterRepository.saveAndFlush(this.blogStackQuestionMasterPojoEntityMapper.INSTANCE.commentMasterRequestToCommentMasterEntity(commentMasterRequestBean));
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackCommentMasterEntityPojoMapper.mapCommentMasterEntityPojoMapping.apply(blogStackCommentMaster)).build());
    }


    @Override
    public Optional<?> fetchAllComment(Integer page, Integer size) {
        Page<BlogStackCommentMaster> blogStackCommentMasterPage = this.iBlogStackCommentMasterRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackCommentMaster :: {}", blogStackCommentMasterPage);

        List<BlogStackCommentMaster> listOfBlogStackExistingComments = blogStackCommentMasterPage.stream().filter(k -> !k.getBscmStatus().equals(CommentMasterStatusEnum.DELETED.getValue())).collect(Collectors.toList());
        System.out.println(listOfBlogStackExistingComments);

        if (CollectionUtils.isEmpty(listOfBlogStackExistingComments))
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return  Optional.of(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackCommentMasterEntityPojoMapper.mapCommentMasterEntityListToPojoListMapping.apply(listOfBlogStackExistingComments))
                        .numberOfElements(listOfBlogStackExistingComments.toArray().length)
                        .pageSize(blogStackCommentMasterPage.getSize())
                        .totalElements(blogStackCommentMasterPage.getTotalElements())
                        .totalPages(blogStackCommentMasterPage.getTotalPages())
                        .currentPage(blogStackCommentMasterPage.getNumber())
                        .build()).build());
    }

    @Override
    public Optional<?> fetchCommentById(String commentId) {
        Optional<BlogStackCommentMaster> blogStackCommentMasterOptional = this.iBlogStackCommentMasterRepository.findByBscmCommentId(commentId);
        LOGGER.info("BlogStackQuestionMasterOptional :: {}", blogStackCommentMasterOptional);

        if (blogStackCommentMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException("Question not found.");

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackCommentMasterEntityPojoMapper.mapCommentMasterEntityPojoMapping.apply(blogStackCommentMasterOptional.get())).build());
    }

    @Override
    public Optional<?> updateComment(CommentMasterRequestBean commentMasterRequestBean) {
        Optional<BlogStackCommentMaster> blogStackCommenMasterOptional = this.iBlogStackCommentMasterRepository.findByBscmCommentId(commentMasterRequestBean.getCommentId());
        LOGGER.debug("BlogStackQuestionMasterOptional :: {}", blogStackCommenMasterOptional);

        if (blogStackCommenMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException("Comment not found.");

        commentMasterRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackCommentMaster blogStackQuestionMaster = this.blogStackQuestionMasterPojoEntityMapper.INSTANCE.updateCommentMaster.apply(commentMasterRequestBean, blogStackCommenMasterOptional.get());
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackQuestionMaster);

        this.iBlogStackCommentMasterRepository.saveAndFlush(blogStackQuestionMaster);
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackCommentMasterEntityPojoMapper.mapCommentMasterEntityPojoMapping.apply(blogStackQuestionMaster)).build());
    }

    @Override
    public Optional<?> deleteComment(String commentId) {
        Optional<BlogStackCommentMaster> blogStackCommentMasterOptional = this.iBlogStackCommentMasterRepository.findByBscmCommentId(commentId);
        LOGGER.info("blogStackCommentMasterOptional :: {}", blogStackCommentMasterOptional);

        if (blogStackCommentMasterOptional.isEmpty())
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message("Comment not found.").build());

        blogStackCommentMasterOptional.get().setBscmStatus(CommentMasterStatusEnum.DELETED.getValue());
        blogStackCommentMasterOptional.get().setBscmModifiedBy(springApplicationName);
        this.iBlogStackCommentMasterRepository.saveAndFlush(blogStackCommentMasterOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message("Comment Deleted").build());
    }
}
