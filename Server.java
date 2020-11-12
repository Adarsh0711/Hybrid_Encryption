import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Scanner;
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Server {
	
	private ObjectOutputStream sOutput;
	private ObjectInputStream sInput;
	private Cipher keyDecipher;
	private Cipher ServerDecryptCipher;
	private Cipher ServerEncryptCipher;
	SecretKey AESKey;
	int i;
	byte[] input;
	private message m;
	int port;
	static String IV = "AAAAAAAAAAAAAAAA";
	message toSend;
	
	
	public Server(int port){
		this.port = port;
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
				System.out.println("Class not found while reading the message object");
			} catch (IOException e) {e.printStackTrace();
		}
			
			if (i == 0) {
				if(m.getData() != null){	
				decryptAESKey(m.getData());
				System.out.println();
				i++;}
				else{
					System.out.println("Error in decrypting AES key in clientThread.run()"); 
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
	            System.out.println(" AES key after decryption : " +key.getEncoded().toString());
	            i = 1;
	            AESKey =  key;
	        }
	        catch(Exception e)
	         {  e.printStackTrace(); 
	             }
	       
	    }
		
		
		private void decryptMessage(byte[] encryptedMessage) {
	        ServerDecryptCipher = null;
	        try
	        {
	            ServerDecryptCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	            ServerDecryptCipher.init(Cipher.DECRYPT_MODE, AESKey, new IvParameterSpec(IV.getBytes()));
	             byte[] msg = ServerDecryptCipher.doFinal(encryptedMessage);		            
	             System.out.println("Message From Client >> " + new String(msg));
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
    	ServerEncryptCipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");  	
    	ServerEncryptCipher.init(Cipher.ENCRYPT_MODE, AESKey, new IvParameterSpec(IV.getBytes()) );
    	cipherText = ServerEncryptCipher.doFinal(s.getBytes());
    	
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
		  		  throw new RuntimeException("Some error in reading private key", e);
		  	} finally {
		 	   readObj.close();
		 	 }
			}
}

