package de.igormukhin.examples;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCaching
public class ApplicationConfig {
	
    @Bean
    public CacheManager cacheManager() {
    	// This standard implementation offered by spring-context-support
    	// doesn't provide the opportunity to fine tune each cache
    	// but creates caches on the fly which actually should be sufficient in many cases.
    	//return new GuavaCacheManager();
    	
    	// configure the caches as you need it 
    	Cache dataCache = new GuavaCache("dataCache", 
    			CacheBuilder.newBuilder().maximumSize(1000).build());
    	
    	SimpleCacheManager manager = new SimpleCacheManager();
    	manager.setCaches(Arrays.asList(dataCache));
    	return manager;
    }

}
