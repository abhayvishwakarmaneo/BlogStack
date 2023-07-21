package com.blogstack.repository;

import com.blogstack.entities.BlogStackCommentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface IBlogStackCommentMasterRepository extends JpaRepository<BlogStackCommentMaster,Long>, JpaSpecificationExecutor<BlogStackCommentMaster> {


    Optional<BlogStackCommentMaster> findByBscmCommentIgnoreCase(String comment);

    Optional<BlogStackCommentMaster> findByBscmCommentId(String commentId);


}
