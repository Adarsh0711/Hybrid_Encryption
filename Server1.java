import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Server1 {
	
	private ObjectOutputStream sOutput;
	private ObjectInputStream sInput;
	private Cipher keyDecipher;
	private Cipher ServerDecryptCipher;
	private Cipher ServerEncryptCipher;
	SecretKey AESkey;
	int i;
	byte[] input;
	private message m;
	int port;
	static String IV = "AAAAAAAAAAAAAAAA";
	message toSend;
	
	
	public Server1(int port){
		this.port = port;
	}
	
    public static String toHexString(byte[] ba) {
    StringBuilder str = new StringBuilder();
    for(int i = 0; i < ba.length; i++)
        str.append(String.format("%x", ba[i]));
    return str.toString();
    }

    public static String fromHexString(String hex) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < hex.length(); i+=2) {
        str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
    }
    return str.toString();
    }
	
	public static void main(String[] args) throws IOException, GeneralSecurityException{
		RSA rsa = new RSA();
		rsa.createRSA();
		int port = 8002;
		Server server = new Server(port);
		server.start();
	}
	
	void start() throws IOException{
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.print("Receiver listening on the port " + port + ".");
		Socket socket = serverSocket.accept();  
		clientThread t = new clientThread(socket);
		t.run();
		serverSocket.close();
	}
	
	  class clientThread extends Thread{
		Socket socket;
		clientThread( Socket socket) throws IOException{
			this.socket = socket;
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sInput = new ObjectInputStream(socket.getInputStream());
			new listenFromClient().start();
			new sendToClient().start();
			}
	  }
	  
		class listenFromClient extends Thread{
			
			public void run(){
				
			while(true){
			try {
				m = (message) sInput.readObject();
				
			} catch (ClassNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();
		}
			
			if (i == 0) {
				if(m.getData() != null){	
				decryptAESKey(m.getData());
				System.out.println();
				i++;}
				else{
					 
					System.exit(1);}}
			else
			{
			if(m.getData() != null){
				decryptMessage(m.getData());
				}
			}			
		  }
		}
	  }
		 
	  class sendToClient extends Thread {
	        public void run(){
	        	try{
                    PublicKey pK = readPublicKeyFromFile("public.key");
		            sOutput.writeObject(pK);
                }catch (Exception e){	
	              e.printStackTrace();
	                System.out.println("No message sent to server");
	                }
                while(true){
	        try{
	        	System.out.println("Server: Enter message : > ");
				Scanner sc = new Scanner(System.in);
				String s = sc.nextLine();
				toSend = null;
				toSend = new message(encryptMessage(s));
				write();
	        }
	        	
	         catch (Exception e){	
	              e.printStackTrace();
	                System.out.println("No message sent to server");
	                break;
	                }
	        	}
	        }
	        public synchronized void write() throws IOException{
		        sOutput.writeObject(toSend);
		        sOutput.reset();
		        }
	  	}
	
		private void decryptAESKey(byte[] encryptedKey) {
	        SecretKey key = null; PrivateKey privKey = null; keyDecipher = null;
	        try
	        {
	            privKey = readPrivateKeyFromFile("private.key"); 
	            keyDecipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 		
	            keyDecipher.init(Cipher.DECRYPT_MODE, privKey );
	            key = new SecretKeySpec (keyDecipher.doFinal(encryptedKey), "AES");
	            System.out.println();
	            System.out.println(" AES key after decryption: " +key.getEncoded());
	            i = 1;
	            AESkey =  key;
	        }
	        catch(Exception e)
	         {  e.printStackTrace(); 
	             }
	       
	    }
		
		
		private void decryptMessage(byte[] encryptedMessage) {
	        ServerDecryptCipher = null;
	        try
	        {
                String encodedKey,keyy;
                encodedKey = Base64.getEncoder().encodeToString(AESkey.getEncoded());
                keyy=toHexString(encodedKey.getBytes());
                keyy=keyy.substring(0,32);
                String ret=new String(encryptedMessage);
                AES aes1= new AES(ret,keyy);
                String pt=aes1.decrypt();
                pt=fromHexString(pt);
                pt=pt.replaceAll("-*$","");		            
	             System.out.println("Message From Client >> " + pt);
	             System.out.println("Server: Enter message : > ");
	        }
	        
	        catch(Exception e)
	         {
	        	e.getCause();
	        	e.printStackTrace();
	            }
	    }
		
			
		private byte[] encryptMessage(String s) throws NoSuchAlgorithmException, NoSuchPaddingException, 
							InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, 
											BadPaddingException{
		ServerEncryptCipher = null;
    	byte[] cipherText = null;
    	int k=s.length();
           if(k<16){
               for(int i=0;i<(16-k);i++){
               s=s.concat("-");
               }
           }
           String encodedKey,key;
           try{
         encodedKey = Base64.getEncoder().encodeToString(AESkey.getEncoded());
           key=toHexString(encodedKey.getBytes());
           key=key.substring(0,32);  
           String st=toHexString(s.getBytes());
	       AES aes= new AES(st,key);
           String et=aes.encrypt();
           cipherText=et.getBytes();
        }catch(Exception e){}
   	   return cipherText;
	}	
		
		PrivateKey readPrivateKeyFromFile(String fileName) throws IOException {
			
			 FileInputStream in = new FileInputStream(fileName);
		  	ObjectInputStream readObj =  new ObjectInputStream(new BufferedInputStream(in));

		  	try {
		  	  BigInteger m = (BigInteger) readObj.readObject();
		  	  BigInteger d = (BigInteger) readObj.readObject();
		  	  RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, d);
		  	  KeyFactory fact = KeyFactory.getInstance("RSA");
		  	  PrivateKey priKey = fact.generatePrivate(keySpec);
		  	  return priKey;
		  	} catch (Exception e) {
		  		  throw new RuntimeException("private key err", e);
		  	} finally {
		 	   readObj.close();
		 	 }
			}
        PublicKey readPublicKeyFromFile(String fileName) throws IOException {
		
	 	FileInputStream in = new FileInputStream(fileName);
	  	ObjectInputStream oin =  new ObjectInputStream(new BufferedInputStream(in));

	  	try {
	  	  BigInteger m = (BigInteger) oin.readObject();
	  	  BigInteger e = (BigInteger) oin.readObject();
	  	  RSAPublicKeySpec keySpecifications = new RSAPublicKeySpec(m, e);
	  	  
	  	  KeyFactory kF = KeyFactory.getInstance("RSA");
	  	  PublicKey pubK = kF.generatePublic(keySpecifications);
	  	  return pubK;
	  	} catch (Exception e) {
	  		  throw new RuntimeException("public key err", e);
	  	} finally {
	 	   oin.close();
	 	 }
		}
}

