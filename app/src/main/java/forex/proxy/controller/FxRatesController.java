package forex.proxy.controller;

import java.util.List;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forex.proxy.constant.Constant;
import forex.proxy.model.FxRateResponse;
import forex.proxy.service.FxRatesService;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class FxRatesController implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(FxRatesController.class);
    private FxRatesService fxRatesService;

    public FxRatesController(FxRatesService fxRatesService) {
        this.fxRatesService = fxRatesService;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        logger.info(ctx.req.toString());
        List<String> pairs = ctx.queryParams(Constant.PAIR);
        FxRateResponse fxRateResponse = (FxRateResponse) fxRatesService.getRates(pairs);
        Gson gson = new Gson();
        ctx.result(gson.toJson(fxRateResponse));
    }
}
