package com.blogstack.entity.pojo.mapper;

import com.blogstack.beans.responses.SubcategoryMasterResponseBean;
import com.blogstack.entities.BlogStackSubcategoryMaster;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IBlogStackSubcategoryMasterEntityPojoMapper {
    public static Function<BlogStackSubcategoryMaster, SubcategoryMasterResponseBean> mapSubcategoryMasterEntityPojoMapping = blogStackSubcategoryMaster -> SubcategoryMasterResponseBean.builder()
            .subcategoryId(blogStackSubcategoryMaster.getBsscmSubcategoryId())
            .subcategory(blogStackSubcategoryMaster.getBsscmSubcategory())
            .categoryId(blogStackSubcategoryMaster.getBsscmCategoryId())
            .status(blogStackSubcategoryMaster.getBsscmStatus())
            .addedOn(blogStackSubcategoryMaster.getBsscmCreatedDate())
            .build();


    public static Function<List<BlogStackSubcategoryMaster>, List<SubcategoryMasterResponseBean>> mapSubcategoryMasterEntityListToPojoListMapping = blogStackSubcategoryMasterList -> blogStackSubcategoryMasterList.stream()
            .map(blogStackSubcategoryMaster -> {
                SubcategoryMasterResponseBean.SubcategoryMasterResponseBeanBuilder subcategoryMasterResponseBeanBuilder = SubcategoryMasterResponseBean.builder();
                subcategoryMasterResponseBeanBuilder.subcategoryId(blogStackSubcategoryMaster.getBsscmSubcategoryId())
                        .subcategory(blogStackSubcategoryMaster.getBsscmSubcategory())
                        .categoryId(blogStackSubcategoryMaster.getBsscmCategoryId())
                        .status(blogStackSubcategoryMaster.getBsscmStatus())
                        .addedOn(blogStackSubcategoryMaster.getBsscmCreatedDate());
                return subcategoryMasterResponseBeanBuilder.build();
            }).collect(Collectors.toList());
}
