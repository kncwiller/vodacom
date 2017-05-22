/**
 * 
 */
package com.ussd.vodacom.utils;

/**
 * @author knc
 *
 */
public enum EnumPagination {
	Précedent("98.< Précedent"),
	Suivant("99.> Suivant");
	
	private String value;
	private EnumPagination(String value){
	   this.value = value;
	}

	public String toString(){
		return this.value; //This will return string value
	}
}
