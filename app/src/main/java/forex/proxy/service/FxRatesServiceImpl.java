package forex.proxy.service;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forex.proxy.client.FxProviderClient;
import forex.proxy.constant.Constant;
import forex.proxy.model.FxRate;
import forex.proxy.model.FxRateResponse;

public class FxRatesServiceImpl implements FxRatesService {
    private static final Logger logger = LoggerFactory.getLogger(FxRatesServiceImpl.class);
    private FxProviderClient fxProviderClient;

    public FxRatesServiceImpl(FxProviderClient fxProviderClient) {
        this.fxProviderClient = fxProviderClient;
    }

    @Override
    public FxRateResponse getRates(List<String> pairs) {
        String res = fxProviderClient.getRates(pairs);
        FxRateResponse fxRateResponse = new FxRateResponse();

        if (Constant.ERROR.equals(res)) {
            fxRateResponse.setStatus(res);
        } else {
            Gson gson = new Gson();
            try {
                FxRate[] fxRates = gson.fromJson(res, FxRate[].class);
                fxRateResponse.setFxRates(fxRates);
                fxRateResponse.setStatus(Constant.SUCCESS);
            } catch (JsonSyntaxException e) {
                logger.info("forexResponse: " + res);
                logger.error(e.getMessage());
                fxRateResponse.setStatus(Constant.ERROR);
            }

        }

        return fxRateResponse;
    }
}
