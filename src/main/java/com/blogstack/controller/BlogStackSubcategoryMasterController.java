package com.blogstack.controller;

import com.blogstack.beans.requests.SubcategoryMasterRequestBean;
import com.blogstack.service.IBlogStackSubcategoryMasterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/subcategory")
@AllArgsConstructor
public class BlogStackSubcategoryMasterController {

    private final IBlogStackSubcategoryMasterService blogStackSubcategoryMasterService;

    @PostMapping("/")
    public ResponseEntity<?> addSubcategory(@Valid @RequestBody SubcategoryMasterRequestBean subcategoryMasterRequestBean){
        return ResponseEntity.ok(this.blogStackSubcategoryMasterService.addSubcategory(subcategoryMasterRequestBean));
    }

    @GetMapping("/")
    public ResponseEntity<?> fetchAllSubcategory(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity.ok(this.blogStackSubcategoryMasterService.fetchAllSubcategories(page, size));
    }

    @GetMapping("/{subcategory_id}")
    public ResponseEntity<?> fetchSubcategoryById(@PathVariable(value = "subcategory_id") @NotBlank(message = "Subcategory Id can not be empty.") String subcategoryId){
        return ResponseEntity.ok(this.blogStackSubcategoryMasterService.fetchSubcategoryById(subcategoryId));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateSubcategory(@Valid @RequestBody SubcategoryMasterRequestBean subcategoryMasterRequestBean){
        return ResponseEntity.ok(this.blogStackSubcategoryMasterService.updateSubcategory(subcategoryMasterRequestBean));
    }

    @DeleteMapping("/{subcategory_id}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable(value = "subcategory_id") @NotBlank(message = "Subcategory Id can not be empty.") String subcategoryId){
        return ResponseEntity.ok(this.blogStackSubcategoryMasterService.deleteSubcategory(subcategoryId));
    }

    @GetMapping("/category_id/{category_id}")
    public ResponseEntity<?> fetchSubcategoryByICategoryId(@PathVariable(value = "category_id") @NotBlank(message = "category Id can not be empty.") String categoryId){
        return ResponseEntity.ok(this.blogStackSubcategoryMasterService.fetchSubcategoryByCategoryId(categoryId));
    }
}
