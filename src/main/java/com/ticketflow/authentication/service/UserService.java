package com.ticketflow.authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketflow.authentication.controller.dto.UserCreationDTO;
import com.ticketflow.authentication.controller.dto.UserCredentialDTO;
import com.ticketflow.authentication.controller.dto.UserDTO;
import com.ticketflow.authentication.controller.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final VaultService vaultService;

    @Value("${KEYCLOAK_HOST:http://localhost:18080}")
    private String keycloakHost;


    public UserService(VaultService vaultService) {
        this.vaultService = vaultService;
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public String getToken(UserLoginDTO userLoginDTO) {
        String requestBody = getRequestBodyForToken(userLoginDTO);
        HttpRequest request = getHttpRequestForToken(requestBody);

        return makeRequest(request);
    }

    public UserCreationDTO buildAndCreateUser(UserCreationDTO userCreationDTO) {
        Set<UserCredentialDTO> credentials = new HashSet<>();

        UserCredentialDTO userCredential = UserCredentialDTO.builder()
                .type("password")
                .value(userCreationDTO.getPassword())
                .temporary(false)
                .build();

        credentials.add(userCredential);

        UserDTO user = UserDTO.builder()
                .enabled(true)
                .email(userCreationDTO.getEmail())
                .emailVerified(false)
                .firstName(userCreationDTO.getFirstName())
                .lastName(userCreationDTO.getLastName())
                .username(userCreationDTO.getUsername())
                .credentials(credentials)
                .build();

        createUser(user);

        return userCreationDTO;
    }

    private void createUser(UserDTO user) {
        String url = keycloakHost + "/auth/admin/realms/" + getRealmName() + "/users";

        // todo - deixar mais seguro
        UserLoginDTO userLoginDTO = UserLoginDTO.builder()
                .password("12345")
                .username("daniellygj")
                .build();

        String accessToken = getToken(userLoginDTO);

        try {
            String requestBody = objectMapper.writeValueAsString(user);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201) {
                // todo - lançar exception personalizada
                throw new RuntimeException("Falha ao criar o usuário: " + response.body());
            }
        } catch (Exception e) {
            // todo - lançar exception personalizada
            throw new RuntimeException("Erro ao criar o usuário", e);
        }
    }

    private String makeRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> responseJson = objectMapper.readValue(response.body(), Map.class);

            return (String) responseJson.get("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpRequest getHttpRequestForToken(String requestBody) {
        String url = getKeycloakUrl() + "/token";

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private String getRequestBodyForToken(UserLoginDTO userLoginDTO) {
        return "username=" + URLEncoder.encode(userLoginDTO.getUsername(), StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(userLoginDTO.getPassword(), StandardCharsets.UTF_8) +
                "&grant_type=" + URLEncoder.encode("password", StandardCharsets.UTF_8) +
                "&client_id=" + URLEncoder.encode(getClientId(), StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(getClientSecret(), StandardCharsets.UTF_8);
    }

    private String getClientSecret() {
//        String secretPath = "secret/development/AUTHENTICATION_CLIENT_SECRET";
//        return vaultService.getAuthenticationClientSecret(secretPath);
        return "45edaa89-16cb-41e2-aee5-970ab971ee9c";
    }

    private String getClientId() {
//        String secretPath = "secret/development/AUTHENTICATION_CLIENT_ID";
//        return vaultService.getAuthenticationClientSecret(secretPath);

        return "CouponManager-client";
    }

    private String getRealmName() {
//        String secretPath = "secret/development/REALM";
//        return vaultService.getAuthenticationClientSecret(secretPath);

        return "TicketFlow";
    }

    private String getKeycloakUrl() {
        String realmName = getRealmName();
        return keycloakHost + "/auth/realms/" + realmName + "/protocol/openid-connect";
    }

}





