package com.dev.olivebakery.service;


import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Log
public class BreadUpdateServiceTest {

    @Autowired
    IngredientsRepository ingredientsRepository;

    @Autowired
    BreadRepository breadRepository;

    @Test
    public void ingredientsUpdateTest(){

//        Optional<Bread> bread = breadRepository.findByName("식빵2");

        Ingredients byNameAndOrigin = ingredientsRepository.findByNameAndOrigin("밀가루", "한국");
//        List<Bread> allByIngredientsList = breadRepository.findAllByIngredientsList(byNameAndOrigin);

//        for (Bread bread : allByIngredientsList) {
//            log.info("------------  " + bread.getName());
//        }

    }
}