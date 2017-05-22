/**
 * 
 */
package com.ussd.vodacom.utils;

/**
 * @author knc
 *
 */
public enum EnumXMLElement {
	methodResponse("methodResponse"),
	params("params"),
	param("param"),
	value("value"),
	struct("struct"),
	member("member"),
	name("name"),
	string("string"),
	dateTime("dateTime.iso8601"),
	doSearchReturn("doSearchReturn"),
	item("item");
	
	private String val;
	private EnumXMLElement(String val){
	   this.val = val;
	}

	public String toString(){
		return this.val; //This will return string value
	}
}
