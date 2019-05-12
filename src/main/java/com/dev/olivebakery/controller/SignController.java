package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dto.SignDto;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.service.SignService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 로그인 관련 Controller
 */

//TODO 회원정보조회

@RestController
@RequestMapping(value = "/olive/sign")
public class SignController {

    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @ApiOperation("회원가입")
    @PostMapping("/signup")
    public String signUp(@RequestBody SignDto.SignUp signupDto){
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

    // TODO
    @ApiOperation("회원정보 조회")
    @GetMapping("/userId/{userId}")
    public SignDto.MemberDto getMember(@PathVariable String userId){
        return signService.getMemberInfo(userId);
    }

    @ApiOperation("전체 회원정보 조회")
    @GetMapping("/members")
    public List<SignDto.MemberDto> getWholeMembers(){
        return signService.getMembersInfo();
    }


    /*public AuthTokenDto login(@RequestBody AuthRequestDto authRequestDto, HttpSession session){
        String username = authRequestDto.getUsername();
        String password = authRequestDto.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        Member member = loginService.getMemberById(username);
        return new AuthTokenDto(member.getId(), member.getRole(), session.getId());

    }
*/
}
