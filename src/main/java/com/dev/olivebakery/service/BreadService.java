package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadRepository;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by YoungMan on 2019-02-12.
 */

@Service @Log4j2
public class BreadService {

    private final BreadRepository breadRepository;

    public BreadService(BreadRepository breadRepository) {
        this.breadRepository = breadRepository;
    }

    public Bread findByName(String breadName) {
        return breadRepository.findByName(breadName)
                .orElseThrow(() -> new UserDefineException("해당 이름의 빵이 없습니다."));
    }

    public List<Bread> findsByNames(List<String> breadNames) {
        return breadRepository.findByNameIn(breadNames);
    }

    public int getFinalPrice(LinkedHashMap<String, Integer> breadInfos) {
        List<String> breadNames = new ArrayList<>(breadInfos.keySet());
        List<Integer> counts = new ArrayList<>(breadInfos.values());
        List<Bread> breads = findsByNames(breadNames);
        int finalPrice = 0;

        for (int i = 0; i < breadInfos.size(); i++) {
            finalPrice = finalPrice + (breads.get(i).getPrice() * counts.get(i));
        }

        return finalPrice;
    }

    public List<BreadDto.BreadGetAll> getBreadByDay(DayType day){
        List<Bread> breadList = breadRepository.findByDays(day);
        List<BreadDto.BreadGetAll> breadGetAll = new ArrayList<>();
        breadList.forEach(bread -> {
            boolean isSoldOut = false;
            if(bread.getSoldOut() != null)
                isSoldOut = bread.getSoldOut().getDate().isEqual(LocalDate.now());

            breadGetAll.add(
                BreadDto.BreadGetAll.builder()
                        .name(bread.getName())
                        .price(bread.getPrice())
                        .description(bread.getDescription())
                        .soldOut(isSoldOut)
                        .breadState(bread.getState())
                        .build());

        });

        return breadGetAll;
    }

    public BreadDto.BreadGetDetail getBreadDetails(String name){
        Bread bread = breadRepository.findByName(name)
                .orElseThrow(() -> new UserDefineException(name + "이란 빵은 존재하지 않습니다."));
        boolean isSoldOut = false;
        if(bread.getSoldOut() != null)
            isSoldOut = bread.getSoldOut().getDate().isEqual(LocalDate.now());

        List<BreadDto.BreadIngredient> ingredientList = new ArrayList<>();
        bread.getIngredients().forEach(ingredient -> ingredientList.add(
                BreadDto.BreadIngredient.builder()
                        .ingredient(ingredient.getName())
                        .origin(ingredient.getOrigin())
                        .build()
        ));

        return BreadDto.BreadGetDetail.builder()
                .name(bread.getName())
                .price(bread.getPrice())
                .detailDescription(bread.getDetailDescription())
                .ingredientsList(ingredientList)
                .soldOut(isSoldOut)
                .breadState(bread.getState())
                .build();
    }

    public void updateBread(BreadDto.BreadSave updateBread){
        Bread bread = breadRepository.findByName(updateBread.getName())
                .orElseThrow(() -> new UserDefineException(updateBread.getName() + "이란 빵은 존재하지 않습니다."));

    }

    public void saveBread(BreadDto.BreadSave breadSave){

        log.info(breadSave.getName());
//        BreadImage breadImage ;
//        if(breadSave.getBreadImage() != null){
//            breadImage = saveImage(breadSave.getBreadImage());
//        }  else {
//            breadImage = new BreadImage();
//        }
//
//        log.info(breadImage.getImageName());
    }

    public BreadImage saveImage(MultipartFile imageFile) throws IOException{
//        String sourceFileName = imageFile.getOriginalFilename();
//        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
//
//        File destinationFile;
//        String destinationFileName;
//        do {
//            //destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
//            destinationFileName = sourceFileNameExtension;
//            destinationFile = new File("C:\\Users\\Kimyunsang\\Desktop\\spring\\imageTest" + destinationFileName);
//        } while (destinationFile.exists());
//
//        destinationFile.getParentFile().mkdirs();

        String fileName = imageFile.getOriginalFilename();
        File destinationFile = new File("C:\\Users\\Kimyunsang\\Desktop\\spring\\imageTest"+ File.separator + fileName);

        imageFile.transferTo(destinationFile);

        BreadImage breadImage = BreadImage.builder()
                .imageName(imageFile.getOriginalFilename())
                .imageSize(imageFile.getSize())
                .imageType(imageFile.getContentType())
                .imageUrl("http://localhost:8080/Users\\Kimyunsang\\Desktop\\spring\\imageTest" + fileName)
                .build();

        return breadImage;
    }
}
