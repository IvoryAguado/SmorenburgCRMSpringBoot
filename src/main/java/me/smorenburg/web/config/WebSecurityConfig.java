package me.smorenburg.web.config;

import me.smorenburg.api.security.JwtAuthenticationEntryPoint;
import me.smorenburg.api.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.XContentTypeOptionsHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;
    @Resource
    private Environment env;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .addHeaderWriter(new XContentTypeOptionsHeaderWriter())
                .addHeaderWriter(new XXssProtectionHeaderWriter())
                .addHeaderWriter(new CacheControlHeadersWriter())
                .addHeaderWriter(new HstsHeaderWriter());
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

//                 allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/*",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.jpg",
                        "/**/*.otf",
                        "/**/*.eot",
                        "/**/*.svg",
                        "/**/*.ttf",
                        "/**/*.woff",
                        "/**/*.woff2",
                        "/**/*.css",
                        "/**/*.scss",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/beans/*").hasAnyRole("ROLE_ADMIN")
                .antMatchers("/profile/*").hasAnyRole("ROLE_ADMIN")
                .antMatchers("/users/*").hasAnyRole("ROLE_ADMIN")
                .antMatchers("/profile/*").hasAnyRole("ROLE_ADMIN")
                .anyRequest().authenticated();

        httpSecurity.formLogin()
                .loginPage("/**")
                .loginProcessingUrl("/api/auth")
                .defaultSuccessUrl("/index.html")
                .failureUrl("/index.html")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll(true)
                .init(httpSecurity);

        RememberMeConfigurer<HttpSecurity> jwtToken = httpSecurity.rememberMe().rememberMeCookieName("jwtToken");


//        httpSecurity.logout().logoutUrl("/signout").deleteCookies("JSESSIONID");

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), BasicAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();


    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }


    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        JpaDialect jpaDialect = new HibernateJpaDialect();
        txManager.setEntityManagerFactory(entityManagerFactory);
        txManager.setJpaDialect(jpaDialect);
        return txManager;
    }
}