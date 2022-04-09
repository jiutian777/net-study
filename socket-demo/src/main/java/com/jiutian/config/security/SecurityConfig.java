package com.jiutian.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

//开启WebSecurity模式
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private SecurityUserDetailsService userDetailsService;

    //注入数据源
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private DefaultAccessDeniedHandler defaultAccessDeniedHandler;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    //配置对象
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定使用自定义查询用户信息来完成身份认证
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 先认证，认证成功后才授权
    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf保护
        http.csrf().disable();

        // 退出
        http.logout().logoutUrl("/logout").
                logoutSuccessUrl("/user/logout").permitAll();

        // 开启表单验证
        http.formLogin().loginPage("/toLogin").usernameParameter("username")
                .passwordParameter("password")
                // 登录访问路径
                .loginProcessingUrl("/user/login")
                // 认证成功自定义处理器
                .successHandler(myAuthenticationSuccessHandler)
                // 认证失败自定义处理器
                .failureHandler(myAuthenticationFailureHandler)
                // 登录认证成功后默认转跳的路径
                // .successForwardUrl("/user/loginSuccess")
                // .defaultSuccessUrl("/index").permitAll()
                // 登录认证失败跳转页面
                // .failureUrl("/login.html")
                .and().authorizeRequests()
                .antMatchers("/toLogin", "/toRegist", "/user/login", "/user/regist")
                .permitAll()
                .antMatchers("/", "/index", "/index.html")
                .hasAnyAuthority("ADMIN", "USER")

                .anyRequest().authenticated()
                .and().exceptionHandling()
                // 配置匿名用户访问无权限处理
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                // 配置认证成功但没有权限访问跳转自定义页面
                // .accessDeniedPage("/login.html")
                // 配置认证成功但没有权限访问自定义处理器
                .accessDeniedHandler(defaultAccessDeniedHandler)

                .and().rememberMe().rememberMeParameter("remember")
                .tokenRepository(persistentTokenRepository())
                // 设置有效时长，单位秒
                .tokenValiditySeconds(60)
                .userDetailsService(userDetailsService)

                .and().sessionManagement()
                // 默认的session创建策略
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                // 默认的session保护方式
                .sessionFixation().migrateSession()
                // 指定非法session跳向的url
                // .invalidSessionUrl("/toLogin")
                // 指定最大的session并发数量，踢掉已经登录的
                .maximumSessions(1)
                // 指定过期session跳向的url
                // .expiredUrl("/toLogin")
                // 禁止同一用户再次登录
                // .maxSessionsPreventsLogin(true)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy());
    }

    /** 放行静态资源 */
    @Override
    public void configure(WebSecurity web) {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers(
                "/css/**","/js/**","/img/**","/images/**","/fonts/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
