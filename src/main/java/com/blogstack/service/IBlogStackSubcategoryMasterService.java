package com.blogstack.service;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;

import java.util.Optional;

public interface IBlogStackSubcategoryMasterService {

    Optional<?> addSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean);

    Optional<?> fetchSubcategoryById(String subcategoryId);

    Optional<?> fetchAllSubcategories(Integer page, Integer size);

    Optional<?> fetchSubcategoryByCategoryId(String categoryId);

    Optional<?> deleteSubcategory(String subcategoryId);

    Optional<?> updateSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean);
}
