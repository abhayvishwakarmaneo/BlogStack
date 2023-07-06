package com.blogstack.beans.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponseBean {

    private String message;

    @Builder.Default
    private Boolean status = Boolean.FALSE;

    private Object data;

    private Set<String> errors;
}