package forex.proxy.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

@ExtendWith(SystemStubsExtension.class)
public class ConfigTest {

    Config config;
    @SystemStub
    EnvironmentVariables environmentVariables;

    @Test
    @DisplayName("Should be able to get environment variables")
    void testConfig() throws Exception {
        environmentVariables.set("FX_PROXY_BASE_URL", "http://test");
        environmentVariables.set("FX_PROXY_HTTP_TOKEN", "token");
        environmentVariables.set("FX_PROXY_HTTP_TIMEOUT", "30");
        environmentVariables.set("FX_PROXY_APP_PORT", "7070");
        environmentVariables.set("FX_PROXY_APP_SECRET", "secret");

        config = new Config();

        assertEquals("http://test", config.getBaseUrl());
        assertEquals("token", config.getHttpToken());
        assertEquals("30", config.getHttpTimeout());
        assertEquals("7070", config.getAppPort());
        assertEquals("secret", config.getAppSecret());
    }

    @Test
    @DisplayName("Should throw exception when environment variables are missing or empty")
    void testConfigWithException() throws Exception {
        environmentVariables.set("FX_PROXY_BASE_URL", "");

        assertThrows(Exception.class, () -> config = new Config());
    }
}
