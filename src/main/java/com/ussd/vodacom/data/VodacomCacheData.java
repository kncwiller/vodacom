/**
 * 
 */
package com.ussd.vodacom.data;

import java.util.List;

import com.ussd.vodacom.utils.EnumResponseAction;
import com.ussd.vodacom.utils.EnumStep;

/**
 * @author knc
 *
 */
public class VodacomCacheData {

	EnumStep step;
	EnumResponseAction action;
	int page; //page courante de la pagination
	int pages; //nombre de pages paginées
	int index; //cle de l'élémnent suivant dans la map paginée
	List<StructMember> members;
	String responseContent;//reponse à la requete ussd
	String qui;
	String ou;
	List<SOAPResponseValue> items;
	
	public VodacomCacheData() {
		super();
	}
	public VodacomCacheData(EnumStep step, int page, int pages,int index,
			List<StructMember> members) {
		this.step = step;
		this.page = page;
		this.pages = pages;
		this.index = index;
		this.members = members;
	}

	public EnumStep getStep() {
		return step;
	}
	public void setStep(EnumStep step) {
		this.step = step;
	}
	public List<StructMember> getMembers() {
		return members;
	}
	public void setMembers(List<StructMember> members) {
		this.members = members;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getResponseContent() {
		return responseContent;
	}
	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getQui() {
		return qui;
	}
	public void setQui(String qui) {
		this.qui = qui;
	}
	public String getOu() {
		return ou;
	}
	public void setOu(String ou) {
		this.ou = ou;
	}
	public List<SOAPResponseValue> getItems() {
		return items;
	}
	public void setItems(List<SOAPResponseValue> items) {
		this.items = items;
	}
	public EnumResponseAction getAction() {
		return action;
	}
	public void setAction(EnumResponseAction action) {
		this.action = action;
	}
}
