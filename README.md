# Hybrid_Encryption

### About

Security is nothing but protecting data and other information from unauthorised persons to access, destruction or change our data.In this world, cybersecurity is incredibly important be- cause we've got our privacy at stake.So to counter this we'll design an encrypted chat employ- ing a hybrid cryptographic system in java using AES(advanced encryption standard) and RSA(Rivest–Shamir–Adleman ) for encryption.

RSA is employed to verify the intended recipient and to transfer the AES key. AES is em- ployed then to encrypt the messages passed between the 2 users. First the Client transfers the AES key (encrypted using public key of recipient with RSA) to the recipient(Server).Server then decrypts the message using its private key to urge AES key which is employed to en- crypt and decrypt further messages.RSA utilized in EBC(electronic code book) mode with 1024 bit key and AES with 128 bit key. AES is implemented with dynamic Key based SBox. Using this method messages will be securely passed between the 2 users.

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
