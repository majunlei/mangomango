package cc.mangomango.web.controller.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Bean
    public LoginInterceptor newLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(newLoginInterceptor()).addPathPatterns(
                "/editor*", "/newxq*", "/adduser*", "/pic/upload", "/xq/add*", "/xq/upload", "/story/add", "/story/upload", "/user/add", "/we/add");
        super.addInterceptors(registry);
    }
}
