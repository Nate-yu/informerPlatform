package cn.edu.hut.config;

import cn.edu.hut.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Nate yu
 * @date 2022/5/3 - 上午 12:08
 * @Description TODO
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                // 拦截所有请求，通过判断token是否合法来决定是否需要登录
                .addPathPatterns("/**")
                .excludePathPatterns("/login/login", "/**/export");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}
