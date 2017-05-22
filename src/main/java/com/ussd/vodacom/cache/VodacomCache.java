/**
 * 
 */
package com.ussd.vodacom.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.ussd.vodacom.data.VodacomCacheData;

/**
 * @author knc
 *
 */
@Component
public class VodacomCache {

	public LoadingCache<String,VodacomCacheData> cache;
	
	@Autowired
	VodacomCacheLoader cacheLoader;
	public void setCacheLoader(VodacomCacheLoader cacheLoader) {
		this.cacheLoader = cacheLoader;
	}
	@Autowired
	VodacomCacheListener cacheListener;	
	public void setCacheListener(VodacomCacheListener cacheListener) {
		this.cacheListener = cacheListener;
	}

	public LoadingCache<String, VodacomCacheData> getCache(){
		cache = CacheBuilder.newBuilder().
				maximumSize(1000).
				expireAfterAccess(10, TimeUnit.MINUTES).
				removalListener(cacheListener).
				build(cacheLoader);
		return cache;
	}
	
	public VodacomCacheData get(String key) throws ExecutionException{
		return cache.get(key);
	}
	
	public void remove(String key) throws ExecutionException{
		cache.invalidate(key);
	}
	
	public void put(String key,VodacomCacheData data) throws ExecutionException{
		cache.put(key, data);
	}
}
