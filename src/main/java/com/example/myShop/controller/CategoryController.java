package com.example.myShop.controller;

import com.example.myShop.domain.dto.CategoryDto;
import com.example.myShop.domain.dto.CategoryNotIdDto;
import com.example.myShop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.myShop.domain.mapper.CategoryMapper;

import java.util.Optional;

/**
 * @author nafis
 * @since 20.12.2021
 */

@RestController
@RequestMapping(path = "categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

//    @GetMapping()
//    public Array getCategories(){
//        return Optional.of(id)
//                .map(userService::get)
//                .map(userMapper::toDto)
//                .orElseThrow(() -> new RuntimeException("User not found: " + id));
//    }

    @GetMapping("{id}")
    public CategoryDto get(@PathVariable("id") Integer id){
        return Optional.of(id)
                .map(categoryService::get)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @PostMapping()
    public CategoryDto create(@RequestBody CategoryNotIdDto categoryDto){
        return Optional.ofNullable(categoryDto)
                .map(categoryMapper::fromNotIdDto)
                .map(categoryService::create)
                .map(categoryMapper::toDto)
                .orElseThrow();
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable("id") Integer id, @RequestBody CategoryNotIdDto categoryDto){
        return Optional.ofNullable(categoryDto)
                .map(categoryMapper::fromNotIdDto)
                .map(toUpdate -> categoryService.update(id, toUpdate))
                .map(categoryMapper::toDto)
                .orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id){
        categoryService.delete(id);
    }
}
