package com.blogstack.service;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import reactor.core.publisher.Mono;

public interface IBlogStackSubcategoryMasterService {

    Mono<?> addSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean);

    Mono<?> fetchSubcategoryById(String subcategoryId);

    Mono<?> fetchAllSubcategories(String filterCriteria, String sortCriteria, Integer page, Integer size, String... args);

    Mono<?> fetchSubcategoryByCategoryId(String categoryId);

    Mono<?> deleteSubcategory(String subcategoryId);

    Mono<?> updateSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean);
}
