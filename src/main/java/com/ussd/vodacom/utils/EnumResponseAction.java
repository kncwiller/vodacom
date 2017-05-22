/**
 * 
 */
package com.ussd.vodacom.utils;

/**
 * @author knc
 *
 */
public enum EnumResponseAction {
	request("request"),//attend une reponse de l'utilisateur
	end("end"),//fin de session
	notify("notify");//message de notification sans droit de reponse
	
	private String value;
	private EnumResponseAction(String value){
	   this.value = value;
	}

	public String toString(){
		return this.value; //This will return string value
	}
}
