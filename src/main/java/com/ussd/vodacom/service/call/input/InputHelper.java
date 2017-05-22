/**
 * 
 */
package com.ussd.vodacom.service.call.input;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ussd.vodacom.data.Struct;
import com.ussd.vodacom.data.StructMember;
import com.ussd.vodacom.data.VodacomCacheData;
import com.ussd.vodacom.utils.EnumResponseAction;
import com.ussd.vodacom.utils.EnumStep;
import com.ussd.vodacom.utils.EnumXMLElement;
import com.ussd.vodacom.utils.VodacomUtils;

/**
 * @author knc
 *
 */
public class InputHelper {
	private static final Logger logger = Logger.getLogger(InputHelper.class.getName());
	
	/**
	 * retourne le menu ussd approprié
	 * @param request
	 * @param content
	 * @return
	 */
	public static  String getMenuAction(HttpServletRequest request,VodacomCacheData content) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            //add elements to Document
            Element rootElement =doc.createElement(EnumXMLElement.methodResponse.toString());
            Element params=doc.createElement(EnumXMLElement.params.toString());
    		Element param=doc.createElement(EnumXMLElement.param.toString());
    		Element value=doc.createElement(EnumXMLElement.value.toString());
    		Element struct=doc.createElement(EnumXMLElement.struct.toString());
    		//append root element to document
            doc.appendChild(rootElement);
            
            struct.appendChild(getMember(doc,VodacomUtils.STRING_RESPONSE_KEY,content.getResponseContent(),true));
            struct.appendChild(getMember(doc,VodacomUtils.STRING_ACTION_KEY, content.getAction().toString(),true));
            struct.appendChild(getMember(doc,VodacomUtils.STRING_TIME_KEY, VodacomUtils.sdf.format(new Date()),false));
            struct.appendChild(getMember(doc,VodacomUtils.STRING_TRXID_KEY, VodacomUtils.getMemberValue(content.getMembers(),VodacomUtils.STRING_TRXID_KEY,true),true));
            
            value.appendChild(struct);
            param.appendChild(value);
            params.appendChild(param);
            rootElement.appendChild(params);
            
            //for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter swriter=new StringWriter();
            StreamResult result = new StreamResult(swriter);
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            logger.info(swriter.toString());
            return swriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	/**
	 * build member
	 * @param doc
	 * @param name
	 * @param value
	 * @param isString
	 * @return
	 */
	public static Node getMember(Document doc,String name,String value,boolean isString){
		Element member=doc.createElement(EnumXMLElement.member.toString());
		member.appendChild(getMemberElement(doc,EnumXMLElement.name.toString(),name,isString));
		member.appendChild(getMemberElement(doc,EnumXMLElement.value.toString(),value,isString));
        return member;
	}
	
	/**
	 * build member element details
	 * @param doc
	 * @param cle
	 * @param valeur
	 * @param isString
	 * @return
	 */
	public static Node getMemberElement(Document doc,String cle,String valeur,boolean isString){
		Element name = doc.createElement(cle);
		if(cle.equalsIgnoreCase(EnumXMLElement.value.toString())){
			if(isString){
				Element string = doc.createElement(EnumXMLElement.string.toString());
				string.appendChild(doc.createTextNode(valeur));
				name.appendChild(string);
			}else{
				Element dateTime = doc.createElement(EnumXMLElement.dateTime.toString());
				dateTime.appendChild(doc.createTextNode(valeur));
				name.appendChild(dateTime);
			}
		}else{
			name.appendChild(doc.createTextNode(valeur));
		}
        return name;
	}
	
	/**
	 * retourne le contenu du cache correspondant au trxId courant
	 * @param request
	 * @return
	 */
	public static VodacomCacheData getCacheStepValue(Struct request){
		List<StructMember> members=request.getMembers();
		VodacomCacheData data=null;
		String trxId=VodacomUtils.getMemberValue(members,VodacomUtils.STRING_TRXID_KEY, true);
		String userData=VodacomUtils.getMemberValue(members,VodacomUtils.STRING_REQUEST_KEY, true);
		logger.info("get cache value with key ==>"+trxId);
		try {
			data=VodacomUtils.cache.get(trxId);
			if(data.getStep()!=null){
				logger.info("found cache value with key ==>"+trxId);
				if(data.getStep().name().equalsIgnoreCase(EnumStep.qui.name())){
					data.setQui(VodacomUtils.getMapValue(Integer.valueOf(userData), VodacomUtils.categories));
					if(VodacomUtils.getMapValue(Integer.valueOf(userData), VodacomUtils.categories).equalsIgnoreCase("Médecins")){
						logger.info("execution sous menu medecins");
						data.setStep(EnumStep.sub_qui);
						data.setAction(EnumResponseAction.request);
						data.setPage(1);
						data.setMembers(members);
						data.setIndex(1);
						data.setPages(1);
						data.setResponseContent(VodacomUtils.getContent(VodacomUtils.MEDECINS_TITRE,VodacomUtils.medecins));
						/*int taille=VodacomUtils.getMapSize(VodacomUtils.MEDECINS_TITRE, VodacomUtils.medecins);
						if(taille>VodacomUtils.DATA_MAX_SIZE){
							int pages=Math.round(taille/VodacomUtils.DATA_MAX_SIZE);
							logger.info(EnumStep.sub_qui.name()+" à "+pages+" pages");
							data.setPages(pages);
						}else data.setPages(1);*/
					}else{
						logger.info("execution creation menu ville via "+EnumStep.qui.name());
						data.setStep(EnumStep.ou);
						data.setAction(EnumResponseAction.request);
						data.setPage(1);
						data.setMembers(members);
						data.setIndex(1);
						data.setPages(1);
						data.setResponseContent(VodacomUtils.getContent(VodacomUtils.VILLE_TITRE,VodacomUtils.places));
					}
				}else if(data.getStep().name().equalsIgnoreCase(EnumStep.sub_qui.name())){
					logger.info("execution creation menu ville via "+EnumStep.sub_qui.name());
					data.setStep(EnumStep.ou);
					data.setAction(EnumResponseAction.request);
					data.setPage(1);
					data.setMembers(members);
					data.setIndex(1);
					data.setPages(1);
					data.setQui(VodacomUtils.getMapValue(Integer.valueOf(userData), VodacomUtils.medecins));
					data.setResponseContent(VodacomUtils.getContent(VodacomUtils.VILLE_TITRE,VodacomUtils.places));
				}else if(data.getStep().name().equalsIgnoreCase(EnumStep.ou.name())){
					data.setOu(VodacomUtils.getMapValue(Integer.valueOf(userData), VodacomUtils.places));
					if(VodacomUtils.getMapValue(Integer.valueOf(userData), VodacomUtils.places).equalsIgnoreCase("Kinshasa")){
						logger.info("execution sous menu Kinshasa");
						data.setStep(EnumStep.sub_ou);
						data.setAction(EnumResponseAction.request);
						data.setPage(1);
						data.setMembers(members);
						data.setIndex(1);
						data.setPages(1);
						data.setResponseContent(VodacomUtils.getContent(VodacomUtils.KINSHASA_TITRE,VodacomUtils.kinshasa));
					}else{
						logger.info("appel du webservice pageclaires pour les resultats via "+EnumStep.ou.name());
						data.setStep(EnumStep.result);
						data.setAction(EnumResponseAction.request);
						data.setPage(1);
						data.setMembers(members);
						data.setIndex(1);
						data.setPages(1);
						data.setItems(VodacomUtils.getPagesClairesItems(data.getQui(), data.getOu()));
						data.setResponseContent(VodacomUtils.getItemsTitle(data.getItems(),data.getQui(), data.getOu()));
					}
				}else if(data.getStep().name().equalsIgnoreCase(EnumStep.sub_ou.name())){
					logger.info("appel du webservice pageclaires pour les resultats via "+EnumStep.sub_ou.name());
					data.setOu(VodacomUtils.getMapValue(Integer.valueOf(userData), VodacomUtils.kinshasa));
					data.setStep(EnumStep.result);
					data.setAction(EnumResponseAction.request);
					data.setPage(1);
					data.setMembers(members);
					data.setIndex(1);
					data.setPages(1);
					data.setItems(VodacomUtils.getPagesClairesItems(data.getQui(), data.getOu()));
					data.setResponseContent(VodacomUtils.getItemsTitle(data.getItems(),data.getQui(), data.getOu()));
				}else if(data.getStep().name().equalsIgnoreCase(EnumStep.result.name())){
					logger.info("details resultat ");
					data.setStep(EnumStep.detail);
					data.setAction(EnumResponseAction.notify);
					data.setPage(1);
					data.setMembers(members);
					data.setIndex(1);
					data.setPages(1);
					data.setResponseContent(VodacomUtils.getItemsDetails(userData, data.getItems()));
				}else if(userData.equalsIgnoreCase("98") && data.getStep().name().equalsIgnoreCase(EnumStep.detail.name())){
					logger.info("details resultat ");
					data.setStep(EnumStep.result);
					data.setAction(EnumResponseAction.request);
					data.setPage(1);
					data.setMembers(members);
					data.setIndex(1);
					data.setPages(1);
					data.setResponseContent(VodacomUtils.getItemsTitle(data.getItems(),data.getQui(), data.getOu()));
				}else{
					data.setAction(EnumResponseAction.end);
					data.setResponseContent("Code invalide.");
				}
				
				if(data.getAction().toString().equalsIgnoreCase(EnumResponseAction.end.toString()))VodacomUtils.cache.invalidate(trxId);
				else VodacomUtils.cache.put(trxId,data);
			}else{
				logger.info("no cache value found, init cache value with key ==>"+trxId);
				data=new VodacomCacheData(EnumStep.qui,1,1,1,members);
				data.setAction(EnumResponseAction.request);
				data.setResponseContent(VodacomUtils.getContent(VodacomUtils.CATEGORIES_TITRE,VodacomUtils.categories));
				VodacomUtils.cache.put(trxId,data);
			}
		} catch (ExecutionException e) {
			logger.info("ExecutionException "+e.getMessage());
			e.printStackTrace();
		}
		return data;
	}
}
