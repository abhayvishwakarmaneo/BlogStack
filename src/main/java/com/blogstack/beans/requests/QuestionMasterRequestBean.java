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
public class QuestionMasterRequestBean {
    @JsonProperty(value = "question_id")
    private String questionId;

    @NotNull(message = "Question can not be empty.")
    private String question;

    private String status;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "code_snippet")
    private String codeSnippet;

    @JsonProperty(value = "tag_id")
    private String tagId;

    @JsonProperty(value = "category_id")
    private String categoryId;

    @JsonProperty(value = "sub_category_id")
    private String subCategoryId;

    @JsonIgnore
    private String modifiedBy;

    @JsonIgnore
    private String createdBy;
}