package forex.proxy;

import forex.proxy.client.FxProviderClient;
import forex.proxy.constant.Constant;
import forex.proxy.controller.FxRatesController;
import forex.proxy.service.FxRatesService;
import forex.proxy.service.FxRatesServiceImpl;
import io.javalin.Javalin;
import okhttp3.OkHttpClient;

public class App {
    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FxProviderClient fxProviderClient = new FxProviderClient(okHttpClient, "http://0.0.0.0:8080", "10dc303535874aeccc86a8251e6992f5");
        FxRatesService fxRatesService = new FxRatesServiceImpl(fxProviderClient);
        FxRatesController fxRatesController = new FxRatesController(fxRatesService);

        Javalin app = Javalin.create().start(7070);
        app.get(Constant.RATES, fxRatesController);
    }
}
