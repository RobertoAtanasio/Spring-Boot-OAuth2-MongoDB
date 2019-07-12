package com.rapl.curso.ws.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.Header;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//public class SwaggerConfig extends WebMvcConfigurationSupport {
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private String clientId;
	@Value("${app.client.secret}")
	private String clientSecret;
	@Value("${host.full.dns.auth.link}")
	private String authLink;

	private final ResponseMessage m201 = customMessage1();

	private final ResponseMessage m204put = simpleMessage(204, "Atualização ok");
	private final ResponseMessage m204del = simpleMessage(204, "Deleção ok");
	private final ResponseMessage m403 = simpleMessage(403, "Não autorizado");
	private final ResponseMessage m404 = simpleMessage(404, "Não encontrado");
	private final ResponseMessage m422 = simpleMessage(422, "Erro de validação");
	private final ResponseMessage m500 = simpleMessage(500, "Erro inesperado");

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, Arrays.asList(m403, m404, m500))
				.globalResponseMessage(RequestMethod.POST, Arrays.asList(m201, m403, m422, m500))
				.globalResponseMessage(RequestMethod.PUT, Arrays.asList(m204put, m403, m404, m422, m500))
				.globalResponseMessage(RequestMethod.DELETE, Arrays.asList(m204del, m403, m404, m500))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.rapl.curso.ws.resource"))
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(Collections.singletonList(securitySchema()))
				.securityContexts(Collections.singletonList(securityContext())).pathMapping("/") 
				.apiInfo(apiInfo())
				.tags(new Tag("UsuarioResource", "Endpoints para criar, retornar, atualizar e deletar usuários."),
					new Tag("tag2", "Tag 2 description."), 
					new Tag("tag2", "Tag 3 description."));
	}

	private ApiInfo apiInfo() {
		String titulo = "API do curso Spring Boot e Angular 8";
		String descricao = "Esta API é utilizada no curso de Spring Boot do prof. Hugo Silva";
		String versao = "Versão 1.0";
		String urlTermoServico = "https://www.udemy.com/terms";
		String nomeContato = "Hugo Silva";
		String urlContato = "udemy.com/user/hugo-silva";
		String email = "hugosilva.cursos@gmail.com";
		String licenca = "Permitido uso para estudantes";
		String urlLicenca = "https://www.udemy.com/terms";
		return new ApiInfo(titulo, descricao, versao, urlTermoServico, new Contact(nomeContato, urlContato, email),
				licenca, urlLicenca, Collections.emptyList());
	}

	private ResponseMessage simpleMessage(int code, String msg) {
		return new ResponseMessageBuilder().code(code).message(msg).build();
	}

	private ResponseMessage customMessage1() {
		Map<String, Header> map = new HashMap<>();
		map.put("location", new Header("location", "URI do novo recurso", new ModelRef("string")));
		return new ResponseMessageBuilder().code(201).message("Recurso criado").headersWithDescription(map).build();
	}

	private OAuth securitySchema() {
		List<AuthorizationScope> authorizationScopeList = new ArrayList();
		authorizationScopeList.add(new AuthorizationScope("read", "Ler tudo"));
		authorizationScopeList.add(new AuthorizationScope("write", "Acessar tudo"));
		List<GrantType> grantTypes = new ArrayList();
		GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant(authLink + "/oauth/token");
		grantTypes.add(creGrant);
		return new OAuth("oauth2schema", authorizationScopeList, grantTypes);
	}
	
	private SecurityContext securityContext() {
		return SecurityContext
				.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.ant("/api/users/**"))
				.build();
	}

	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[2];
		authorizationScopes[0] = new AuthorizationScope("read", "Ler tudo");
		authorizationScopes[1] = new AuthorizationScope("write", "Acessar tudo");
		return Collections.singletonList(new SecurityReference("oauth2schema",
				authorizationScopes));
	}
	
	@Bean
	public SecurityConfiguration securityInfo() {
		return SecurityConfigurationBuilder.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.scopeSeparator(" ")
				.useBasicAuthenticationWithAccessCodeGrant(true)
				.build();
	}



//	@Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
