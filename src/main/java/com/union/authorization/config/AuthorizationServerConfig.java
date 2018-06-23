package com.union.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    static final String CLIENT_ID = "client";
    static final String CLIENT_SECRET = "secret";
    static final String GRANT_TYPE_PASSWORD = "password";
    static final String REFRESH_TOKEN = "refresh_token";
    static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
    static final int REFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;
    private String publicKey;
    private String privateKey;
    private PrivateKey rsaPrivateKey;
    private PublicKey rsaPublicKey;

    private ResourceLoader resourceLoader;

    @Autowired
    public AuthorizationServerConfig(ResourceLoader resourceLoader) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.resourceLoader = resourceLoader;
//        try {
//            InputStream publicKeyFile = resourceLoader.getResource("classpath:keys\\publicKey.txt").getInputStream();
//            InputStream privateKeyFile = resourceLoader.getResource("classpath:keys\\privateKey.txt").getInputStream();
//            Scanner publicScanner = new Scanner(publicKeyFile);
//            publicScanner.useDelimiter("\\Z");
//            publicKey = publicScanner.next();
//            Scanner privateScanner = new Scanner(privateKeyFile);
//            privateScanner.useDelimiter("\\Z");
//            privateKey = privateScanner.next();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            byte[] privateKeyBytes = Files.readAllBytes(new File("KeyPair/privateKey").toPath());
            byte[] publicKeyBytes = Files.readAllBytes(new File("Keypair/publicKey").toPath());
            this.rsaPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            this.rsaPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new CustomTokenConverter();
//        converter.setSigningKey(privateKey);
//        converter.setVerifierKey(publicKey);
        converter.setSigner(new RsaSigner((RSAPrivateKey) this.rsaPrivateKey));
        converter.setVerifier(new RsaVerifier((RSAPublicKey) this.rsaPublicKey));
        return converter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenConverter());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore()).accessTokenConverter(tokenConverter());
    }

    @Bean
    public CustomTokenConverter customTokenEnhancer() {
        return new CustomTokenConverter();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CLIENT_ID)
                .secret(CLIENT_SECRET)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, REFRESH_TOKEN)
                .scopes("read", "write")
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
}
