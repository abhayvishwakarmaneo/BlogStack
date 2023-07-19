package com.blogstack.service.impl;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.commons.BlogStackMessageConstants;
import com.blogstack.entities.BlogStackSubcategoryMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackSubcategoryMasterEntityPojoMapper;
import com.blogstack.enums.SubcategoryMasterStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogstackDataNotFoundException;
import com.blogstack.pojo.entity.mapper.IBlogStackSubcategoryMasterPojoEntityMapper;
import com.blogstack.repository.IBlogStackSubcategoryMasterRepository;
import com.blogstack.service.IBlogStackSubcategoryMasterService;
import com.blogstack.utils.BlogStackCommonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogStackSubcategoryMasterService implements IBlogStackSubcategoryMasterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogStackSubcategoryMasterService.class);

    @Autowired
    private IBlogStackSubcategoryMasterRepository blogStackSubcategoryMasterRepository;

    @Value("#{'${spring.application.name}'.toUpperCase()}")
    private String springApplicationName;

    @Autowired
    private IBlogStackSubcategoryMasterPojoEntityMapper blogStackSubcategoryMasterPojoEntityMapper;

    @Override
    public Optional<ServiceResponseBean> addSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryIgnoreCase(subcategoryMasterRequestBean.getSubcategory());
        LOGGER.warn("BlogStackSubcategoryMasterOptional :: {}", blogStackSubcategoryMasterOptional);

        if(blogStackSubcategoryMasterOptional.isPresent())
            return Optional.of(ServiceResponseBean.builder().status(Boolean.FALSE).message("Subcategory Already Present.").build());

        String subcategoryId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.SUBCATEGORY_ID.getValue());
        LOGGER.warn("SubcategoryId :: {}", subcategoryId);

        subcategoryMasterRequestBean.setSubcategoryId(subcategoryId);
        subcategoryMasterRequestBean.setCreatedBy(springApplicationName);

        BlogStackSubcategoryMaster blogStackSubcategoryMaster = this.blogStackSubcategoryMasterRepository.saveAndFlush(this.blogStackSubcategoryMasterPojoEntityMapper.INSTANCE.subcategoryMasterRequestToSubcategoryMasterEntity(subcategoryMasterRequestBean));
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityPojoMapping.apply(blogStackSubcategoryMaster)).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchSubcategoryById(String subcategoryId) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryId(subcategoryId);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityPojoMapping.apply(blogStackSubcategoryMasterOptional.get())).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchAllSubcategories(Integer page, Integer size) {
        Page<BlogStackSubcategoryMaster> blogStackSubcategoryMasterPage = this.blogStackSubcategoryMasterRepository.findAll(PageRequest.of(page, size));
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackSubcategoryMasterPage);

        if(CollectionUtils.isEmpty(blogStackSubcategoryMasterPage.toList()))
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityListToPojoListMapping.apply(blogStackSubcategoryMasterPage.toList()))
                        .numberOfElements(blogStackSubcategoryMasterPage.getNumberOfElements())
                        .pageSize(blogStackSubcategoryMasterPage.getSize())
                        .totalElements(blogStackSubcategoryMasterPage.getTotalElements())
                        .totalPages(blogStackSubcategoryMasterPage.getTotalPages())
                        .currentPage(blogStackSubcategoryMasterPage.getNumber())
                        .build()).build());
    }

    @Override
    public Optional<ServiceResponseBean> fetchSubcategoryByCategoryId(String categoryId) {
        Optional<List<BlogStackSubcategoryMaster>> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmCategoryId(categoryId);
        LOGGER.warn("BlogStackSubcategoryMasterOptional :: {}", blogStackSubcategoryMasterOptional);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityListToPojoListMapping.apply(blogStackSubcategoryMasterOptional.get())).build());
    }

    @Override
    public Optional<ServiceResponseBean> updateSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryId(subcategoryMasterRequestBean.getSubcategoryId());
        LOGGER.warn("BlogStackSubcategoryMasterOptional :: {}", blogStackSubcategoryMasterOptional);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        subcategoryMasterRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackSubcategoryMaster blogStackSubcategoryMaster = IBlogStackSubcategoryMasterPojoEntityMapper.updateSubcategoryMaster.apply(subcategoryMasterRequestBean, blogStackSubcategoryMasterOptional.get());
        LOGGER.debug("blogStackSubcategoryMaster :: {}", blogStackSubcategoryMaster);

        this.blogStackSubcategoryMasterRepository.saveAndFlush(blogStackSubcategoryMaster);
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityPojoMapping.apply(blogStackSubcategoryMaster)).build());
    }

    @Override
    public Optional<ServiceResponseBean> deleteSubcategory(String subcategoryId) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryId(subcategoryId);
        LOGGER.warn("blogStackSubcategoryMasterOptional :: {}", blogStackSubcategoryMasterOptional);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            throw new BlogstackDataNotFoundException(BlogStackMessageConstants.INSTANCE.DATA_NOT_FOUND);

        blogStackSubcategoryMasterOptional.get().setBsscmStatus(SubcategoryMasterStatusEnum.DELETED.getValue());
        blogStackSubcategoryMasterOptional.get().setBsscmModifiedBy(springApplicationName);
        this.blogStackSubcategoryMasterRepository.saveAndFlush(blogStackSubcategoryMasterOptional.get());
        return Optional.of(ServiceResponseBean.builder().status(Boolean.TRUE).message(BlogStackMessageConstants.INSTANCE.DATA_DELETED).build());
    }
}