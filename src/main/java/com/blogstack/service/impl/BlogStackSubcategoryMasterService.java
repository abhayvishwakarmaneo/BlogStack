package com.blogstack.service.impl;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import com.blogstack.beans.responses.PageResponseBean;
import com.blogstack.beans.responses.ServiceResponseBean;
import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.entities.BlogStackSubcategoryMaster;
import com.blogstack.entity.pojo.mapper.IBlogStackSubcategoryMasterEntityPojoMapper;
import com.blogstack.enums.SubcategoryMasterStatusEnum;
import com.blogstack.enums.UuidPrefixEnum;
import com.blogstack.exceptions.BlogstackDataNotFoundException;
import com.blogstack.pojo.entity.mapper.IBlogStackSubcategoryMasterPojoEntityMapper;
import com.blogstack.repository.IBlogStackSubcategoryMasterRepository;
import com.blogstack.service.IBlogStackSubcategoryMasterService;
import com.blogstack.utils.BlogStackCommonUtils;
import com.blogstack.utils.BlogStackSpecificationUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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
    public Mono<?> addSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryIgnoreCase(subcategoryMasterRequestBean.getSubcategory());
        LOGGER.info("BlogStackSubcategoryMasterOptional :: {}", blogStackSubcategoryMasterOptional);

        if(blogStackSubcategoryMasterOptional.isPresent())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message("Subcategory Already Present.").build());

        String subcategoryId = BlogStackCommonUtils.INSTANCE.uniqueIdentifier(UuidPrefixEnum.SUBCATEGORY_ID.getValue());
        LOGGER.info("SubcategoryId :: {}", subcategoryId);

        subcategoryMasterRequestBean.setSubcategoryId(subcategoryId);
        subcategoryMasterRequestBean.setCreatedBy(springApplicationName);

        BlogStackSubcategoryMaster blogStackSubcategoryMaster = this.blogStackSubcategoryMasterRepository.saveAndFlush(this.blogStackSubcategoryMasterPojoEntityMapper.INSTANCE.subcategoryMasterRequestToSubcategoryMasterEntity(subcategoryMasterRequestBean));
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityPojoMapping.apply(blogStackSubcategoryMaster)).build());
    }

    @Override
    public Mono<?> fetchSubcategoryById(String subcategoryId) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryId(subcategoryId);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            return Mono.error(new BlogstackDataNotFoundException("DATA_NOT_FOUND"));

        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityPojoMapping.apply(blogStackSubcategoryMasterOptional.get())).build());
    }

    @Override
    public Mono<?> fetchAllSubcategories(String filterCriteria, String sortCriteria, Integer page, Integer size, String... args) {
        Specification<BlogStackSubcategoryMaster> specification = null;
        if(StringUtils.isNotEmpty(filterCriteria)){
            String entityFilterCriteria = BlogStackSpecificationUtils.INSTANCE.convertFilterCriteriaToEntityFilterCriteria(filterCriteria, "bsscm");
            LOGGER.debug("EntityFilterCriteria :: {}", entityFilterCriteria);
            specification = BlogStackSpecificationUtils.INSTANCE.buildSpecificaton(entityFilterCriteria, new ArrayList<>());
        }

        Sort sort = io.micrometer.common.util.StringUtils.isNotEmpty(sortCriteria) ? BlogStackSpecificationUtils.INSTANCE.convertSortCriteriaToEntitySortCriteria(sortCriteria, "bsscm") : Sort.by("bsscmSeqId").ascending();

        Page<BlogStackSubcategoryMaster> blogStackSubcategoryMasterPage = this.blogStackSubcategoryMasterRepository.findAll(specification, PageRequest.of(page, size, sort));
        LOGGER.debug("BlogStackQuestionMaster :: {}", blogStackSubcategoryMasterPage);

        return CollectionUtils.isNotEmpty(blogStackSubcategoryMasterPage.toList()) ? Mono.just(ServiceResponseBean.builder()
                .status(Boolean.TRUE).data(PageResponseBean.builder().payload(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityListToPojoListMapping.apply(blogStackSubcategoryMasterPage.toList()))
                        .numberOfElements(blogStackSubcategoryMasterPage.getNumberOfElements())
                        .pageSize(blogStackSubcategoryMasterPage.getSize())
                        .totalElements(blogStackSubcategoryMasterPage.getTotalElements())
                        .totalPages(blogStackSubcategoryMasterPage.getTotalPages())
                        .currentPage(blogStackSubcategoryMasterPage.getNumber())
                        .build()).build())
                : Mono.error(new BlogstackDataNotFoundException(BlogStackCommonConstants.INSTANCE.DATA_NOT_FOUND));
    }

    @Override
    public Mono<?> fetchSubcategoryByCategoryId(String categoryId) {
        Optional<List<BlogStackSubcategoryMaster>> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmCategoryId(categoryId);
        if(blogStackSubcategoryMasterOptional.isEmpty())
            return Mono.error(new BlogstackDataNotFoundException(BlogStackCommonConstants.INSTANCE.DATA_NOT_FOUND));

        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityListToPojoListMapping.apply(blogStackSubcategoryMasterOptional.get())).build());
    }

    @Override
    public Mono<?> deleteSubcategory(String subcategoryId) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryId(subcategoryId);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            return Mono.error(new BlogstackDataNotFoundException(BlogStackCommonConstants.INSTANCE.DATA_NOT_FOUND));
        LOGGER.info("blogStackSubcategoryMasterOptional :: {}",blogStackSubcategoryMasterOptional);

        blogStackSubcategoryMasterOptional.get().setBsscmStatus(SubcategoryMasterStatusEnum.DELETED.getValue());
        blogStackSubcategoryMasterOptional.get().setBsscmModifiedBy(springApplicationName);
        this.blogStackSubcategoryMasterRepository.saveAndFlush(blogStackSubcategoryMasterOptional.get());
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).message("Subcategory Deleted").build());
    }

    @Override
    public Mono<?> updateSubcategory(SubcategoryMasterRequestBean subcategoryMasterRequestBean) {
        Optional<BlogStackSubcategoryMaster> blogStackSubcategoryMasterOptional = this.blogStackSubcategoryMasterRepository.findByBsscmSubcategoryId(subcategoryMasterRequestBean.getSubcategoryId());
        LOGGER.info("BlogStackSubcategoryMasterOptional :: {}", blogStackSubcategoryMasterOptional);

        if(blogStackSubcategoryMasterOptional.isEmpty())
            return Mono.just(ServiceResponseBean.builder().status(Boolean.FALSE).message(BlogStackCommonConstants.INSTANCE.DATA_NOT_FOUND).build());

        subcategoryMasterRequestBean.setModifiedBy(this.springApplicationName);
        BlogStackSubcategoryMaster blogStackSubcategoryMaster = this.blogStackSubcategoryMasterPojoEntityMapper.INSTANCE.updateSubcategoryMaster.apply(subcategoryMasterRequestBean, blogStackSubcategoryMasterOptional.get());
        LOGGER.debug("blogStackSubcategoryMaster :: {}", blogStackSubcategoryMaster);

        this.blogStackSubcategoryMasterRepository.saveAndFlush(blogStackSubcategoryMaster);
        return Mono.just(ServiceResponseBean.builder().status(Boolean.TRUE).data(IBlogStackSubcategoryMasterEntityPojoMapper.mapSubcategoryMasterEntityPojoMapping.apply(blogStackSubcategoryMaster)).build());
    }


}
