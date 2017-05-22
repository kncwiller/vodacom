/**
 * 
 */
package com.ussd.vodacom.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheLoader;
import com.ussd.vodacom.data.VodacomCacheData;

/**
 * @author knc
 *
 */
@Component
public class VodacomCacheLoader extends CacheLoader<String, VodacomCacheData>{

	@Autowired
	VodacomCacheSerializer serializer;
	public void setSerializer(VodacomCacheSerializer serializer) {
		this.serializer = serializer;
	}

	@Override
	public VodacomCacheData load(String key) throws Exception {
		return serializer.deserialize(key);
	}

}
