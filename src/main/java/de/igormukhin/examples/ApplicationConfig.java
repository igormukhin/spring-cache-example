package de.igormukhin.examples;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableCaching
public class ApplicationConfig {
	
    @Bean
    public CacheManager cacheManager() {
    	CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("dataCache");
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(1000);
        PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration();
        persistenceConfiguration.setStrategy(PersistenceConfiguration.Strategy.NONE.name());
		cacheConfiguration.persistence(persistenceConfiguration );

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);

        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.newInstance(config);
        return new EhCacheCacheManager(cacheManager);
    }

}
