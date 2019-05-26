package com.dev.olivebakery.domain.dto;

import com.dev.olivebakery.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SignDto {

    @Getter
    @NoArgsConstructor
    public static class SignIn {
        private String id;
        private String pw;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class SignUp {
        private String email;
        private String pw;
        private String name;
        private String phoneNumber;

        @Builder
        public SignUp(String email, String pw, String name, String phoneNumber){
            this.email = email;
            this.pw = pw;
            this.name = name;
            this.phoneNumber = phoneNumber;
        }

        public Member toEntity(){
            return Member.builder()
                    .email(email)
                    .pw(pw)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .stamp(0)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MemberDto{
        private String email;
        private String name;
        private String phoneNumber;
        private int stamp;

        @Builder
        public MemberDto(String email, String name, String phoneNumber, int stamp) {
            this.email = email;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.stamp = stamp;
        }

        public Member toEntity(){
            return Member.builder()
                    .email(email)
                    .name(name)
                    .phoneNumber(phoneNumber)
                    .stamp(stamp)
                    .build();
        }
    }
}
