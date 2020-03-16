package com.naver.pubtrans.itn.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 프로젝트 글로벌 WebConfig
 * @author adtec10
 *
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean
	public DispatcherServlet dispatcherServlet() {

		// 404 에러 처리를 위한 설정 변경
		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}

	/**
	 * Cors
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/*/**")
			.allowedOrigins("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowedHeaders("*")
			.maxAge(3600);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// API document 접근 설정
		registry.addResourceHandler("/static/docs/**").addResourceLocations("classpath:/static/docs/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// API document 접근 설정
		registry.addRedirectViewController("/static/docs/", "/static/docs/index.html");
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

		configurer
			.favorPathExtension(false)
			.defaultContentType(MediaType.APPLICATION_JSON)
			.mediaType("json", MediaType.APPLICATION_JSON)
			.ignoreAcceptHeader(true);

	}
}
