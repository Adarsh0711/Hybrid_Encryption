# Hybrid_Encryption

🔧 System Architecture Overview
This project implements a secure, real-time chat application using a multithreaded client-server architecture in Java. The system is designed to ensure confidentiality, authenticity, and integrity of messages through a hybrid encryption approach combining RSA and AES with a dynamic key-based S-Box.

🧠 Key Components
Client Module
Each client establishes a socket connection to the server and operates on a separate thread, enabling multiple users to send and receive messages simultaneously. The client is responsible for encrypting messages using AES and securely sending the AES key using RSA.

Server Module
The server listens for incoming client connections on a dedicated thread. For each client, a new thread is spawned to manage bidirectional encrypted communication. The server decrypts the AES key using its private RSA key and forwards encrypted messages to recipients.

Encryption Layer

RSA (1024-bit, ECB mode): Used for user authentication and secure transmission of AES keys.
AES (128-bit): Handles message encryption and decryption. Enhanced with a custom dynamic S-Box for additional complexity and protection against cryptographic attacks.
🔐 Security Highlights
Hybrid Cryptography: Combines the speed of symmetric encryption (AES) with the security of asymmetric encryption (RSA).
Dynamic S-Box: Adds a unique, key-dependent transformation layer to AES, increasing resistance to differential and linear cryptanalysis.
Multithreading: Supports multiple simultaneous connections and real-time messaging through concurrent threads for each client.
End-to-End Encryption: Messages remain encrypted throughout the transmission path, accessible only to sender and intended recipient.


### Demo Pictures

![Screenshot 2024-07-28 at 17 45 07](https://github.com/user-attachments/assets/000b722a-c7ad-45e8-b5c6-1edfde380846)

![Screenshot 2024-07-28 at 17 44 59](https://github.com/user-attachments/assets/1b378862-04ce-4296-a1d2-3d782079876e)
