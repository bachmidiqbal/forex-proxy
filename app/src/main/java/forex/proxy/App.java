package forex.proxy;

import java.net.http.HttpClient;
import forex.proxy.client.FxProviderClient;
import forex.proxy.config.Config;
import forex.proxy.constant.Constant;
import forex.proxy.controller.FxRatesController;
import forex.proxy.service.FxRatesService;
import forex.proxy.service.FxRatesServiceImpl;
import io.javalin.Javalin;

public class App {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        HttpClient httpClient = HttpClient.newHttpClient();
        long timeout = Long.parseLong(config.getHttpTimeout());
        FxProviderClient fxProviderClient = new FxProviderClient(config.getBaseUrl(), config.getHttpToken(), timeout,
                httpClient);
        FxRatesService fxRatesService = new FxRatesServiceImpl(fxProviderClient);
        FxRatesController fxRatesController = new FxRatesController(fxRatesService);

        int port = Integer.parseInt(config.getAppPort());
        Javalin app = Javalin.create().start(port);
        app.get(Constant.RATES, fxRatesController);
    }
}
