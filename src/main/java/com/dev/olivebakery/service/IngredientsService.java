package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientsService {

    private final IngredientsRepository ingredientsRepository;

    public Ingredients saveIngredients(BreadDto.BreadIngredient ingredientsDto) {
        Ingredients newIngredients = Ingredients.builder().name(ingredientsDto.getName()).origin(ingredientsDto.getOrigin()).build();



        return ingredientsRepository.save(newIngredients);
    }

    public List<Ingredients> saveIngredientsList(List<BreadDto.BreadIngredient> ingredientsDtoList) {      //

        List<Ingredients> ingredientsList = new ArrayList<>();
        ingredientsDtoList.forEach(ingredientDto -> {
            Ingredients newIngredients = Ingredients.builder().name(ingredientDto.getName()).origin(ingredientDto.getOrigin()).build();
            ingredientsList.add(ingredientsRepository.save(newIngredients));
        });

        return ingredientsList;
    }

    public Ingredients deleteIngredients(BreadDto.BreadIngredient ingredientsDto) {
        Ingredients deleteIngredients = ingredientsRepository.findByNameAndOrigin(ingredientsDto.getName(), ingredientsDto.getOrigin());

        ingredientsRepository.delete(deleteIngredients);

        return deleteIngredients;
    }

    public List<BreadDto.BreadIngredient> getIngredientsList(){
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();

        return ingredientListToIngredientsDtoList(ingredientsList);
    }

    private List<BreadDto.BreadIngredient> ingredientListToIngredientsDtoList(List<Ingredients> ingredientsList){
        List<BreadDto.BreadIngredient> ingredientDtoList = new ArrayList<>();

        ingredientsList.forEach(ingredients -> {
            ingredientDtoList.add(BreadDto.BreadIngredient.builder().name(ingredients.getName()).origin(ingredients.getOrigin()).build());
        });

        return ingredientDtoList;
    }
}
