package com.polaroid.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration	//스프링부트의 빈 설정파일
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		//정적 리소스 자원 경로 추가
        registry
                .addResourceHandler( "img/**")
                .addResourceLocations("classpath:/static/img/");

        registry
                .addResourceHandler("css/**")
                .addResourceLocations("classpath:/static/css/");
        
        registry
		        .addResourceHandler("js/**")
		        .addResourceLocations("classpath:/static/js/");
		
        registry
	            .addResourceHandler("vendor/**")
	            .addResourceLocations("classpath:/static/vendor/");
		
        //업로드 파일처리
        registry
        		.addResourceHandler("/upload/**")
        		.addResourceLocations("file:/usr/local/tomcat/upload");
        
	}
}
