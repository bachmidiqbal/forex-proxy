package forex.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import forex.proxy.cache.Cache;
import forex.proxy.cache.MapCache;
import forex.proxy.client.FxProviderClient;
import forex.proxy.constant.Constant;
import forex.proxy.controller.FxRatesController;
import forex.proxy.model.FxRate;
import forex.proxy.model.FxRateResponse;
import forex.proxy.service.FxRatesService;
import forex.proxy.service.FxRatesServiceImpl;
import forex.proxy.validator.QueryParamValidator;
import forex.proxy.validator.TokenValidator;
import forex.proxy.validator.Validator;
import forex.proxy.validator.ValidatorHandler;
import io.javalin.testtools.TestUtil;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

@ExtendWith(MockitoExtension.class)
class AppTest {

    FxRatesController fxRatesController;
    FxRatesService fxRatesService;
    @Mock
    FxProviderClient fxProviderClient;

    @Test
    @DisplayName("Should return FX rates when request is successful")
    void testApp() {
        String expectedResponse = "[{\"from\":\"USD\",\"to\":\"JPY\",\"bid\":0.6118225421857174,\"ask\":0.8243869101616611,\"price\":0.7181047261736893,\"time_stamp\":\"2022-04-09T12:42:41.851Z\"},"
                + "{\"from\":\"EUR\",\"to\":\"USD\",\"bid\":0.8435259660697864,\"ask\":0.4175532166907524,\"price\":0.6305395913802694,\"time_stamp\":\"2022-04-09T12:42:41.851Z\"},"
                + "{\"from\":\"GBP\",\"to\":\"USD\",\"bid\":0.1350922166954046,\"ask\":0.13871074418376472,\"price\":0.13690148043958467,\"time_stamp\":\"2022-04-09T12:42:41.851Z\"}]";
        Gson gson = new Gson();
        FxRate[] fxRates = gson.fromJson(expectedResponse, FxRate[].class);
        Cache<String, FxRate> cache = new MapCache<>();
        Set<String> pairs = new HashSet<>();
        pairs.add("USDJPY");
        pairs.add("EURUSD");
        pairs.add("GBPUSD");

        when(fxProviderClient.getRates(pairs)).thenReturn(expectedResponse);

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);
        fxRatesController = new FxRatesController(fxRatesService);

        TokenValidator tokenValidator = new TokenValidator("testToken");
        QueryParamValidator queryParamValidator = new QueryParamValidator();
        Validator[] validators = new Validator[] { tokenValidator, queryParamValidator };
        ValidatorHandler validatorHandler = new ValidatorHandler(validators);

        TestUtil.test((server, client) -> {
            server.before(Constant.RATES, validatorHandler);
            server.get(Constant.RATES, fxRatesController);

            HttpUrl httpUrl = HttpUrl
                    .parse("http://localhost:" + server.port() + "/rates?pair=GBPUSD&pair=EURUSD&pair=USDJPY");
            Map<String, String> map = new HashMap<>();
            map.put("token", "testToken");
            Headers headers = Headers.of(map);
            Map<Class<?>, ? extends Object> val = new HashMap<>();
            Request request = new Request(httpUrl, "GET", headers, null, val);

            Response response = client.request(request);

            FxRateResponse fxRateResponse = new FxRateResponse();
            fxRateResponse.setFxRates(fxRates);

            assertEquals(gson.toJson(fxRateResponse), response.body().string());
        });
    }

    @Test
    @DisplayName("Should return error message when token is not valid")
    void testAppWithInvalidToken() {
        Cache<String, FxRate> cache = new MapCache<>();

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);
        fxRatesController = new FxRatesController(fxRatesService);

        TokenValidator tokenValidator = new TokenValidator("testToken");
        QueryParamValidator queryParamValidator = new QueryParamValidator();
        Validator[] validators = new Validator[] { tokenValidator, queryParamValidator };
        ValidatorHandler validatorHandler = new ValidatorHandler(validators);

        TestUtil.test((server, client) -> {
            server.before(Constant.RATES, validatorHandler);
            server.get(Constant.RATES, fxRatesController);

            HttpUrl httpUrl = HttpUrl
                    .parse("http://localhost:" + server.port() + "/rates?pair=GBPUSD&pair=EURUSD&pair=USDJPY");
            Map<String, String> map = new HashMap<>();
            map.put("token", "invalidToken");
            Headers headers = Headers.of(map);
            Map<Class<?>, ? extends Object> val = new HashMap<>();
            Request request = new Request(httpUrl, "GET", headers, null, val);

            Response response = client.request(request);

            FxRateResponse fxRateResponse = new FxRateResponse();
            fxRateResponse.setErrorMessage("Invalid token!");

            assertEquals(new Gson().toJson(fxRateResponse), response.body().string());
        });
    }

    @Test
    @DisplayName("Should return error message when currency pair is not valid")
    void testAppWithInvalidCurrencyPairRequest() {
        Cache<String, FxRate> cache = new MapCache<>();

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);
        fxRatesController = new FxRatesController(fxRatesService);

        TokenValidator tokenValidator = new TokenValidator("testToken");
        QueryParamValidator queryParamValidator = new QueryParamValidator();
        Validator[] validators = new Validator[] { tokenValidator, queryParamValidator };
        ValidatorHandler validatorHandler = new ValidatorHandler(validators);

        TestUtil.test((server, client) -> {
            server.before(Constant.RATES, validatorHandler);
            server.get(Constant.RATES, fxRatesController);

            HttpUrl httpUrl = HttpUrl
                    .parse("http://localhost:" + server.port() + "/rates?pair=GBPUSD&pair=EURUSD&pair=USDIDR");
            Map<String, String> map = new HashMap<>();
            map.put("token", "testToken");
            Headers headers = Headers.of(map);
            Map<Class<?>, ? extends Object> val = new HashMap<>();
            Request request = new Request(httpUrl, "GET", headers, null, val);

            Response response = client.request(request);

            FxRateResponse fxRateResponse = new FxRateResponse();
            fxRateResponse.setErrorMessage("Invalid currency pair!");

            assertEquals(new Gson().toJson(fxRateResponse), response.body().string());
        });
    }

    @Test
    @DisplayName("Should return error message when there is failure on FX server")
    void testAppWithFailureOnFXServer() {
        Cache<String, FxRate> cache = new MapCache<>();
        Set<String> pairs = new HashSet<>();
        pairs.add("USDJPY");
        pairs.add("EURUSD");
        pairs.add("GBPUSD");

        when(fxProviderClient.getRates(pairs)).thenReturn(null);

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);
        fxRatesController = new FxRatesController(fxRatesService);

        TokenValidator tokenValidator = new TokenValidator("testToken");
        QueryParamValidator queryParamValidator = new QueryParamValidator();
        Validator[] validators = new Validator[] { tokenValidator, queryParamValidator };
        ValidatorHandler validatorHandler = new ValidatorHandler(validators);

        TestUtil.test((server, client) -> {
            server.before(Constant.RATES, validatorHandler);
            server.get(Constant.RATES, fxRatesController);

            HttpUrl httpUrl = HttpUrl
                    .parse("http://localhost:" + server.port() + "/rates?pair=GBPUSD&pair=EURUSD&pair=USDJPY");
            Map<String, String> map = new HashMap<>();
            map.put("token", "testToken");
            Headers headers = Headers.of(map);
            Map<Class<?>, ? extends Object> val = new HashMap<>();
            Request request = new Request(httpUrl, "GET", headers, null, val);

            Response response = client.request(request);

            FxRateResponse fxRateResponse = new FxRateResponse();
            fxRateResponse.setErrorMessage("Failed to get response from FX Server!");

            assertEquals(new Gson().toJson(fxRateResponse), response.body().string());
        });
    }
}
