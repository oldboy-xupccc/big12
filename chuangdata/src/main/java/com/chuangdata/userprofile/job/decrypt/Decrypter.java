package com.chuangdata.userprofile.job.decrypt;

import com.chuangdata.framework.encrypt.RSAEncrypt;

import java.io.*;
import java.security.interfaces.RSAPrivateKey;

/** 
 * @author luxiaofeng
 */
public class Decrypter {

    private RSAPrivateKey privateKey;
    
    public Decrypter(String privateKeyPath) throws Exception {
    	privateKey = RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(privateKeyPath));
    }
    
    public String decrypt(String encrypted) throws Exception {
    	return RSAEncrypt.decrypt(privateKey, encrypted);
    }
    
    public void decryptFile(String encrypted, String decrypted) throws Exception {
    	BufferedReader reader = new BufferedReader(new FileReader(new File(encrypted)));
    	BufferedWriter writer = new BufferedWriter(new FileWriter(new File(decrypted)));
    	String line;
    	while ((line = reader.readLine()) != null) {
    		String[] info = line.split("\t");
    		// String time = info[0];
    		String[] encryptedInfo = info[1].split("\\|");
    		for (String str : encryptedInfo) {
	    		writer.write(decrypt(str));
    		}
    		writer.write("\n");
    	}
    	reader.close();
    	writer.close();
    }
    
    public static void main(String[] args) throws Exception {
    	if (args != null && args.length == 2) {
	    	Decrypter decrypter = new Decrypter(args[0]);
	    	System.out.println(decrypter.decrypt(args[1]));
    	} else if (args != null && args.length == 3) {
    		Decrypter decrypter = new Decrypter(args[0]);
    		decrypter.decryptFile(args[1], args[2]);
    	}
    }
}
