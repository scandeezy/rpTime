package com.roosterpark.rptime.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Per <a href="https://code.google.com/p/google-guice/wiki/GoogleAppEngine">Google Guice documentation<a>.
 */
public class MyGuiceServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new MyServletModule());
	}
}
