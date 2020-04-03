package com.example.demo.config;

import java.awt.RenderingHints.Key;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.graph.Graph;

public class ServerConfig {
	public void ttr() {
		LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
				 .maximumSize(1000)
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.removalListener(MY_LISTENER)
				 
				       .build(
				 
				           new CacheLoader<Key, Graph>() {
				 
				             public Graph load(Key key) throws AnyException {
				 
				               return createExpensiveGraph(key);
				 
				             }
				 
				           });
	}
}
