/**
 * 
 */
package com.ussd.vodacom.cache;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Component;

import com.ussd.vodacom.data.VodacomCacheData;

/**
 * @author knc
 *
 */
@Component
public class VodacomCacheSerializer {

	public void serialize(String key, VodacomCacheData data){
		 try
		 {
			 FileOutputStream fos = new FileOutputStream(key);
			 ObjectOutputStream out = new ObjectOutputStream(fos);
			 out.writeObject(data);
			 out.close();
		 }
		 catch(IOException ex){}
	}
	
	public VodacomCacheData deserialize(String key){
		VodacomCacheData data = new VodacomCacheData();
		try{
			FileInputStream fis = new FileInputStream(key);
			ObjectInputStream in = new ObjectInputStream(fis);
			data = (VodacomCacheData)in.readObject();
			in.close();
		}
		catch(IOException ex){}
		catch(ClassNotFoundException ex1){}
		return data;
	}
}
