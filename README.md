# Hybrid_Encryption

### About
This project focuses on designing a secure encrypted chat application using Java. The system employs a hybrid cryptographic approach, integrating AES (Advanced Encryption Standard) and RSA (Rivest-Shamir-Adleman) to ensure data security. In this application, RSA is used to verify the intended recipient and securely transfer the AES key. The client encrypts the AES key using the recipient's public key with RSA and sends it to the server. The server then decrypts the AES key using its private key. Once the AES key is exchanged, it is used to encrypt and decrypt messages between users. RSA is utilized in Electronic Code Book (EBC) mode with a 1024-bit key, while AES uses a 128-bit key and is implemented with a dynamic key-based S-Box. This method ensures that messages are securely transmitted between users, protecting data from unauthorized access, alteration, or destruction.

### Features

Hybrid Cryptographic System: Utilizes both AES (Advanced Encryption Standard) and RSA (Rivest-Shamir-Adleman) for robust encryption.
Secure Key Exchange: Employs RSA for secure transmission of the AES key, ensuring that only the intended recipient can decrypt it.
Dynamic S-Box: Implements AES with a dynamic key-based S-Box for enhanced security.
End-to-End Encryption: Ensures that all messages between users are encrypted and decrypted using AES, protecting the data during transmission.
Public/Private Key Management: Uses RSA in Electronic Code Book (EBC) mode with a 1024-bit key for verifying recipients and transferring the AES key securely.
User Authentication: RSA verifies the identity of users, ensuring that messages are sent to the correct recipient.

### Demo Pictures

![Screenshot 2024-07-28 at 17 45 07](https://github.com/user-attachments/assets/000b722a-c7ad-45e8-b5c6-1edfde380846)

![Screenshot 2024-07-28 at 17 44 59](https://github.com/user-attachments/assets/1b378862-04ce-4296-a1d2-3d782079876e)
