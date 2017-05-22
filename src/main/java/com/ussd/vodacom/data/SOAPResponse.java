/**
 * 
 */
package com.ussd.vodacom.data;

import java.util.List;

/**
 * @author knc
 *
 */
public class SOAPResponse {
	List<SOAPResponseItem> items;

	public List<SOAPResponseItem> getItems() {
		return items;
	}

	public void setItems(List<SOAPResponseItem> items) {
		this.items = items;
	}
	
}
