package com.blogstack.repository;

import com.blogstack.entities.BlogStackSubcategoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBlogStackSubcategoryMasterRepository extends JpaRepository<BlogStackSubcategoryMaster, Long>, JpaSpecificationExecutor<BlogStackSubcategoryMaster> {

    Optional<BlogStackSubcategoryMaster> findByBsscmSubcategoryIgnoreCase(String subcategory);

    Optional<BlogStackSubcategoryMaster> findByBsscmSubcategoryId(String subcategoryId);

    Optional<List<BlogStackSubcategoryMaster>> findByBsscmCategoryId(String categoryId);
}
