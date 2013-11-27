package com.roosterpark.rptime.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;

import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.GitRepositoryState;
import com.roosterpark.rptime.service.WorkerService;

@Configuration
@Import(AppEngineConfiguration.class)
@PropertySource("classpath:git.properties")
@ComponentScan(basePackages = { "com.roosterpark.rptime" }, excludeFilters = @ComponentScan.Filter({ Configuration.class, Controller.class }))
public class WebModuleConfiguration {

	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public WebModuleConfiguration() {
		LOGGER.info("init");
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

	// @Inject
	@Bean(name = "workerFilter")
	// @DependsOn({ "userService", "workerService" })
	public WorkerFilter workerFilter(final UserService users, final WorkerService workers) {
		LOGGER.debug("Creating worker filter with userService={} and workerService={}", users, workers);
		WorkerFilter filter = new WorkerFilter();
		filter.setUserService(users);
		filter.setWorkerService(workers);
		return filter;
	}
}
