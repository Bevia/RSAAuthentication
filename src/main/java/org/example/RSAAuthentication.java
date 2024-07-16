package org.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class RSAAuthentication {
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(message.getBytes("UTF-8"));

        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public static boolean verify(String message, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(message.getBytes("UTF-8"));

        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public static void printKeyPair(KeyPair keyPair, String userName) {
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println(userName + " Private Key: " + privateKey);
        System.out.println(userName + " Public Key: " + publicKey);
    }

    public static void main(String[] args) {
        try {
            // Generate key pairs for Alice and Bob
            KeyPair aliceKeyPair = generateKeyPair();
            KeyPair bobKeyPair = generateKeyPair();

            // Print out the keys
            printKeyPair(aliceKeyPair, "Alice");
            printKeyPair(bobKeyPair, "Bob");

            String message = "This is a challenge message";

            // Alice signs the message
            String aliceSignature = sign(message, aliceKeyPair.getPrivate());
            System.out.println("Alice's signed message: " + aliceSignature);

            // Bob verifies Alice's signature
            boolean isAliceVerified = verify(message, aliceSignature, aliceKeyPair.getPublic());
            System.out.println("Alice's signature verified: " + isAliceVerified);

            // Bob signs the message
            String bobSignature = sign(message, bobKeyPair.getPrivate());
            System.out.println("Bob's signed message: " + bobSignature);

            // Alice verifies Bob's signature
            boolean isBobVerified = verify(message, bobSignature, bobKeyPair.getPublic());
            System.out.println("Bob's signature verified: " + isBobVerified);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

