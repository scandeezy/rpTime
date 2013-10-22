package com.roosterpark.rptime.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Spring MVC {@link Configuration}. Extends {@link WebMvcConfigurationSupport}, which provides convenient callbacks.
 */
@Configuration
@Import(DatastoreConfiguration.class)
@ComponentScan(basePackages = { "com.roosterpark.rptime" }, includeFilters = @ComponentScan.Filter({ Controller.class }), excludeFilters = @ComponentScan.Filter({ Configuration.class }))
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public WebMvcConfiguration() {
		LOGGER.info("init");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String[] staticResourcesLocations = { "/resources/", "classpath:/META-INF/web-resources/" };
		String[] rootResourceLocation = { "/" };
		registry.addResourceHandler("/resources/**").addResourceLocations(staticResourcesLocations);
		// .setCachePeriod(0); // to disable caching
		registry.addResourceHandler("/favicon.ico").addResourceLocations(rootResourceLocation);
		registry.addResourceHandler("/index.jsp").addResourceLocations(rootResourceLocation);
		registry.addResourceHandler("/").addResourceLocations(rootResourceLocation);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		mapper.getSerializationConfig()//
				.with(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)//
				.with(SerializationFeature.INDENT_OUTPUT)//
				.shouldSortPropertiesAlphabetically();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper;
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
		// registry.addViewController("/error"); //TODO
	}

}
