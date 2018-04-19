package com.union.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    static final String CLIENT_ID = "client";
    static final String CLIENT_SECRET = "secret";
    static final String GRANT_TYPE_PASSWORD = "password";
    static final String REFRESH_TOKEN = "refresh_token";
    static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
    static final int REFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;
    private String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXS1tYFR1Kp1gV65sF/tX5iHh2\n" +
            "kgK1GtdZf/L1Mtjf1zX2dCiSLFNsb+upLdp8w2FXeesEcHGu9pGL/WKihpBkEdFG\n" +
            "AcsF31dGhx1SHnJiUGRrAz2zxCNvWWi3dZmTaCoPpNsmCAKzLe23kkhBrElR4xni\n" +
            "C4lzzR6t8fg4wQkYvQIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXAIBAAKBgQCXS1tYFR1Kp1gV65sF/tX5iHh2kgK1GtdZf/L1Mtjf1zX2dCiS\n" +
            "LFNsb+upLdp8w2FXeesEcHGu9pGL/WKihpBkEdFGAcsF31dGhx1SHnJiUGRrAz2z\n" +
            "xCNvWWi3dZmTaCoPpNsmCAKzLe23kkhBrElR4xniC4lzzR6t8fg4wQkYvQIDAQAB\n" +
            "AoGAbBCUVy8FtBwhq86U9UxX1BupR6/1JRzd8vyUMpW2GPRkg/vmOrpUgCDVKAsr\n" +
            "g3Ae6FeaCZqzaPCPd3luaX5KRUas9wBH34irAM/M9Myr5oEg4pXcaKvkEtPJnyZY\n" +
            "0yxgAIDEVJOHZugFiQBKRXh5ASOMJqNtjofg1Q72auMywfUCQQDMCBb8jdz6b/dR\n" +
            "fl+BdVm1sCkrQTUU0q8XzXGIeLB+BwuFfFbqRMHGm//XubDYPgh1KR53+1gaVAz3\n" +
            "cBMZFDInAkEAvdSHGYBiHHrjSKSEYtnq4DGsvP4KJJ9iCPrpcCfUM9KLGs9uf+ma\n" +
            "fx9a+pWIx5Vkfzsy5AFsbSv1GGqM/yMAewJAI+VAdgpXWFAeiN3c25/Tup1VgOCm\n" +
            "ABXY7C0ezk29b065/jAT8n9KQDDt3/wxWrn/Lu2fCKjVpTwoU9gJ5B2jfQJAMpUJ\n" +
            "9sKd4gbgvz+PQyPcESdLZwSuQTnzDn+FamxCgBiPFnLFd/IQR+VcAb+MtdnZ8Ike\n" +
            "s2CpuqFm6r2hmm6CNwJBAILZJlx83AwB7i4GfSRCcroLtnMUWWkqAbBc09h4/tmj\n" +
            "iZ3Tc+8ytZDjtTCTki/7F+OueHAwRKU3F2vzcqn1iuY=\n" +
            "-----END RSA PRIVATE KEY-----";

    @Autowired
    private ResourceLoader resourceLoader;

    public AuthorizationServerConfig() {
//        try {
//            File publicKeyFile = resourceLoader.getResource("classpath:keys\\publicKey.txt").getFile();
//            File privateKeyFile = resourceLoader.getResource("classpath:keys\\privateKey.txt").getFile();
//            Scanner publicScanner = new Scanner(publicKeyFile);
//            publicScanner.useDelimiter("\\Z");
//            publicKey = publicScanner.next();
//            Scanner privateScanner = new Scanner(privateKeyFile);
//            privateScanner.useDelimiter("\\Z");
//            privateKey = privateScanner.next();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(privateKey);
        converter.setVerifierKey(publicKey);
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

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(CLIENT_ID)
                .secret(CLIENT_SECRET)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, REFRESH_TOKEN)
                .scopes("read", "write")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .refreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
}
