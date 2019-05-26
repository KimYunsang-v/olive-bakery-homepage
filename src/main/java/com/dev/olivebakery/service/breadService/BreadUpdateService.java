package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadImageRepository;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class BreadUpdateService {

    private final BreadRepository breadRepository;
    private final IngredientsRepository ingredientsRepository;
    private final BreadSaveService breadSaveService;
    private final BreadImageRepository breadImageRepository;

    public BreadUpdateService(BreadRepository breadRepository, IngredientsRepository ingredientsRepository,
                              BreadSaveService breadSaveService, BreadImageRepository breadImageRepository){
        this.breadRepository = breadRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.breadSaveService = breadSaveService;
        this.breadImageRepository = breadImageRepository;
    }

    public Bread updateBreadName(BreadDto.BreadUpdateName breadNames){
        Bread bread = breadRepository.findByName(breadNames.getOldName()).get();

        bread.updateName(breadNames.getNewName());

        return breadRepository.save(bread);
    }

    public Bread updateBread(BreadDto.BreadUpdate updateBread){
        Bread bread = breadRepository.findByName(updateBread.getOldName())
                .orElseThrow(() -> new UserDefineException(updateBread.getOldName() + "이란 빵은 존재하지 않습니다."));

        bread.updatePrice(updateBread.getPrice());
        bread.updateDescription(updateBread.getDescription());
        bread.updateDetailDescription(updateBread.getDetailDescription());
        updateIngredients(updateBread.getIngredientsList(), bread);

        return breadRepository.save(bread);
    }

    public void updateIngredients(List<BreadDto.BreadIngredient> breadIngredients, Bread bread) {
//        ingredientsRepository.deleteAllByBread(bread);

        //breadSaveService.saveIngredients(breadIngredients, bread);
    }

    @Transactional
    public String updateBreadImage(MultipartFile image, String breadname) throws IOException {
        Optional<Bread> breadOptional = breadRepository.findByName(breadname);
        Optional<BreadImage> breadImageOptional = breadImageRepository.findByBread(breadOptional.get());
        breadImageOptional.get().changeCurrentStatus(false);

        breadImageRepository.save(breadImageOptional.get());

        return breadSaveService.saveImage(image, breadOptional.get()).getImageUrl();
    }

    public Bread deleteBread(String name){
        Optional<Bread> breadOptional = breadRepository.findByName(name);
        breadOptional.get().deleteBread(true);
        return breadRepository.save(breadOptional.get());
    }

    public Bread updateBreadState(BreadDto.BreadUpdateState breadUpdateState){
        Optional<Bread> breadOptional = breadRepository.findByName(breadUpdateState.getName());
        breadOptional.get().updateBreadState(breadUpdateState.getBreadState());
        return breadRepository.save(breadOptional.get());
    }

    public Bread updateBreadSoldOut(BreadDto.BreadUpdateSoldOut breadUpdateSoldOut){
        Bread bread = breadRepository.findByName(breadUpdateSoldOut.getName()).get();
        bread.updateBreadSoldOut(breadUpdateSoldOut.getIsSoldOut());
        return breadRepository.save(bread);
    }

    public void addBreadIngredients(BreadDto.BreadUpdateIngredients breadUpdateIngredients){
        log.info("find bread name : " + breadUpdateIngredients.getName());
        Bread bread = breadRepository.findByName(breadUpdateIngredients.getName()).get();

        bread.getIngredientsList().forEach(oldIngredients -> {
            breadUpdateIngredients.getIngredientsList().forEach(newIngredients -> {
                if(oldIngredients.getName().equals(newIngredients.getName()) && oldIngredients.getOrigin().equals(newIngredients.getOrigin())){
                    throw new UserDefineException("이미 성분: " + newIngredients.getName() + "  원산지 : " + newIngredients.getOrigin() + " 이(가)존재합니다.");
                }
            });
        });

        breadUpdateIngredients.getIngredientsList().forEach(newIngredientDto -> {
//            Ingredients newIngredients = Ingredients.builder().bread(bread)
//                    .name(newIngredientDto.getName())
//                    .origin(newIngredientDto.getOrigin())
//                    .build();
            Ingredients newIngredients = ingredientsRepository.findByNameAndOrigin(newIngredientDto.getName(), newIngredientDto.getOrigin());
//            ingredientsRepository.save(newIngredients);
            bread.addBreadIngredients(newIngredients);
        });

        breadRepository.save(bread);
    }

    public void deleteBreadIngredients(BreadDto.BreadUpdateIngredients breadUpdateIngredients){
        log.info("find bread name : " + breadUpdateIngredients.getName());
        Bread bread = breadRepository.findByName(breadUpdateIngredients.getName()).get();


        bread.getIngredientsList().forEach(oldIngredients -> {
            breadUpdateIngredients.getIngredientsList().forEach(deleteIngredients -> {
                if(oldIngredients.getName().equals(deleteIngredients.getName()) && oldIngredients.getOrigin().equals(deleteIngredients.getOrigin())){
                    deleteIngredients.setExist(true);
                }
            });
        });

        breadUpdateIngredients.getIngredientsList().forEach(deleteIngredientDto -> {
            if(!deleteIngredientDto.getExist()){
                throw new UserDefineException("해당 성분: " + deleteIngredientDto.getName() + "  원산지 : " + deleteIngredientDto.getOrigin() + " 은(는) 존재히지 않습니다.");
            }
        });

        breadUpdateIngredients.getIngredientsList().forEach(deleteIngredientDto -> {
//            ingredientsRepository.deleteIngredientsByBread(bread, deleteIngredientDto.getName(), deleteIngredientDto.getOrigin());
            Ingredients deleteIngredients = ingredientsRepository.findByNameAndOrigin(deleteIngredientDto.getName(), deleteIngredientDto.getOrigin());
            bread.deleteBreadIngredients(deleteIngredients);
        });

        breadRepository.save(bread);
    }
}
