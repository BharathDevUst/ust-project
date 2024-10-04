package com.ust.api_gateway.filter;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;

//    @Autowired
//    private RestClient restClient;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config{
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain)->{
            //for the uris NOT specified in the RouteValidator do the following steps
            if(validator.isSecured.test(exchange.getRequest())) {
                // check if the exchange request header contains the Authorization header
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing Authorization Header");
                }
                // take out the Authorization header
                String authHeaderToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeaderToken != null && authHeaderToken.startsWith("Bearer")) {
                    // remove Bearer from front
                    authHeaderToken = authHeaderToken.substring(7);
                }
                try {
                    // now consume /api/auth/validate/token of authentication-service using RestClient
                    // can keep this call in a separate JwtUtil class and call
                    WebClient.Builder webClient = WebClient.builder();
                    webClient.build().get().uri("http://localhost:8090/api/auth/validate/token?token="+authHeaderToken).retrieve().bodyToMono(Boolean.class).block();
                    // also instead of making a RestClient call for every request, we can validate the token here in api-gateway itself
                }catch(Exception e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException("Invalid Access!! : " + e.getMessage());
                }
            }
            //for other uris simply chain the request.
            return chain.filter(exchange);
        });
    }
}

