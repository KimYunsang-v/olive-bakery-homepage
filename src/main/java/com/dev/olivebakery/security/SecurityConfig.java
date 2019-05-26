package com.dev.olivebakery.security;

import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final HttpAuthenticationEntryPoint httpAuthenticationEntryPoint;
    private final AuthenticationTokenFilter authenticationTokenFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final LogoutSuccessHandlerCustom logoutSuccessHandlerCustom;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, LogoutSuccessHandlerCustom logoutSuccessHandlerCustom, HttpAuthenticationEntryPoint httpAuthenticationEntryPoint, AuthenticationTokenFilter authenticationTokenFilter, AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.logoutSuccessHandlerCustom = logoutSuccessHandlerCustom;
        this.httpAuthenticationEntryPoint = httpAuthenticationEntryPoint;
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .httpBasic().disable()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                    .antMatchers(HttpMethod.POST, "/olive/sign/client").permitAll()
//                    .antMatchers( HttpMethod.POST,"/olive/sign").permitAll()
//                    .antMatchers("/olive/sign/admin").hasRole(MemberRole.ADMIN.name())
//                    .antMatchers(HttpMethod.PUT,"/olive/sign").hasRole(MemberRole.CLIENT.name())
//                    .antMatchers(HttpMethod.DELETE,"/olive/sign").hasRole(MemberRole.CLIENT.name())
//
//                //TODO("여기에 위 방식처럼 제한하고 싶은 url들 제한좀 해줘")
//                    .anyRequest().authenticated()
//                .and()
//                    .exceptionHandling().authenticationEntryPoint(httpAuthenticationEntryPoint).accessDeniedHandler(accessDeniedHandler)
//                .and()
//                    .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                    .logout().logoutUrl("/olive/logout").logoutSuccessHandler(logoutSuccessHandlerCustom)
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }


    @Override
    public void configure(WebSecurity web) {
//        web.ignoring()
//                .antMatchers(HttpMethod.POST, "/olive/sign/client")
//                .antMatchers(HttpMethod.POST, "/olive/sign")
//                .antMatchers("/v2/api-docs", "/configuration/ui",
//                                "/swagger-resources", "/configuration/security",
//                                "/swagger-ui.html", "/webjars/**","/swagger/**");
        web.ignoring().antMatchers("/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
