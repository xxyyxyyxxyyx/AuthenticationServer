package com.union.authorization.apis;

import com.union.authorization.utilities.GenerateKeys;
import com.union.authorization.utilities.KeysUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RestController
@RequestMapping(value = "/api/keys")
@PreAuthorize("hasRole('ROLE_SYSADMIN')")
public class KeyController {

    @Autowired
    private KeysUtilities keysUtilities;

    @GetMapping
    public ResponseEntity<String> getPublicKey() {
        Base64.Encoder encoder = Base64.getMimeEncoder();
        try {
            byte[] publicKeyBytes = Files.readAllBytes(new File("Keypair/publicKey").toPath());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            String publicKeyAsString = encoder.encodeToString(publicKey.getEncoded());
            return new ResponseEntity<>(publicKeyAsString, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
        } catch (InvalidKeySpecException e) {
            return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity createKeys() {
        GenerateKeys gk = null;
        try {
            gk = new GenerateKeys(1024);
            Base64.Encoder encoder = Base64.getEncoder();
            gk.createKeys();
            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());

            keysUtilities.changeEncryptionAlgorithm();

            return new ResponseEntity<String>("Keys created successfully", HttpStatus.CREATED);
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.badRequest().body("Error");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error");
        }

    }
}
