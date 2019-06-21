package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dto.SignDto;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.security.JwtProvider;
import com.dev.olivebakery.service.signService.SignService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 로그인 관련 Controller
 */

//TODO 회원정보조회

@RestController
@RequestMapping(value = "/olive/sign")
public class SignController {
    @Autowired
    JwtProvider jwtProvider;

    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @ApiOperation("관리자 회원가입")
    @PostMapping("/admin")
    public String signUpAdmin(@RequestBody SignDto.SignUp signupDto){
        return signService.signUp(signupDto, MemberRole.ADMIN.name());
    }

    @ApiOperation("일반 회원가입")
    @PostMapping("/client")
    public String signUpClient(@RequestBody SignDto.SignUp signupDto){
        return signService.signUp(signupDto, MemberRole.CLIENT.name());
    }

    @ApiOperation("로그인")
    @PostMapping
    public String signIn(@RequestBody SignDto.SignIn signInDto){
        return signService.signIn(signInDto);
    }

    @ApiOperation("회원정보 수정")
    @PutMapping
    public void update(@RequestBody SignDto.SignUp signupDto){
        signService.update(signupDto);
    }

    @ApiOperation("회원정보 삭제")
    @DeleteMapping
    public void delete(@RequestBody SignDto.SignIn signInDto){
        signService.delete(signInDto);
    }

//    @ApiOperation("회원정보 조회")
//    @GetMapping("/userId/{userId:.+}/")
//    public SignDto.MemberDto getMember(@PathVariable String userId){
//        return signService.getMemberInfo(userId);
//    }

    @ApiOperation("회원정보 조회")
    @GetMapping("/check")
    public SignDto.MemberDto getMember(@RequestHeader(name = "Authorization") String bearerToken){
        return signService.getMemberInfo(bearerToken);
    }

    @ApiOperation("전체 회원정보 조회")
    @GetMapping("/members")
    public List<SignDto.MemberDto> getWholeMembers(){
        return signService.getMembersInfo();
    }
}
