package com.tagsoft.registry.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;

//    @Value("${spring.queries.customer-query}")
    private String usersQuery;
//    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder,
                          DataSource dataSource,
                          @Value("${spring.queries.customer-query}") String usersQuery,
                          @Value("${spring.queries.roles-query}") String rolesQuery) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dataSource = dataSource;
        this.usersQuery = usersQuery;
        this.rolesQuery = rolesQuery;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers( "/login", "/registration").permitAll()
                .antMatchers("/free-access").permitAll()
                .antMatchers("/product-images-uploads/**").hasAuthority("ADMIN")
//                .anyRequest()
//                .authenticated()
                .antMatchers("/index/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                // controller method GET mapped "/index", and THE ADDRESS WILL BE http://localhost:8082/store/index
                .defaultSuccessUrl("/index")
                // controller method POST mapped "/index". But in the same time it must be
                // <form th:action="@{/login}" method="POST"> in the login.html, and THE ADDRESS WILL BE http://localhost:8082/store/login
//                .loginProcessingUrl("/index")
                .usernameParameter("login")
                .passwordParameter("password")
                .and()
                .logout()
                // to use POST "/logout":
//                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
//  let Spring knows that some static resources can be served skipping the antMatchers defined
                .ignoring()
                .antMatchers("/bootstrap3.3.7/**", "/css/**", "/js/**", "/images/**");
    }
}
