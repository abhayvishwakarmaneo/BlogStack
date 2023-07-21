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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blogstack-comment_master" ,schema = "master_data")
public class BlogStackCommentMaster implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bscm_seq_id")
    private  long bscmSeqId;
    @Column(name="bscm_comment_id")
    private  String bscmCommentId;
    @Column(name = "bscm_comment")
    private  String bscmComment;
    @Column(name="bscm_upvote")
    private  Long bscmUpvote;
    @Column(name = "bscm_downvote")
    private  Long bscmDownvote;
    @Column(name = "bscm_status")
    private  String bscmStatus;
    @CreatedBy
    @Column(name = "bscm_created_by")
    private  String bscmCreatedBy;
    @CreatedDate
    @Column(name = "bscm_created_date")
    private LocalDateTime bscmCreatedDate;
    @LastModifiedBy
    @Column(name = "bscm_modified_by")
    private String bscmModifiedBy;
    @LastModifiedDate
    @Column(name = "bscm_modified_date")
    private LocalDateTime bscmModifiedDate;


}
