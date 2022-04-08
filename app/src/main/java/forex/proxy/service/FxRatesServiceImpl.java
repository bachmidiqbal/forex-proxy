package forex.proxy.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forex.proxy.client.FxProviderClient;
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
        Set<String> pairSet = removeDuplicate(pairs);
        String res = fxProviderClient.getRates(pairSet);
        FxRateResponse fxRateResponse = new FxRateResponse();
        fxRateResponse.setErrorMessage("Failed to get response from FX Server!");

        if (res != null) {
            try {
                Gson gson = new Gson();
                FxRate[] fxRates = gson.fromJson(res, FxRate[].class);
                fxRateResponse.setFxRates(fxRates);
                fxRateResponse.setErrorMessage(null);
            } catch (JsonSyntaxException e) {
                logger.info("fxServerResponse: " + res);
                logger.error(e.getMessage());
            }
        }

        return fxRateResponse;
    }

    private Set<String> removeDuplicate(List<String> pairs) {
        Set<String> pairSet = new HashSet<>();
        for (String pair : pairs) {
            pairSet.add(pair);
        }

        return pairSet;
    }
}
