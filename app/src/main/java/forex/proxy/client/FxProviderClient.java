package forex.proxy.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forex.proxy.constant.Constant;

public class FxProviderClient {
    private static final Logger logger = LoggerFactory.getLogger(FxProviderClient.class);
    private String baseUrl;
    private String token;
    private long timeout;
    private HttpClient httpClient;

    public FxProviderClient(String baseUrl, String token, long timeout, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.token = token;
        this.timeout = timeout;
        this.httpClient = httpClient;
    }

    public String getRates(List<String> pairs) {
        String queryParams = getQueryParams(pairs);
        String uriString = this.baseUrl + Constant.RATES;
        if (pairs.size() > 0) {
            uriString += queryParams;
        }
        logger.info("uriString: " + uriString);
        String response;
        try {
            URI uri = new URI(uriString);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(this.timeout))
                    .header(Constant.TOKEN, this.token)
                    .GET()
                    .build();

            HttpResponse<String> httpResponse = this.httpClient.send(httpRequest, BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                logger.error("fxServerRC: " + httpResponse.statusCode());
                return null;
            }

            response = httpResponse.body();

        } catch (URISyntaxException | InterruptedException | IOException e) {
            logger.error(e.getMessage());
            return null;
        }

        return response;
    }

    private String getQueryParams(List<String> pairs) {
        String queryParams = "?";
        for (String pair : pairs) {
            queryParams += "pair=" + pair + "&";
        }

        return queryParams.substring(0, queryParams.length() - 1);
    }
}
