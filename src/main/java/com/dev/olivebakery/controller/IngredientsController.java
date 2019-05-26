package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.service.IngredientsService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/olive/ingredients")
@RequiredArgsConstructor
public class IngredientsController {

    private final IngredientsService ingredientsService;

    @ApiOperation("빵 성분 추가")
    @PostMapping()
    public ResponseEntity<Ingredients> saveIngredients(@RequestBody BreadDto.BreadIngredient breadIngredient){

        return ResponseEntity.ok(ingredientsService.saveIngredients(breadIngredient));
    }

    @ApiOperation("빵 성분 삭제")
    @DeleteMapping()
    public ResponseEntity<Ingredients> deleteIngredients(@RequestBody BreadDto.BreadIngredient breadIngredient){
        return ResponseEntity.ok(ingredientsService.deleteIngredients(breadIngredient));
    }

    @ApiOperation("빵 성분 여러개 등록")
    @PostMapping("/list")
    public ResponseEntity<List<Ingredients>> saveIngredientsList(@RequestBody List<BreadDto.BreadIngredient> breadIngredientList){
        return ResponseEntity.ok(ingredientsService.saveIngredientsList(breadIngredientList));
    }

    @ApiOperation("빵 성분 리스틑 가져오기")
    @GetMapping()
    public List<BreadDto.BreadIngredient> getIngredientsList(){
        return ingredientsService.getIngredientsList();
    }
}
