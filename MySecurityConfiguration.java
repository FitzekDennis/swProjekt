package de.othr.sw.DreamSchufa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    @Qualifier("labresources")
    private UserDetailsService userDetailsService;

    @Autowired
    private MySecurityUtilities securityUtilities;

    private BCryptPasswordEncoder passwordEncoder() {
        return securityUtilities.passwordEncoder();
    }

    private static final String[] ALLOW_ACESS_WITHOUT_AUTHENTIFICATION = {
            "/css/**", "/image/**", "/fonts/**", "/", "/login", "/forgotPassword", "/customer/login", "/register“ ", "/restapi/risk", "/restapi/user", "/restapi/customer/**"
            };
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers(ALLOW_ACESS_WITHOUT_AUTHENTIFICATION)
                .permitAll().anyRequest().authenticated();
        http
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/customer/login")
                    .failureUrl("/login?error")
                .and()
                    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/?logout")
                    .deleteCookies("remember-me")
                    .permitAll()
                .and()
                    .rememberMe();
        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }


}

