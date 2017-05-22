/**
 * 
 */
package com.ussd.vodacom;

import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ussd.vodacom.cache.VodacomCache;
import com.ussd.vodacom.utils.VodacomUtils;

/**
 * @author knc
 *
 */
public class VodacomListener implements ServletContextListener{
	private static final Logger logger = Logger.getLogger(VodacomListener.class.getName());
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("app context cache destroyed at "+new Date());
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("intialized the app context cache at "+new Date());
		final WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	    final VodacomCache cache = (VodacomCache)springContext.getBean("cache");
		VodacomUtils.cache=cache.getCache();
	}

}
