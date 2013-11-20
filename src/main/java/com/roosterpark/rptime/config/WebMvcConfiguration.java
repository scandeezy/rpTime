package com.roosterpark.rptime.config;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.GitRepositoryState;
import com.roosterpark.rptime.service.WorkerService;

/**
 * Spring MVC {@link Configuration}. Extends {@link WebMvcConfigurationSupport}, which provides convenient callbacks.
 */
@Configuration
@Import(AppEngineConfiguration.class)
@PropertySource("classpath:git.properties")
@ComponentScan(basePackages = { "com.roosterpark.rptime" }, includeFilters = @ComponentScan.Filter({ Controller.class }), excludeFilters = @ComponentScan.Filter({ Configuration.class }))
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	private UserService userService;

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
		// registry.addViewController("/error"); //TODO
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

	@Bean(name = "propertySourcesPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	@DependsOn("propertySourcesPlaceholderConfigurer")
	public GitRepositoryState gitRepositoryState() {
		return new GitRepositoryState();
	}

	@Inject
	@Bean(name = "workerFilter")
	public WorkerFilter workerFilter(UserService users, WorkerService workers) {
		LOGGER.debug("Creating worker filter with services {} and {}", users, workers);
		WorkerFilter filter = new WorkerFilter();
		filter.setUserService(users);
		filter.setWorkerService(workers);
		return filter;
	}
}
