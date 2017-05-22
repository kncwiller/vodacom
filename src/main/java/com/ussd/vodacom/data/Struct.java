/**
 * 
 */
package com.ussd.vodacom.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author knc
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Struct {

	@XmlElement(name="member")
	List<StructMember> members;

	public List<StructMember> getMembers() {
		return members;
	}

	public void setMembers(List<StructMember> members) {
		this.members = members;
	}
}
