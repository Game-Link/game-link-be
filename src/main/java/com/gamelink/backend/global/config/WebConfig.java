package com.gamelink.backend.global.config;

import com.gamelink.backend.global.interceptor.VoidSuccessResponseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.cors}")
    private String corsList;

    private final VoidSuccessResponseInterceptor vscInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(vscInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(parseCorsList(corsList))
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    private static String[] parseCorsList(String corsList) {
        return Arrays.stream(corsList.split(","))
                .map(cors -> cors.strip())
                .toArray(String[]::new);
    }

    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> filter = new FilterRegistrationBean<>();
        filter.addUrlPatterns("/manage/*");
        filter.setFilter(new OpenEntityManagerInViewFilter());
        return filter;
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
