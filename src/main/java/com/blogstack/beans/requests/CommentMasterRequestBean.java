package com.blogstack.beans.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentMasterRequestBean {
    @JsonProperty(value="comment_id")
    private  String commentId;
    @NotNull(message="Comment cannot be empty")
    private  String comment;

    private  String status;
    @JsonProperty(value = "upvote")
    private Long upvote;
    @JsonProperty(value = "downvote")
    private Long downvote;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String modifiedBy;



}
