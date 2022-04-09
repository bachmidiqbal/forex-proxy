package forex.proxy.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import forex.proxy.model.FxRate;

@ExtendWith(MockitoExtension.class)
public class FxProviderClientTest {

    FxProviderClient fxProviderClient;
    @Mock
    HttpClient httpClient;
    @Mock
    HttpResponse<String> httpResponse;

    @Test
    @DisplayName("Should return FX rates when the request is successful")
    void testGetRates() throws Exception {
        String baseUrl = "http://test";
        String uriString = baseUrl + "/rates?pair=GBPUSD";
        String token = "token";
        long timeout = 30;
        String fxResponse = "[{\"from\":\"GBP\",\"to\":\"USD\",\"bid\":0.42890530850806075,\"ask\":0.12341870746956063,\"price\":0.27616200798881069,\"time_stamp\":\"2022-04-09T04:25:51.219Z\"}]";
        URI uri = new URI(uriString);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(timeout))
                .header("token", token)
                .GET()
                .build();

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(fxResponse);
        when(httpClient.send(httpRequest, BodyHandlers.ofString())).thenReturn(httpResponse);

        fxProviderClient = new FxProviderClient(baseUrl, token, timeout, httpClient);
        Set<String> pairs = new HashSet<>();
        pairs.add("GBPUSD");

        String res = fxProviderClient.getRates(pairs);

        Gson gson = new Gson();
        FxRate[] fxRate = gson.fromJson(res, FxRate[].class);

        assertEquals("GBP", fxRate[0].getFrom());
        assertEquals("USD", fxRate[0].getTo());
        assertEquals(0.42890530850806075, fxRate[0].getBid());
        assertEquals(0.12341870746956063, fxRate[0].getAsk());
        assertEquals(0.27616200798881069, fxRate[0].getPrice());
        assertEquals("2022-04-09T04:25:51.219Z", fxRate[0].getTimestamp());
    }

    @Test
    @DisplayName("Should return null when the response code isn't 200")
    void testGetRatesWithNon200RC() throws Exception {
        String baseUrl = "http://test";
        String uriString = baseUrl + "/rates?pair=GBPUSD";
        String token = "token";
        long timeout = 30;
        URI uri = new URI(uriString);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(timeout))
                .header("token", token)
                .GET()
                .build();

        when(httpResponse.statusCode()).thenReturn(400);
        when(httpClient.send(httpRequest, BodyHandlers.ofString())).thenReturn(httpResponse);

        fxProviderClient = new FxProviderClient(baseUrl, token, timeout, httpClient);
        Set<String> pairs = new HashSet<>();
        pairs.add("GBPUSD");

        String res = fxProviderClient.getRates(pairs);

        assertNull(res);
    }

    @Test
    @DisplayName("Should return null when there is exception caused by the client")
    void testGetRatesWithException() throws Exception {
        String baseUrl = "http://test";
        String uriString = baseUrl + "/rates?pair=GBPUSD";
        String token = "token";
        long timeout = 30;
        URI uri = new URI(uriString);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofSeconds(timeout))
                .header("token", token)
                .GET()
                .build();

        when(httpClient.send(httpRequest, BodyHandlers.ofString())).thenThrow(new IOException("IO Exception"));

        fxProviderClient = new FxProviderClient(baseUrl, token, timeout, httpClient);
        Set<String> pairs = new HashSet<>();
        pairs.add("GBPUSD");

        String res = fxProviderClient.getRates(pairs);

        assertNull(res);
    }
}
