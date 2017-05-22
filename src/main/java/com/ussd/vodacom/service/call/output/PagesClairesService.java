/**
 * 
 */
package com.ussd.vodacom.service.call.output;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.text.WordUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ussd.vodacom.data.SOAPResponse;
import com.ussd.vodacom.data.SOAPResponseItem;
import com.ussd.vodacom.data.SOAPResponseValue;
import com.ussd.vodacom.utils.EnumXMLElement;
import com.ussd.vodacom.utils.VodacomUtils;

/**
 * @author knc
 *
 */
public class PagesClairesService {
	private static final Logger logger = Logger.getLogger(PagesClairesService.class.getName());
	
	/**
	 * appel du webservice pageclaires
	 * @param qui
	 * @param ou
	 */
	public static List<SOAPResponseValue> consumePageClaires(String qui,String ou){
		List<SOAPResponseValue> values=null;
		try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
           
            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(qui,ou), VodacomUtils.PAGECLAIRES_ENDPOINT);

            // Process the SOAP Response
            SOAPResponse response=printSOAPResponse(soapResponse);
            values=buildSoapValue(response);
            
            soapConnection.close();
        } catch (Exception e) {
            logger.info("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
		return values;
	}

	/**
	 * use to create SOAP Body
	 * @param qui: categorie recherche
	 * @param ou : lieu (ville) choisie pour la recherche
	 * @return
	 * @throws Exception
	 */
    private static SOAPMessage createSOAPRequest(String qui,String ou) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("xsd","http://www.w3.org/2001/XMLSchema");
        envelope.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");
    	envelope.addNamespaceDeclaration("enc","http://schemas.xmlsoap.org/soap/encoding/");
    	envelope.addNamespaceDeclaration("env","http://schemas.xmlsoap.org/soap/envelop/");
    	envelope.addNamespaceDeclaration("urn", VodacomUtils.PAGECLAIRES_WSDL);
    	envelope.setEncodingStyle(VodacomUtils.PAGECLAIRES_ENCODING);
        
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(envelope.createName("doSearch", "urn",VodacomUtils.PAGECLAIRES_TYPES));
        soapBodyElem.setEncodingStyle(VodacomUtils.PAGECLAIRES_ENCODING);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("qui","urn");
        soapBodyElem1.addTextNode(qui).setAttribute("xsi:type","xsd:string");
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("ou","urn");
        soapBodyElem2.addTextNode(ou).setAttribute("xsi:type","xsd:string");
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("offset","urn");
        soapBodyElem3.addTextNode("").setAttribute("xsi:type","xsd:int");

        MimeHeaders hd = soapMessage.getMimeHeaders();
        hd.addHeader("SOAPAction", "urn:doSearchAction");
        
        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println();

        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     */
    private static SOAPResponse printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.print("\nResponse SOAP Message = ");
        StringWriter swriter=new StringWriter();
        StreamResult result = new StreamResult(swriter);
        transformer.transform(sourceContent, result);
        logger.info(swriter.toString());
        return soapResponseToObject(swriter.toString());
    }
    
    /**
     * convertit la reponse soap notamment le tag <item> en POJO
     * @param xml
     * @return
     */
    private static SOAPResponse soapResponseToObject(String xml){
    	SOAPResponse response=new SOAPResponse();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			Element rootElement = document.getDocumentElement();
			 NodeList list = rootElement.getElementsByTagName(EnumXMLElement.item.toString());
		        if (list != null && list.getLength() > 0) {
		        	List<SOAPResponseItem> items=new ArrayList<SOAPResponseItem>();
		        	for(int i=0;i<list.getLength();i++){
		        		NodeList subList = list.item(i).getChildNodes();
			            if (subList != null && subList.getLength() > 0) {
			            	SOAPResponseItem item=new SOAPResponseItem();
			            	item.setItem(subList.item(0).getNodeValue());
			            	items.add(item);
			            	logger.info("item value "+item.getItem());
			            }
		        	}
		        	response.setItems(items);
				}
		        return response;
		} catch (ParserConfigurationException e) {
			logger.info("ParserConfigurationException "+e.getLocalizedMessage());
		    e.printStackTrace();
		} catch (SAXException e) {
			logger.info("SAXException "+e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("IOException "+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * parsing item string from pageclaires soap result
     * @param response
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static List<SOAPResponseValue> buildSoapValue(SOAPResponse response) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
    	List<SOAPResponseValue> values=new ArrayList<SOAPResponseValue>();
    	for (SOAPResponseItem item : response.getItems()) {
    		String[] elts=item.getItem().split(VodacomUtils.ITEM_SEPARATOR);
			SOAPResponseValue value=new SOAPResponseValue();
			for(int i=0;i<elts.length;i++){
				String[] sub=elts[i].split(VodacomUtils.VALUE_SEPARATOR);
				//on vérifie l'existence de la methode dans la classe
				Method method=null;
				method=SOAPResponseValue.class.getMethod("set"+WordUtils.capitalize(sub[0]), new Class[] { String.class });
				if(method!=null)method.invoke(value,String.valueOf(sub[1]));
			}
			values.add(value);
		}
    	return values;
    }

}
