/**
 * 
 */
package com.ussd.vodacom.service.call.input;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.ussd.vodacom.data.Struct;
import com.ussd.vodacom.data.VodacomCacheData;
import com.ussd.vodacom.utils.EnumResponseAction;
import com.ussd.vodacom.utils.EnumXMLElement;
import com.ussd.vodacom.utils.VodacomUtils;

/**
 * @author knc
 *
 */
@Component
@Produces("application/xml")
@Path("/cp/ussd/call")
public class USSDService {

	private static final Logger logger = Logger.getLogger(USSDService.class.getName());
	
	/**
	 * URL ${BASE_URL}/ws/cp/ussd/call
	 * @return
	 */
	@POST
	@Produces({MediaType.TEXT_XML})
	@Path("/")
    public Response  getUssdMenuCategorie(@Context HttpServletRequest request) {
		IOUtils ioUtils=new IOUtils();
		Struct struct=null;
		String s;
		try {
			s = ioUtils.toString(request.getInputStream());
			logger.info("input request ==>"+s);
			struct=xmlToObject(s);
			VodacomCacheData datas=InputHelper.getCacheStepValue(struct);
			if(datas!=null){
				String xmlContent =InputHelper.getMenuAction(request,datas);
				return Response.ok(xmlContent, MediaType.TEXT_XML).build();
			}else logger.info("Bad request content : datas is null");
		}catch (IOException e) {
			logger.info("IOException "+e.getMessage());
		}
		return null;
    }
	
	/**
	 * convert vodafone input xml content to Object
	 * @param xml
	 * @return
	 */
	public Struct xmlToObject(String xml){
		XMLInputFactory xif = XMLInputFactory.newFactory();
		StringReader sr = new StringReader(xml);
        StreamSource ss = new StreamSource(sr);
        XMLStreamReader xsr;
		try {
			xsr = xif.createXMLStreamReader(ss);
			while(xsr.hasNext()) {
	            if(xsr.isStartElement() && EnumXMLElement.struct.toString().equals(xsr.getLocalName())) {
	                break;
	            }
	            xsr.next();
	        }
	        JAXBContext jc = JAXBContext.newInstance(Struct.class);
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        Struct struct = unmarshaller.unmarshal(xsr, Struct.class).getValue();
	        xsr.close();
	        return struct;
	        
		} catch (XMLStreamException e1) {
			e1.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
