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
public class AnswerMasterRequestBean {
    @JsonProperty(value = "answer_id")
    private String answerId;

    @NotNull(message = "Answer can not be empty.")
    private String answer;

    private String status;

    @JsonIgnore
    private String modifiedBy;

    @JsonIgnore
    private String createdBy;
}