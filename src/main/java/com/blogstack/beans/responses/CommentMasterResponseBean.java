package com.blogstack.beans.responses;

import com.blogstack.commons.BlogStackCommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentMasterResponseBean {
    @JsonProperty(value="comment_id")
    private  String commentId;
    @NotNull(message="Comment cannot be empty")
    private  String comment;

    private  String status;
    @JsonProperty(value = "upvote")
    private Long upvote;
    @JsonProperty(value = "downvote")
    private Long downvote;

    @JsonProperty(value = "added_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = BlogStackCommonConstants.DATE_FORMAT)
    private LocalDateTime addedOn;

    @JsonProperty(value="modified_on")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = BlogStackCommonConstants.DATE_FORMAT)
    private LocalDateTime modifiedOn;

}
