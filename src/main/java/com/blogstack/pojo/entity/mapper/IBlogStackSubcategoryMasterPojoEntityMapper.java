package com.blogstack.pojo.entity.mapper;
import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import com.blogstack.entities.BlogStackSubcategoryMaster;
import com.blogstack.enums.SubcategoryMasterStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, SubcategoryMasterStatusEnum.class})
public interface IBlogStackSubcategoryMasterPojoEntityMapper {

    IBlogStackSubcategoryMasterPojoEntityMapper INSTANCE = Mappers.getMapper(IBlogStackSubcategoryMasterPojoEntityMapper.class);

    @Mappings({
            @Mapping(target = "bsscmSubcategoryId" , source = "subcategoryMasterRequestBean.subcategoryId"),
            @Mapping(target = "bsscmCategoryId", source = "subcategoryMasterRequestBean.categoryId"),
            @Mapping(target = "bsscmSubcategory", source = "subcategoryMasterRequestBean.subcategory"),
            @Mapping(target = "bsscmStatus", expression = "java(SubcategoryMasterStatusEnum.ACTIVE.getValue())"),
            @Mapping(target = "bsscmCreatedBy", source = "subcategoryMasterRequestBean.createdBy"),
            @Mapping(target = "bsscmCreatedDate", expression = "java(LocalDateTime.now())")
    })
    BlogStackSubcategoryMaster subcategoryMasterRequestToSubcategoryMasterEntity(SubcategoryMasterRequestBean subcategoryMasterRequestBean);

    public static BiFunction<SubcategoryMasterRequestBean, BlogStackSubcategoryMaster, BlogStackSubcategoryMaster> updateSubcategoryMaster = (subcategoryMasterRequestBean, blogStackSubcategoryMaster) ->  {
       blogStackSubcategoryMaster.setBsscmSubcategoryId(subcategoryMasterRequestBean.getSubcategoryId() != null ? subcategoryMasterRequestBean.getSubcategoryId() : blogStackSubcategoryMaster.getBsscmSubcategoryId());
       blogStackSubcategoryMaster.setBsscmSubcategory(subcategoryMasterRequestBean.getSubcategory() != null ? subcategoryMasterRequestBean.getSubcategory(): blogStackSubcategoryMaster.getBsscmSubcategory());
       blogStackSubcategoryMaster.setBsscmCategoryId(subcategoryMasterRequestBean.getCategoryId() != null ? subcategoryMasterRequestBean.getCategoryId() : blogStackSubcategoryMaster.getBsscmCategoryId());
       blogStackSubcategoryMaster.setBsscmStatus(subcategoryMasterRequestBean.getStatus() != null ? subcategoryMasterRequestBean.getStatus() : blogStackSubcategoryMaster.getBsscmStatus());
       blogStackSubcategoryMaster.setBsscmModifiedBy(subcategoryMasterRequestBean.getModifiedBy());
       blogStackSubcategoryMaster.setBsscmModifiedDate(LocalDateTime.now());
       return blogStackSubcategoryMaster;
    };
}
