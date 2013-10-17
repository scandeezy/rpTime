package com.roosterpark.rptime.config;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import com.roosterpark.rptime.HistoryServlet;
import com.roosterpark.rptime.LandingServlet;
import com.roosterpark.rptime.SheetServlet;
import com.roosterpark.rptime.admin.UserServlet;
import com.roosterpark.rptime.admin.CompanyServlet;

/**
 * Per <a href="https://code.google.com/p/google-guice/wiki/GoogleAppEngine">Google Guice documentation<a>.
 */
public class MyServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/rptime").with(LandingServlet.class);
		serve("/rptime/company").with(CompanyServlet.class);
		serve("/rptime/history").with(HistoryServlet.class);
		serve("/rptime/sheet").with(SheetServlet.class);
		serve("/rptime/user").with(UserServlet.class);
	}

	// @Provides
	// public CompanyService companyService(){
	// return CompanySer
	// }

	@Provides
	public DatastoreService datastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}

	@Provides
	public UserService userService() {
		return UserServiceFactory.getUserService();
	}

}
