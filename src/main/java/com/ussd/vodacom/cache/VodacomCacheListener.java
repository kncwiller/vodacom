/**
 * 
 */
package com.ussd.vodacom.cache;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.ussd.vodacom.data.VodacomCacheData;

/**
 * @author knc
 *
 */
@Component
public class VodacomCacheListener implements RemovalListener<String,VodacomCacheData>{
	private static final Logger logger = Logger.getLogger(VodacomCacheListener.class.getName());
	
	@Override
	public void onRemoval(RemovalNotification<String, VodacomCacheData> notification) {
		logger.info("Data associated with the key("+notification.getKey()+ ") is removed.");
	}

}
