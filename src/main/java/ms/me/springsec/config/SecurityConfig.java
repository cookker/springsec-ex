package ms.me.springsec.config;

import lombok.RequiredArgsConstructor;
import ms.me.springsec.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;

    public SecurityExpressionHandler expressionHandler(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);

        return expressionHandler;
    }

    //--- AccessDecisionManager를 사용하여 ROLE 계층 생성.
//    public AccessDecisionManager accessDecisionManager(){
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
//
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy);
//
//        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
//        webExpressionVoter.setExpressionHandler(expressionHandler);
//
//        List<AccessDecisionVoter<?>> voters = Arrays.asList(webExpressionVoter);
//        return new AffirmativeBased(voters);
//    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class);


        http.authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll()
                .mvcMatchers("/", "/info", "/signup/**").permitAll()
                .mvcMatchers("/account/**").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .mvcMatchers("/user").hasRole("USER")
                .anyRequest().authenticated()
//                .accessDecisionManager(accessDecisionManager())
                .expressionHandler(expressionHandler())
                ;
//                .and()
//                .csrf().ignoringAntMatchers("/h2-console/**")
//                .and().headers().frameOptions().sameOrigin();

        http.formLogin()
                .loginPage("/login")
                .permitAll();

        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                .maximumSessions(1)
//                .maxSessionsPreventsLogin()
                ;
        http.rememberMe()
                .userDetailsService(accountService)
                .key("remember-me");

        http.httpBasic();

        http.logout()
                .logoutSuccessUrl("/");

        http.exceptionHandling()
                .accessDeniedPage("/access-denied");

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
     }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("ms").password("{noop}1234").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}1234").roles("ADMIN");
//    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        /*
        * @Autowired로 AccountService를 선언한 이후에
        * auth.userDetailsService(accountService); 이와 같은 코드로 AuthethicationManagerBuilder에 주입시켜야 하지만
        * AccountService가 빈으로 등록되어 있으면 설정하지 않아도 된다.
        * */
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
