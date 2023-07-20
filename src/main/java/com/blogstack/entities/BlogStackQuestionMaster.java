package com.blogstack.entities;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blogstack_question_master", schema = "master_data")
public class BlogStackQuestionMaster implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bsqm_seq_id")
    private Long bsqmSeqId;

    @Column(name = "bsqm_question_id")
    private String bsqmQuestionId;

    @Column(name = "bsqm_question")
    private String bsqmQuestion;

    @Column(name = "bsqm_user_id")
    private String bsqmUserId;

    @Column(name = "bsqm_code_snippet")
    private String bsqmCodeSnippet;

    @Column(name = "bsqm_tag_id")
    private String bsqmTagId;

    @Column(name = "bsqm_category_id")
    private String bsqmCategoryId;

    @Column(name = "bsqm_sub_category_id")
    private String bsqmSubCategoryId;

    @Column(name = "bsqm_status")
    private String bsqmStatus;

    @CreatedBy
    @Column(name = "bsqm_created_by")
    private String bsqmCreatedBy;

    @CreatedDate
    @Column(name = "bsqm_created_date")
    private LocalDateTime bsqmCreatedDate;

    @LastModifiedBy
    @Column(name = "bsqm_modified_by")
    private String bsqmModifiedBy;

    @LastModifiedDate
    @Column(name = "bsqm_modified_date")
    private LocalDateTime bsqmModifiedDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bsqm_question_id")
    private Set<BlogStackAnswerMaster> blogStackAnswerMasterList;
}