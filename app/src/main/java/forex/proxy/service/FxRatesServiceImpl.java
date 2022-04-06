package forex.proxy.service;

import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import forex.proxy.client.FxProviderClient;
import forex.proxy.constant.Constant;
import forex.proxy.model.FxRate;
import forex.proxy.model.FxRateResponse;

public class FxRatesServiceImpl implements FxRatesService {

    private FxProviderClient fxProviderClient;

    public FxRatesServiceImpl(FxProviderClient fxProviderClient) {
        this.fxProviderClient = fxProviderClient;
    }

    @Override
    public FxRateResponse getRates(List<String> pairs) throws IOException {
        String res = fxProviderClient.getRates(pairs);
        FxRateResponse fxRateResponse = new FxRateResponse();

        if (Constant.ERROR.equals(res)) {
            fxRateResponse.setStatus(res);
        } else {
            Gson gson = new Gson();
            FxRate[] fxRates = gson.fromJson(res, FxRate[].class);
            fxRateResponse.setFxRates(fxRates);
            fxRateResponse.setStatus(Constant.SUCCESS);
        }

        return fxRateResponse;
    }
}
