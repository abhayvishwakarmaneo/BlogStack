package com.blogstack.entity.pojo.mapper;

import com.blogstack.beans.responses.CommentMasterResponseBean;
import com.blogstack.entities.BlogStackCommentMaster;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IBlogStackCommentMasterEntityPojoMapper {
    public static Function<BlogStackCommentMaster, CommentMasterResponseBean> mapCommentMasterEntityPojoMapping = blogStackCommentMaster -> CommentMasterResponseBean.builder()
            .commentId(blogStackCommentMaster.getBscmCommentId())
            .comment(blogStackCommentMaster.getBscmComment())
            .status(blogStackCommentMaster.getBscmStatus())
            .downvote(blogStackCommentMaster.getBscmDownvote())
            .upvote(blogStackCommentMaster.getBscmUpvote())
            .addedOn(blogStackCommentMaster.getBscmCreatedDate())
            .modifiedOn(blogStackCommentMaster.getBscmModifiedDate())
            .build();

    public static Function<List<BlogStackCommentMaster>, List<CommentMasterResponseBean>> mapCommentMasterEntityListToPojoListMapping = blogStackCommentMasterList -> blogStackCommentMasterList.stream()
            .map(blogStackCommentMaster -> {
                CommentMasterResponseBean.CommentMasterResponseBeanBuilder commentMasterResponseBeanBuilder = CommentMasterResponseBean.builder();
                commentMasterResponseBeanBuilder.commentId(blogStackCommentMaster.getBscmCommentId())
                        .comment(blogStackCommentMaster.getBscmComment())
                        .status(blogStackCommentMaster.getBscmStatus())
                        .addedOn(blogStackCommentMaster.getBscmCreatedDate())
                        .upvote(blogStackCommentMaster.getBscmUpvote())
                        .downvote(blogStackCommentMaster.getBscmDownvote())
                        .modifiedOn(blogStackCommentMaster.getBscmModifiedDate());


                return commentMasterResponseBeanBuilder.build();
            }).collect(Collectors.toList());
}