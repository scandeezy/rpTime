package com.roosterpark.rptime.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

@Configuration
public class AppEngineConfiguration {
	Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static final String BASE_PACKAGE = "com.roosterpark.rptime";

	public AppEngineConfiguration() {
		LOGGER.trace("init");
	}

	@Bean
	public ObjectifyFactory objectifyFactory() throws ClassNotFoundException {
		LOGGER.debug("init ObjectifyFactory");
		ObjectifyFactory result = ObjectifyService.factory();

		LOGGER.debug("registering JodaTimeTranslators with Objectify");
		// TranslatorRegistry Javadoc: "You must add translators *before* registering entity pojo classes."
		// enables Joda time serialization for Objectify/datastore.
		// per:
		// https://code.google.com/p/objectify-appengine/source/browse/src/com/googlecode/objectify/impl/translate/opt/joda/JodaTimeTranslators.java?r=2d48a85eae3a679c0dc0d01631de99f3b4775b29
		JodaTimeTranslators.add(result);

		LOGGER.info("Finding classes with @Entity* annotations in base package '{}' to register on ObjectifyFactory", BASE_PACKAGE);
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(EntitySubclass.class));

		for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {
			LOGGER.debug("Registering @Entity {} with Objectify TranslatorRegistry", bd.getBeanClassName());
			result.register(Class.forName(bd.getBeanClassName()));
		}

		return result;
	}

	@Bean
	public DatastoreService datastoreService() {
		LOGGER.debug("init DataStoreService");
		return DatastoreServiceFactory.getDatastoreService();
	}

	@Bean(name = "userService")
	public UserService userService() {
		LOGGER.debug("init UserService");
		return UserServiceFactory.getUserService();
	}
}
