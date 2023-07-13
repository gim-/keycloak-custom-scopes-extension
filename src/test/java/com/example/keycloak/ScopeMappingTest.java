package com.example.keycloak;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScopeMappingTest extends KeycloakIntegrationTest {

    private static final String REALM = "test";

    @Test
    public void testScopeMapping() throws Exception {
        ValidatableResponse response = given()
                .param("client_id", "testclient")
                .param("client_secret", "testclient")
                .param("grant_type", "client_credentials")
            .when()
                .post(getTokenUrl(REALM))
            .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("access_token", notNullValue())
                .body("token_type", equalTo("Bearer"));

        String accessToken = response.extract().path("access_token");
        DecodedJWT jwt = JWT.decode(accessToken);

        Claim scopeClaim = jwt.getClaim("scope");
        assertFalse(scopeClaim.isNull());

        List<String> scopes = scopeClaim.asList(String.class);
        assertTrue(scopes.contains("s2s"));
    }
}
