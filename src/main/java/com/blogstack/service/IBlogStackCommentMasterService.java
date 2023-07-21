package com.blogstack.service;

import com.blogstack.beans.requests.CommentMasterRequestBean;

import java.util.Optional;

public interface IBlogStackCommentMasterService {
    Optional<?> addComment(CommentMasterRequestBean commentMasterRequestBean);
    Optional<?> fetchAllComment(Integer page, Integer size);

    Optional<?> fetchCommentById(String commentId);

    Optional<?> updateComment(CommentMasterRequestBean commentMasterRequestBean);

    Optional<?> deleteComment(String commentId);
}
