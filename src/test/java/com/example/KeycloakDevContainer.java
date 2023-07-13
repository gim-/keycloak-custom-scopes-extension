package com.example;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.testcontainers.utility.MountableFile;

import java.util.Arrays;

/**
 * Customised Keycloak Docker container for extension development and debugging.
 */
public class KeycloakDevContainer extends KeycloakContainer {

    /**
     * Keycloak Docker image to use.
     */
    private static final String DEFAULT_IMAGE = "quay.io/keycloak/keycloak:22.0";

    public KeycloakDevContainer() {
        super(DEFAULT_IMAGE);
    }

    public void withFixedExposedPort(int hostPort, int containerPort) {
        super.addFixedExposedPort(hostPort, containerPort);
    }

    @Override
    protected void configure() {
        this.withExposedPorts(8080, 8443, 8787);
        this.withAdminUsername("admin");
        this.withAdminPassword("admin");
        this.withRealmImportFile("realm-export.json");
        this.withEnv("DEBUG_PORT", "*:8787");
        this.withEnv("KC_HEALTH_ENABLED", "true");
        this.withEnv("KC_METRICS_ENABLED", "true");

        super.configure();
        String[] commandParts = getCommandParts();
        String[] newCommandParts = Arrays.copyOf(commandParts, commandParts.length + 1);
        newCommandParts[newCommandParts.length - 1] = "--debug";
        setCommandParts(newCommandParts);

        String classesLocation = MountableFile.forClasspathResource(".").getResolvedPath()
                + "../../../kc-extension";
        this.createKeycloakExtensionProvider(classesLocation);
    }
}
