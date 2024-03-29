package com.rapl.curso.ws.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.rapl.curso.ws.security.CustomUserDetailsService;
import com.rapl.curso.ws.security.utils.PasswordUtils;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private TokenStore tokenStore = new InMemoryTokenStore();
	private String cliente = "cliente";
	private String clientSecret = "123";
	
	// tem que ser o mesmo nome atribuído na classe ResourceServerConfig
	private static final String RESOURCE_ID = "restservice";
	
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(this.tokenStore)
			.authenticationManager(this.authenticationManager)
			.userDetailsService(this.userDetailsService);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient(this.cliente)
			.secret(PasswordUtils.gerarBCrypt(this.clientSecret))
			.authorizedGrantTypes("password", "authorization_code", "refresh_token")
			.scopes("bar", "read", "write")
			.resourceIds(RESOURCE_ID)
			.accessTokenValiditySeconds(60 * 4)
			.refreshTokenValiditySeconds(60 * 60 * 24);
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setAccessTokenValiditySeconds(0);
		tokenServices.setTokenStore(this.tokenStore);
		return tokenServices;
	}

}
