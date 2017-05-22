/**
 * 
 */
package com.ussd.vodacom.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author knc
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StructMemberValue {

	String string;
	@XmlElement(name="dateTime.iso8601")
	String trxTime;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}
}
