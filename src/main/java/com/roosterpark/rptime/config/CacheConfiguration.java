package com.roosterpark.rptime.config;

import java.io.IOException;

import net.sf.ehcache.CacheException;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Cache {@link Configuration} to enable {@link Cacheable} {@link CachePut} {@link CacheEvict} -annotated methods
 * 
 * @author jjzabkar
 * 
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

	/** static cache names. See ehcache.xml for configurations. */
	public static final String CLIENT_SERVICE_GET_AVAILABLE_FOR_WORKER_INTERVAL_DAYS_CACHE_NAME = "clientService.getAvailableForWorkerIntervalDays.cache";
	public static final String CLIENT_SERVICE_GET_SELECTED_IDS_FOR_TIME_SHEET_DAYS_CACHE_NAME = "clientService.getSelectedIdsForTimeSheetDays.cache";
	public static final String CONTRACT_SERVICE_GET_ACTIVE_CONTRACTS_FOR_WORKER_CACHE_NAME = "contractService.getActiveContractsForWorker.cache";
	public static final String WORKER_SERVICE_GET_ALL_CACHE_NAME = "workerService.getAll.cache";
	public static final String WORKER_SERVICE_GET_BY_ID_CACHE_NAME = "workerService.getById.cache";
	public static final String WORKER_SERVICE_GET_BY_USER_CACHE_NAME = "workerService.getByUser.cache";

	@Bean
	public CacheManager cacheManager() {
		final EhCacheCacheManager result = new EhCacheCacheManager();
		result.setCacheManager(ehCacheManager());
		return result;
	}

	@Bean(destroyMethod = "shutdown")
	public net.sf.ehcache.CacheManager ehCacheManager() {
		final EhCacheManagerFactoryBean result = new EhCacheManagerFactoryBean();
		result.setCacheManagerName("RpTimeCache");
		result.setConfigLocation(new ClassPathResource("META-INF/spring/cache/ehcache.xml"));
		result.setShared(true);
		try {
			result.afterPropertiesSet();
		} catch (CacheException e) {
			throw e;
		} catch (IOException e) {
			throw new BeanCreationException(e.getMessage());
		}
		return result.getObject();
	}

}
