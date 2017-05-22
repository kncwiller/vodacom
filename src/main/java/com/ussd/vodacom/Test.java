/**
 * 
 */
package com.ussd.vodacom;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ussd.vodacom.service.call.output.PagesClairesService;

/**
 * @author knc
 *
 */
public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		System.out.println(sdf.format(new Date()));
		
		PagesClairesService.consumePageClaires("Hotels", "Kinshasa");
	}

}
