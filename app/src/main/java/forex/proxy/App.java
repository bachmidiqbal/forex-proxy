package forex.proxy;

import java.net.http.HttpClient;
import forex.proxy.cache.Cache;
import forex.proxy.cache.MapCache;
import forex.proxy.client.FxProviderClient;
import forex.proxy.config.Config;
import forex.proxy.constant.Constant;
import forex.proxy.controller.FxRatesController;
import forex.proxy.model.FxRate;
import forex.proxy.service.FxRatesService;
import forex.proxy.service.FxRatesServiceImpl;
import forex.proxy.validator.QueryParamValidator;
import forex.proxy.validator.TokenValidator;
import forex.proxy.validator.Validator;
import forex.proxy.validator.ValidatorHandler;
import io.javalin.Javalin;

public class App {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        HttpClient httpClient = HttpClient.newHttpClient();
        long timeout = Long.parseLong(config.getHttpTimeout());
        FxProviderClient fxProviderClient = new FxProviderClient(config.getBaseUrl(), config.getHttpToken(), timeout,
                httpClient);
        Cache<String, FxRate> cache = new MapCache<>();
        FxRatesService fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);
        FxRatesController fxRatesController = new FxRatesController(fxRatesService);

        TokenValidator tokenValidator = new TokenValidator(config.getAppSecret());
        QueryParamValidator queryParamValidator = new QueryParamValidator();
        Validator[] validators = new Validator[] { tokenValidator, queryParamValidator };
        ValidatorHandler validatorHandler = new ValidatorHandler(validators);

        int port = Integer.parseInt(config.getAppPort());
        Javalin app = Javalin.create().start(port);
        app.before(Constant.RATES, validatorHandler);
        app.get(Constant.RATES, fxRatesController);
    }
}
