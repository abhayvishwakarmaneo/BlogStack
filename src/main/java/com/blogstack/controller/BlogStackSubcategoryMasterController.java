package com.blogstack.controller;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import com.blogstack.service.IBlogStackSubcategoryMasterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Validated
@RequestMapping("/subcategory")
public class BlogStackSubcategoryMasterController {

    @Autowired
    private IBlogStackSubcategoryMasterService iBlogStackSubcategoryMasterService;

    @PostMapping("/")
    public Mono<?> addSubcategory(@Valid @RequestBody SubcategoryMasterRequestBean subcategoryMasterRequestBean){
        return this.iBlogStackSubcategoryMasterService.addSubcategory(subcategoryMasterRequestBean);
    }

    @GetMapping("/")
    public Mono<?> fetchAllSubcategory(@RequestParam(value = "filter_criteria", required = false) String filterCriteria,
                                    @RequestParam(value = "sort_criteria", required = false) String sortCriteria,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "2147483647") Integer size){
        return this.iBlogStackSubcategoryMasterService.fetchAllSubcategories(filterCriteria, sortCriteria, page, size);
    }

    @GetMapping("/{subcategory_id}")
    public Mono<?> fetchSubcategoryById(@PathVariable(value = "subcategory_id") @NotBlank(message = "Subcategory Id can not be empty.") String subcategoryId){
        return this.iBlogStackSubcategoryMasterService.fetchSubcategoryById(subcategoryId);
    }

    @PutMapping("/")
    public Mono<?> updateSubcategory(@Valid @RequestBody SubcategoryMasterRequestBean subcategoryMasterRequestBean){
        return this.iBlogStackSubcategoryMasterService.updateSubcategory(subcategoryMasterRequestBean);
    }

    @DeleteMapping("/{subcategory_id}")
    public Mono<?> deleteSubcategory(@PathVariable(value = "subcategory_id") @NotBlank(message = "Subcategory Id can not be empty.") String subcategoryId){
        return this.iBlogStackSubcategoryMasterService.deleteSubcategory(subcategoryId);
    }

    @GetMapping("/category_id/{category_id}")
    public Mono<?> fetchSubcategoryByICategoryId(@PathVariable(value = "category_id") @NotBlank(message = "category Id can not be empty.") String categoryId){
        return this.iBlogStackSubcategoryMasterService.fetchSubcategoryByCategoryId(categoryId);
    }

}
