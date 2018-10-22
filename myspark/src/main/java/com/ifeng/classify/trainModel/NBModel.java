package com.ifeng.classify.trainModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ifeng.classify.Util.NativeBayes;

public class NBModel {
	
	private static String path = "c:/data/NB.model.bin";
	
    /**
    * MethodName: SerializePerson 
    * Description: 序列化Person对象
    * @author 
    * @throws FileNotFoundException
    * @throws IOException
    */
    public static void SerializeNativeBayes(NativeBayes nativeBayes){
     // ObjectOutputStream 对象输出流，将Person对象存储到E盘的Person.txt文件中，完成对Person对象的序列化操作
     ObjectOutputStream oo = null;
	try {
		oo = new ObjectOutputStream(new FileOutputStream(
				 new File(path)));
		oo.writeObject(nativeBayes);
		System.out.println("NativeBayes对象序列化成功！");
		oo.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
    /**
    * MethodName: DeserializePerson 
    * Description: 反序列Perons对象
    * @author 
    * @return
    * @throws Exception
    * @throws IOException
    */
    public static NativeBayes DeserializeNativeBayes(){
    	ObjectInputStream ois = null;
    	NativeBayes nativeBayes = null;
    	try{
			ois = new ObjectInputStream(new FileInputStream(
					new File(path)));
			nativeBayes = (NativeBayes) ois.readObject();
			System.out.println("NativeBayes对象反序列化成功！");
		}catch(Exception e){
			e.printStackTrace();
		}
    	
     return nativeBayes;
    }
}
