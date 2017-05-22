/**
 * 
 */
package com.ussd.vodacom.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.common.cache.LoadingCache;
import com.ussd.vodacom.data.SOAPResponseValue;
import com.ussd.vodacom.data.StructMember;
import com.ussd.vodacom.data.VodacomCacheData;
import com.ussd.vodacom.service.call.output.PagesClairesService;

/**
 * @author knc
 *
 */
public class VodacomUtils {

	/**
	 * valeur des noeuds name des fichiers xml (entrants et sortants)
	 */
	public static final String STRING_RESPONSE_KEY="USSDResponseString";
	public static final String STRING_ACTION_KEY="action";
	public static final String STRING_TIME_KEY="TransactionTime";
	public static final String STRING_TRXID_KEY="TransactionId";
	public static final String STRING_MSISDN_KEY="MSISDN";
	public static final String STRING_SERVICE_KEY="USSDServiceCode";
	public static final String STRING_REQUEST_KEY="USSDRequestString";
	
	public static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	/**
	 * separateur dans les resultats du service pageclaires
	 */
	public static final String ITEM_SEPARATOR="\\*\\*NEXT\\*\\*"; //on echape le caractere special * sinon ça crée des pbs dans le code
	public static final String VALUE_SEPARATOR="=";
	
	/**
	 * pagesclaires endpoint url
	 */
	public static final String PAGECLAIRES_ENDPOINT="http://www.pagesclaires.com/fr/webservices/execute/phpsoap";
	public static final String PAGECLAIRES_WSDL="http://www.pagesclaires.com/fr/webservices/wsdl/doSearch";
	public static final String PAGECLAIRES_ENCODING="http://schemas.xmlsoap.org/soap/encoding/";
	public static final String PAGECLAIRES_TYPES="http://www.pagesclaires.com/fr/webservices/wsdl/doSearch/types";
	
	/**
	 * cache partagé dans l'application
	 */
	public static LoadingCache<String,VodacomCacheData> cache=null;
	
	/**
	 * menus
	 */
	public static final int DATA_MAX_SIZE=500;
	public static final int DATA_MAX_SIZE_MIN=485;
	public static final String CATEGORIES_TITRE="Choisissez la catégorie: ";
	public static final String MEDECINS_TITRE="Précisez la catégorie de médecin: ";
	public static final String VILLE_TITRE="Choisissez la ville: ";
	public static final String KINSHASA_TITRE="Choisissez la commune: ";
	
	public static final Map<Integer,String> categories;
	static{
		categories = new HashMap<Integer, String>();
		categories.put(1, "Hôtels");categories.put(2, "Restaurants");categories.put(3, "Bars");
		categories.put(4, "Discothèques");categories.put(5, "Banques");categories.put(6, "Magasins");
		categories.put(7, "Supermarchés");categories.put(8, "Immobilier");categories.put(9, "Voitures");
		categories.put(10, "Administrations");categories.put(11, "Education");categories.put(12, "Hôpitaux");
		categories.put(13, "Médecins");categories.put(14, "Téléphonie");
	}
	public static final Map<Integer,String> medecins;
	static{
		medecins = new HashMap<Integer, String>();
		medecins.put(1, "Médecins généralistes");medecins.put(2, "Anesthésistes");medecins.put(3, "Anatomie et cytologie pathologiques");
		medecins.put(4, "Cardiologues, maladies vasculaires");medecins.put(5, "Urologues");medecins.put(6, "Dermatologues");
		medecins.put(7, "Nutritionistes");medecins.put(8, "Gynécologie obstétrique");medecins.put(9, "Radiologues");
		medecins.put(10, "Gastro-entérologue");medecins.put(11, "Gynécologues");medecins.put(12, "Neurochirurgiens");
		medecins.put(13, "Légistes");medecins.put(14, "Pédiatres");medecins.put(15, "Ophtalmologues");
		medecins.put(16, "Neuropsychiatres");medecins.put(17, "Stomatologues");medecins.put(18, "Rhumatologue");
		medecins.put(19, "Psychiatre");medecins.put(20, "Dentistes");
	}
	public static final Map<Integer,String> places;
	static{
		places = new HashMap<Integer, String>();
		places.put(1, "Kinshasa");places.put(2, "Lubumbashi");places.put(3, "Matadi");
		places.put(4, "Mbuji-Mayi");places.put(5, "Bandundu");places.put(6, "Baraka");
		places.put(7, "Boma");places.put(8, "Bukavu");places.put(9, "Bumba");
		places.put(10, "Bunia");places.put(11, "Butembo");places.put(12, "Gandajika");
		places.put(13, "Gemena");places.put(14, "Goma");places.put(15, "Isiro");
		places.put(16, "Kabinda");places.put(17, "Kalemie");places.put(18, "Kamina");
		places.put(19, "Kananga");places.put(20, "Kikwit");places.put(21, "Kindu");
		places.put(22, "Kipushi");places.put(23, "Kisangani");places.put(24, "Kolwezi");
		places.put(25, "Likasi");places.put(26, "Mbandaka");places.put(27, "Mwene-Ditu");
		places.put(28, "Tshikapa");places.put(29, "Uvira");
	}
	public static final Map<Integer,String> kinshasa;
	static{
		kinshasa = new HashMap<Integer, String>();
		kinshasa.put(1, "Kisenso");kinshasa.put(2, "Lemba");kinshasa.put(3, "Maluku");
		kinshasa.put(4, "Bandalungwa");kinshasa.put(5, "Barumbu");kinshasa.put(6, "Bumbu");
		kinshasa.put(7, "Gombe");kinshasa.put(8, "Kalamu");kinshasa.put(9, "Kasa-Vubu");
		kinshasa.put(10, "Kimbanseke");kinshasa.put(11, "Kinshasa");kinshasa.put(1, "Kintambo");
		kinshasa.put(13, "Limete");kinshasa.put(14, "Lingwala");kinshasa.put(15, "Makala");
		kinshasa.put(16, "Masina");kinshasa.put(17, "Matete");kinshasa.put(18, "Mont Ngafula");
		kinshasa.put(19, "Ndjili");kinshasa.put(20, "Ngaba");kinshasa.put(21, "Ngaliema");
		kinshasa.put(22, "Ngiri-Ngiri");kinshasa.put(23, "Nsele");kinshasa.put(24, "Selembao");
	}
		    	
	/**
	 * recherche d'un élément dans la liste connaissant la clé
	 * @param members
	 * @param key
	 * @param type
	 * @return
	 */
	public static String getMemberValue(List<StructMember> members,String key,boolean type){
		String val=null;
		for(StructMember member:members){
			if(member.getName().equalsIgnoreCase(key)){
				if(type)val=member.getValue().getString();
				else val=member.getValue().getTrxTime();
				break;
			}
		}
		return val;
	}
	
	/**
	 * retourne map value
	 * @param key
	 * @param map
	 * @return
	 */
	public static String getMapValue(Integer key,Map<Integer, String> map){
		return map.get(key);
	}
	
	/**
	 * retourne la taille du contenu d'une map (devant servir de menu ussd)
	 * @param titre
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int getMapSize(String titre,Map<Integer, String> map){
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(map);
		Iterator<Entry<Integer, String>> it = treeMap.entrySet().iterator();
		String taille=titre;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        taille+=pair.getKey()+"."+pair.getValue()+" ";
	    }
	    System.out.println("taille du menu ==>"+taille.length());
	    taille=taille.trim();
	    System.out.println("taille du menu apres trim()==>"+taille.length());
	    return taille.length();
	}
	
	/**
	 * retourne le contenu d'une map (menu ussd à fournir a l'utilisateur)
	 * @param titre
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getContent(String titre,Map<Integer, String> map){
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(map);
		Iterator<Entry<Integer, String>> it = treeMap.entrySet().iterator();
		String taille=titre;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        taille+=pair.getKey()+"."+pair.getValue()+" ";
	    }
	    taille=taille.trim();
	    System.out.println("menu ==>"+taille);
	    return taille;
	}
	
	/**
	 * retourne le contenu paginé d'une map (menu ussd à fournir a l'utilisateur)
	 * @param page
	 * @param pages
	 * @param index
	 * @param titre
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getContent(int page,int pages,String index,String titre,Map<Integer, String> map){
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(map);
		Iterator<Entry<Integer, String>> it = treeMap.entrySet().iterator();
		String taille=titre;
		if(page!=1)taille+=EnumPagination.Précedent.toString();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        if(((Integer)pair.getKey())>=Integer.valueOf(index)){
	        	String s=taille+pair.getKey()+"."+pair.getValue()+" ";
	        	if(s.length()<=DATA_MAX_SIZE_MIN)taille+=pair.getKey()+"."+pair.getValue()+" ";
	        	else break;
	        }
	    }
	    if(page<pages)taille+=EnumPagination.Suivant.toString();
	    System.out.println("menu ==>"+taille);
	    return taille;
	}
	
	/**
	 * retourne les résultats de la recherche de l'utilisateur ussd
	 * @param qui
	 * @param ou
	 * @return
	 */
	public static List<SOAPResponseValue> getPagesClairesItems(String qui,String ou){
		return PagesClairesService.consumePageClaires(qui, ou);
	}
	
	/**
	 * contenu à afficher à l'utilisateur
	 * @param liste
	 * @param qui
	 * @param ou
	 * @return
	 */
	public static String getItemsTitle(List<SOAPResponseValue> liste,String qui,String ou){
		String s=qui+" à "+ou+" : ";
		int i=1;
		if(liste!=null){
			for(SOAPResponseValue value:liste){
				s+=" "+i+"."+value.getSocietyname();
				i++;
			}
		}else s+=" Aucun résultat trouvé";
		return s;
	}
	
	/**
	 * details d'un resultat
	 * @param key
	 * @param liste
	 * @return
	 */
	public static String getItemsDetails(String key,List<SOAPResponseValue> liste){
		SOAPResponseValue item=liste.get(Integer.valueOf(key)-1);
		String s=item.getSocietyname();
		if(valueIsNull(item.getAdresse_commune()))s+=" "+item.getAdresse_commune();
		if(valueIsNull(item.getAdresse_numero()))s+=" "+item.getAdresse_numero();
		if(valueIsNull(item.getAdresse_quartier()))s+=" "+item.getAdresse_quartier();
		if(valueIsNull(item.getAdresse_rue()))s+=" "+item.getAdresse_rue();
		if(valueIsNull(item.getAdresse_ville()))s+=" "+item.getAdresse_ville();
		if(valueIsNull(item.getMail_contact()))s+=" "+item.getMail_contact();
		if(valueIsNull(item.getMobile_1()))s+=" "+item.getMobile_1();
		if(valueIsNull(item.getMobile_2()))s+=" "+item.getMobile_2();
		if(valueIsNull(item.getMobile_3()))s+=" "+item.getMobile_3();
		if(valueIsNull(item.getTel_fax()))s+=" "+item.getTel_fax();
		if(valueIsNull(item.getTel_standard()))s+=" "+item.getTel_standard();
		if(valueIsNull(item.getWebsite()))s+=" "+item.getWebsite();
		s+=" "+EnumPagination.Précedent.toString();
		return s;
	}
	
	public static boolean valueIsNull(String val){
		return (val!=null)? true:false;
	}
}
