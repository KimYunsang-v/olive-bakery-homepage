package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.service.breadService.BreadGetService;
import com.dev.olivebakery.service.breadService.BreadSaveService;
import com.dev.olivebakery.service.breadService.BreadUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/olive/bread")
@RequiredArgsConstructor
@Log4j2
public class BreadController {

    private final BreadSaveService breadSaveService;
    private final BreadGetService breadGetService;
    private final BreadUpdateService breadUpdateService;


    private static final Logger logger = LoggerFactory.getLogger(BreadController.class);

//    public BreadController(BreadSaveService breadSaveService, BreadGetService breadGetService,
//                           BreadUpdateService breadUpdateService){
//        this.breadSaveService = breadSaveService;
//        this.breadGetService = breadGetService;
//        this.breadUpdateService = breadUpdateService;
//    }

//    @ApiOperation("모든 빵 정보 가져오기")
//    @GetMapping()
//    public List<BreadDto.BreadGetAll> getAllBread(){
//
//        return breadGetService.getAllBread();
//    }

    @ApiOperation("모든 빵 정보 가져오기")
    @GetMapping()
    public ResponseEntity<List<BreadDto.BreadGetAll>> getAllBread(){

        return ResponseEntity.ok(breadGetService.getAllBread());

    }

    @ApiOperation("요일별 빵 정보 가져오기")
    @GetMapping("/day/{day}")
    public ResponseEntity<List<BreadDto.BreadGetAll>> getBread(@PathVariable String day){
        return ResponseEntity.ok(breadGetService.getBreadByDay(day));
    }

    @ApiOperation("빵 상세정보 가져오기")
    @GetMapping("/name/{name}")
    public ResponseEntity<BreadDto.BreadGetDetail> getDetail(@PathVariable String name){
        return ResponseEntity.ok(breadGetService.getBreadDetails(name));
    }

    @ApiOperation("빵, 이미지 같이 저장")
    @PostMapping()
    public ResponseEntity<Bread> saveBreadAndImage(@RequestPart MultipartFile file,
                                                        @RequestParam String json) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        BreadDto.BreadSave breadSave = objectMapper.readValue(json, BreadDto.BreadSave.class);
        Bread bread = breadSaveService.saveBread(breadSave, file);

        return ResponseEntity.ok(bread);
    }

    @ApiOperation("빵, 이미지 같이 저장 모델 명세")
    @PostMapping("/test")
    public void saveBreadAndImageTest( BreadDto.BreadSave breadSave) {
    }

    @ApiOperation("빵 이름 수정")
    @PutMapping(value = "/name")
    public ResponseEntity<Bread> updateBreadName(@RequestBody BreadDto.BreadUpdateName bread){
        return ResponseEntity.ok(breadUpdateService.updateBreadName(bread));
    }

//    @ApiOperation("빵 정보 수정")
//    @PutMapping()
//    public ResponseEntity<Bread> updateBread(@RequestBody BreadDto.BreadUpdate bread){
//        return ResponseEntity.ok(breadUpdateService.updateBread(bread));
//    }

    @ApiOperation("빵 정보 수정")
    @PutMapping()
    public ResponseEntity<Bread> updateBread(@RequestPart(name = "file", required = false) MultipartFile file,
                                             @RequestParam String json) throws Exception {
//        if(file == null){
//            return ResponseEntity.ok(breadUpdateService.updateBread(null, json));
//        }
        return ResponseEntity.ok(breadUpdateService.updateBread(file, json));
    }

//    @ApiOperation("빵 사진 수정")
//    @PutMapping("/image")
//    public ResponseEntity<BreadImage> updateBreadIamge(@RequestPart MultipartFile file,
//                                                       @RequestParam String breadName) throws Exception{
//        return ResponseEntity.ok(breadUpdateService.updateBreadImage(file, breadName));
//    }

    @ApiOperation("빵 상태 변경")
    @PutMapping("/state")
    public ResponseEntity<Bread> changeBreadState(@PathVariable BreadDto.BreadUpdateState breadUpdateState){
        return ResponseEntity.ok(breadUpdateService.updateBreadState(breadUpdateState));
    }

    @ApiOperation("빵 매진 상태 변경")
    @PutMapping("/sold_out")
    public ResponseEntity<Bread> changeBreadSoldOut(@PathVariable BreadDto.BreadUpdateSoldOut breadUpdateSoldOut){
        return ResponseEntity.ok(breadUpdateService.updateBreadSoldOut(breadUpdateSoldOut));
    }

    @ApiOperation("빵 삭제")
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Bread> deleteBread(@PathVariable String name){
        return ResponseEntity.ok(breadUpdateService.deleteBread(name));
    }

    @GetMapping(value = "/image/{image}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String image) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(breadGetService.getImageResource(image));
    }

//    @ApiOperation("빵 정보에 성분 추가")
//    @PutMapping("/ingredients/add")
//    public ResponseEntity<HttpStatus> addIngredient(@RequestBody BreadDto.BreadUpdateIngredients breadUpdateIngredients){
//
//        logger.info(breadUpdateIngredients.getIngredientsList().get(0).getName());
//
//        breadUpdateService.addBreadIngredients(breadUpdateIngredients);
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @ApiOperation("빵 정보에 성분 삭제")
//    @DeleteMapping("/ingredients/delete")
//    public ResponseEntity<HttpStatus> deleteIngredient(@RequestBody BreadDto.BreadUpdateIngredients breadUpdateIngredients){
//
//        logger.info(breadUpdateIngredients.getIngredientsList().get(0).getName());
//
//        breadUpdateService.deleteBreadIngredients(breadUpdateIngredients);
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }


}