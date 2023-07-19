package com.blogstack.service;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import com.blogstack.beans.responses.ServiceResponseBean;

import java.util.Optional;

public interface IBlogStackSubcategoryMasterService {

    Optional<ServiceResponseBean> addSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean);

    Optional<ServiceResponseBean> fetchSubcategoryById(String subcategoryId);

    Optional<ServiceResponseBean> fetchAllSubcategories(Integer page, Integer size);

    Optional<ServiceResponseBean> fetchSubcategoryByCategoryId(String categoryId);

    Optional<ServiceResponseBean> deleteSubcategory(String subcategoryId);

    Optional<ServiceResponseBean> updateSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean);
}
