/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config;

import com.supermap.gaf.authentication.config.authc.CustomUserDetailsServiceImpl;
import com.supermap.gaf.authentication.config.authc.jwtorsession.JwtOrSessionAuthenticationFailureHandler;
import com.supermap.gaf.authentication.config.authc.jwtorsession.JwtOrSessionAuthenticationProvider;
import com.supermap.gaf.authentication.config.authc.jwtorsession.JwtOrSessionAuthenticationSuccessHandler;
import com.supermap.gaf.authentication.config.authc.jwtorsession.JwtOrSessionLoginConfiguration;
import com.supermap.gaf.authentication.config.authc.oauth2.Oauth2AuthorizeFilter;
import com.supermap.gaf.authentication.config.authc.usernamepassword.UsernamePasswordAuthenticationFailureHandler;
import com.supermap.gaf.authentication.config.authc.usernamepassword.UsernamePasswordAuthenticationSuccessHandler;
import com.supermap.gaf.authentication.config.authc.usernamepassword.UsernamePasswordLoginConfiguration;
import com.supermap.gaf.authentication.config.authc.usernamepassword.UsernamePasswordUserAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.Arrays;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_URL;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public UsernamePasswordUserAuthenticationProvider customUserAuthenticationProvider(){
        return new UsernamePasswordUserAuthenticationProvider();
    }

    @Bean
    public JwtOrSessionAuthenticationProvider jwtOrSessionAuthenticationProvider(){
        JwtOrSessionAuthenticationProvider provider = new JwtOrSessionAuthenticationProvider();
        provider.setCustomUserDetailsServiceImpl(customUserDetailsService());
        return provider;
    }

    @Bean
    public UsernamePasswordAuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new UsernamePasswordAuthenticationSuccessHandler();
    }
    @Bean
    public UsernamePasswordAuthenticationFailureHandler customAuthenticationFailureHandler(){
        return new UsernamePasswordAuthenticationFailureHandler();
    }

    @Bean
    public Oauth2AuthorizeFilter oauth2AuthorizeFilter() throws Exception {
        Oauth2AuthorizeFilter oauth2AuthorizeFilter = new Oauth2AuthorizeFilter();
        oauth2AuthorizeFilter.setAuthenticationManager(authenticationManagerBean());
        return oauth2AuthorizeFilter;
    }

    @Bean
    public CustomUserDetailsServiceImpl customUserDetailsService(){
        return new CustomUserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customUserAuthenticationProvider());
        auth.authenticationProvider(jwtOrSessionAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                    new Header("Access-control-Allow-Origin","*"),
                    new Header("Access-Control-Expose-Headers","Authorization"))))
                .and()
                //用户名密码登录逻辑
                .apply(new UsernamePasswordLoginConfiguration<>())
                            .loginSuccessHandler(customAuthenticationSuccessHandler())
                            .loginFailureHandler(customAuthenticationFailureHandler())
                .and()
                //jwt或session登录逻辑
                .apply(new JwtOrSessionLoginConfiguration<>())
                            .loginSuccessHandler(new JwtOrSessionAuthenticationSuccessHandler())
                            .loginFailureHandler(new JwtOrSessionAuthenticationFailureHandler())
                .and()
                //auth异常跳转到login
                //配置第三方oauth2登录地址
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .csrf().disable();
    }

}
