package com.blogstack.repository;

import com.blogstack.entities.BlogStackQuestionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBlogStackQuestionMasterRepository extends JpaRepository<BlogStackQuestionMaster, Long> {

    Optional<BlogStackQuestionMaster> findByBsqmQuestionIgnoreCase(String question);

    Optional<BlogStackQuestionMaster> findByBsqmQuestionId(String questionId);
}