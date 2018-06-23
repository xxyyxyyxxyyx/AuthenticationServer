package com.union.authorization.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
public class KeysUtilities {
    @Autowired
    private JwtAccessTokenConverter tokenConverter;

    public void changeEncryptionAlgorithm() {
        try {
            byte[] publicKeyBytes = Files.readAllBytes(new File("Keypair/publicKey").toPath());
            byte[] privateKeyBytes = Files.readAllBytes(new File("KeyPair/privateKey").toPath());
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            tokenConverter.setSigner(new org.springframework.security.jwt.crypto.sign.RsaSigner((RSAPrivateKey) privateKey));
            tokenConverter.setVerifier(new RsaVerifier((RSAPublicKey) publicKey));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
