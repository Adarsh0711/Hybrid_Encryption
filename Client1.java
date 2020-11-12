import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
public class Client1 {

	private ObjectOutputStream sOutput;
	private ObjectInputStream sInput;
	private Socket socket;
	private String server;
	private int port;
	private Cipher cipher1;
	private Cipher cipher2;
	int i = 0,j=0;
	message m;
	SecretKey AESkey;
    PublicKey pK ;
	message toSend;
	static String IV = "AAAAAAAAAAAAAAAA";
	
	Client1 (String server, int port){ 						
	this.server = server;
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
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		
		String serverAddress;
	
		int portNumber = 8002;
        serverAddress = "localhost";
		Client client = new Client(serverAddress, portNumber);
		client.generateAESkey();
		client.start();
	}
	
	void start() throws IOException{
		socket = new Socket(server, port);
		System.out.println("connection accepted " + socket.getInetAddress() + " :"  + socket.getPort());
		
		
		sInput = new ObjectInputStream(socket.getInputStream());
		sOutput = new ObjectOutputStream(socket.getOutputStream());
		
		new sendToServer().start();
		new listenFromServer().start();
	}

	class listenFromServer extends Thread {
		public void run(){
			while(true){
        try{
            if(j == 0){
                pK = (PublicKey) sInput.readObject();
                j=1;
            }else{
     m = (message) sInput.readObject();
            decryptMessage(m.getData());}
      } catch (Exception e){
       		e.printStackTrace();
              System.out.println("connection closed");
                }
      	}
	}
	}

	class sendToServer extends Thread {
        public void run(){
        	while(true){
        try{
	
        if (i == 0){	
        	toSend = null;
   
    	toSend = new message(encryptAESKey());
		sOutput.writeObject(toSend);
        	i =1;
        	}					
        
        else{
        	
        	System.out.println("Client: Enter message > ");
			Scanner sc = new Scanner(System.in);
			String s = sc.nextLine();
			toSend = new message(encryptMessage(s));
			sOutput.writeObject(toSend);
        	}
        	
        } catch (Exception e){
              e.printStackTrace();
                break;
                }
        	}
        }
	}
	
	void generateAESkey() throws NoSuchAlgorithmException{
	AESkey = null;
	KeyGenerator Gen = KeyGenerator.getInstance("AES");
	Gen.init(128);
	AESkey = Gen.generateKey();
	System.out.println("Genereated the AES key : " + AESkey.getEncoded());
	}
	
	private byte[] encryptAESKey (){
		cipher1 = null;
    	byte[] key = null;
  	  try
  	  {
		 
	 	  System.out.println("Encrypting the AES key using RSA Public Key:\n" + pK);
   	     cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
   	     cipher1.init(Cipher.ENCRYPT_MODE, pK );
   	     key = cipher1.doFinal(AESkey.getEncoded());  
   	     i = 1;
   	 	}
  	  
   	 catch(Exception e ) {
    	    System.out.println ( "" + e.getMessage() );
    	    e.printStackTrace();
   	 		}
  	  return key;
  	  } 

		private byte[] encryptMessage(String s) throws NoSuchAlgorithmException, NoSuchPaddingException, 
							InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, 
											BadPaddingException{
		cipher2 = null;
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
		private void decryptMessage(byte[] encryptedMessage) {
	        cipher2 = null;
	        try
	        { 
                String encodedKey,key;
                encodedKey = Base64.getEncoder().encodeToString(AESkey.getEncoded());
                key=toHexString(encodedKey.getBytes());
                key=key.substring(0,32);
                String ret=new String(encryptedMessage);
                AES aes1= new AES(ret,key);
                String pt=aes1.decrypt();
                pt=fromHexString(pt);
                pt=pt.replaceAll("-*$","");	            
	             System.out.println("Message From Server   >> " + pt);
	             System.out.println("Client: Enter message > ");
	        }
	        
	        catch(Exception e)
	         {
	        	e.getCause();
	        	e.printStackTrace();
	        	System.out.println ( ""  + e.getMessage() );
	            }
	    }
	

	public void closeSocket() {
		try{
	if(sInput !=null) sInput.close();
	if(sOutput !=null) sOutput.close();
	if(socket !=null) socket.close();
		}catch (IOException ioe){
			}
		}
	
		}




