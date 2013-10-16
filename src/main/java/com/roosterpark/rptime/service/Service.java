package com.roosterpark.rptime.service;

import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;

public interface Service<T>
{
	public T get(String key) throws EntityNotFoundException;
	public List<T> getPage(int count, int offset);
	public void set(T item);
}
