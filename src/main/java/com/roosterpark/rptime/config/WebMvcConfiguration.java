package com.roosterpark.rptime.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Spring MVC {@link Configuration}. Extends {@link WebMvcConfigurationSupport}, which provides convenient callbacks.
 */
@Configuration
@ComponentScan(basePackages = { "com.roosterpark.rptime.web" }, includeFilters = @ComponentScan.Filter({ ControllerAdvice.class,
		Controller.class }))
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

	private static final Integer DEFAULT_CACHE_EXPIRY_PERIOD = new Integer(60 * 60 * 24 * 30); // 1 month

	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public WebMvcConfiguration() {
		LOGGER.info("init");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String[] staticResourcesLocations = { "/resources/", "classpath:/META-INF/web-resources/" };
		String[] rootResourceLocation = { "/" };
		registry.addResourceHandler("/resources/**").addResourceLocations(staticResourcesLocations)
				.setCachePeriod(DEFAULT_CACHE_EXPIRY_PERIOD); // '0' to disable caching
		registry.addResourceHandler("/favicon.ico").addResourceLocations(rootResourceLocation);
		registry.addResourceHandler("/index.jsp").addResourceLocations(rootResourceLocation);
		// registry.addResourceHandler("/").addResourceLocations(rootResourceLocation);
	}

	/**
	 * Configure Spring to use the a common {@link ObjectMapper} for HTTP message conversion.
	 * <p>
	 * If using RestTemplate, be sure to register the {@link HttpMessageConverter}s with {@code RestTemplate} per <a
	 * href="http://stackoverflow.com/a/9371244/237225">this link</a>.
	 */
	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		final MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
		jacksonConverter.setObjectMapper(objectMapper());
		LOGGER.debug("add a default Jackson2 converter: {}", jacksonConverter);
		converters.add(jacksonConverter);
		// restTemplate().setMessageConverters(jacksonConverters);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index.jsp");
		// registry.addViewController("/login"); //TODO
		// registry.addViewController("/error").setViewName("error.jsp"); // TODO
	}

	//
	// @Bean definitions
	//

	@Bean
	public ObjectMapper objectMapper() {
		Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
		bean.setIndentOutput(true);
		bean.setSimpleDateFormat("yyyy-MM-dd");
		bean.afterPropertiesSet();
		ObjectMapper mapper = bean.getObject();

		mapper.getSerializationConfig()//
				.with(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)//
				.with(SerializationFeature.INDENT_OUTPUT)//
				.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)//
				.shouldSortPropertiesAlphabetically();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		mapper.registerModule(new JodaModule());

		return mapper;
	}

}
