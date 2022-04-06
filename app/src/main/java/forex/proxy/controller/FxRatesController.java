package forex.proxy.controller;

import forex.proxy.model.FxRateResponse;
import forex.proxy.service.FxRatesService;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class FxRatesController implements Handler {

    private FxRatesService fxRatesService;

    public FxRatesController(FxRatesService fxRatesService) {
        this.fxRatesService = fxRatesService;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String[] arr = new String[] {"USDJPY"};
        FxRateResponse fxRateResponse = fxRatesService.getRates(arr);
        ctx.result(fxRateResponse.toString());
    }

}
