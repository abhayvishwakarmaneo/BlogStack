package com.blogstack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "blogstack_answer_master", schema = "master_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogStackAnswerMaster implements Serializable {

    private static final Long searialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "bsam_seq_id")
    private Long bsamSeqId;

    @Column(name = "bsam_answer_id", columnDefinition = "TEXT")
    @JsonProperty(value = "answer_id")
    private String bsamAnswerId;

    @Column(name = "bsam_answer", columnDefinition = "TEXT")
    @JsonProperty(value = "answer")
    private String bsamAnswer;

    @Column(name = "bsam_status")
    @JsonIgnore
    private String bsamStatus;

    @CreatedBy
    @Column(name = "bsam_created_by")
    @JsonIgnore
    private String bsamCreatedBy;

    @CreatedDate
    @Column(name = "bsam_created_date")
    @JsonIgnore
    private LocalDateTime bsamCreatedDate;

    @LastModifiedBy
    @Column(name = "bsam_modified_by")
    @JsonIgnore
    private String bsamModifiedBy;

    @LastModifiedDate
    @Column(name = "bsam_modified_date")
    @JsonIgnore
    private LocalDateTime bsamModifiedDate;

}