package com.blogstack.pojo.entity.mapper;

import com.blogstack.beans.requests.CommentMasterRequestBean;
import com.blogstack.entities.BlogStackCommentMaster;
import com.blogstack.enums.CommentMasterStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, CommentMasterStatusEnum.class})
public interface IBlogStackCommentMasterPojoEntityMapper {

    IBlogStackCommentMasterPojoEntityMapper INSTANCE = Mappers.getMapper(IBlogStackCommentMasterPojoEntityMapper.class);

    @Mappings({
            @Mapping(target = "bscmCommentId", source = "commentMasterRequestBean.commentId"),
            @Mapping(target = "bscmComment", source = "commentMasterRequestBean.comment"),
            @Mapping(target="bscmDownvote",source="commentMasterRequestBean.downvote"),
            @Mapping(target = "bscmUpvote",source = "commentMasterRequestBean.upvote"),
            @Mapping(target = "bscmStatus", expression = "java(CommentMasterStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "bscmCreatedBy", source = "commentMasterRequestBean.createdBy"),
            @Mapping(target = "bscmCreatedDate", expression = "java(LocalDateTime.now())")
    })
    BlogStackCommentMaster commentMasterRequestToCommentMasterEntity(CommentMasterRequestBean commentMasterRequestBean);

    public static BiFunction<CommentMasterRequestBean, BlogStackCommentMaster, BlogStackCommentMaster> updateCommentMaster = ((commentMasterRequestBean, blogStackCommentMaster) ->  {
        blogStackCommentMaster.setBscmCommentId(commentMasterRequestBean.getCommentId() != null ? commentMasterRequestBean.getCommentId() : blogStackCommentMaster.getBscmCommentId());
        blogStackCommentMaster.setBscmComment(commentMasterRequestBean.getComment() != null ? commentMasterRequestBean.getComment() : blogStackCommentMaster.getBscmComment());
        blogStackCommentMaster.setBscmDownvote(commentMasterRequestBean.getDownvote() != null ? commentMasterRequestBean.getDownvote() : blogStackCommentMaster.getBscmDownvote());
        blogStackCommentMaster.setBscmUpvote(commentMasterRequestBean.getUpvote() != null ? commentMasterRequestBean.getUpvote() : blogStackCommentMaster.getBscmUpvote());
        blogStackCommentMaster.setBscmStatus(commentMasterRequestBean.getStatus() != null ? commentMasterRequestBean.getStatus() : blogStackCommentMaster.getBscmStatus());
        blogStackCommentMaster.setBscmModifiedBy(commentMasterRequestBean.getModifiedBy());
        blogStackCommentMaster.setBscmModifiedDate(LocalDateTime.now());
        return blogStackCommentMaster;
    });
}