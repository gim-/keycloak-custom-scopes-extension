import com.example.KeycloakDevContainer;

/**
 * Used to launch Keycloak Docker container for extension testing and debugging.
 */
public class KeycloakDevRunner {

    @SuppressWarnings({"ResultOfMethodCallIgnored", "resource"})
    public static void main(String[] args) throws Exception {
        KeycloakDevContainer keyCloak = new KeycloakDevContainer();
        keyCloak.withFixedExposedPort(8180, 8080);
        keyCloak.withFixedExposedPort(8787, 8787);
        keyCloak.start();

        System.out.println("Keycloak Running, you can now attach your remote debugger!");
        System.in.read();
    }
}
