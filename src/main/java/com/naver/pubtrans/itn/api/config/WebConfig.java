package com.naver.pubtrans.itn.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.naver.pubtrans.itn.api.config.converter.PubTransTypeConverter;
import com.naver.pubtrans.itn.api.config.converter.TaskDataSourceTypeConverter;
import com.naver.pubtrans.itn.api.config.converter.TaskStatusTypeConverter;
import com.naver.pubtrans.itn.api.config.converter.TaskTypeConverter;

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
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
				HttpMethod.DELETE.name())
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

	@Override
    public void addFormatters(FormatterRegistry registry) {

		// GET Parameter를 통해 전송되는 Enum 값들을 각 타입에 맞게 변환한다
        registry.addConverter(new PubTransTypeConverter());
        registry.addConverter(new TaskDataSourceTypeConverter());
        registry.addConverter(new TaskStatusTypeConverter());
        registry.addConverter(new TaskTypeConverter());
    }
}
