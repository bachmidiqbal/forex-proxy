package forex.proxy.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forex.proxy.cache.Cache;
import forex.proxy.client.FxProviderClient;
import forex.proxy.constant.Constant;
import forex.proxy.model.FxRate;
import forex.proxy.model.FxRateResponse;

public class FxRatesServiceImpl implements FxRatesService {
    private static final Logger logger = LoggerFactory.getLogger(FxRatesServiceImpl.class);
    private static final int FIVE_MINUTES = 300000;
    private FxProviderClient fxProviderClient;
    private Cache<String, FxRate> cache;

    public FxRatesServiceImpl(FxProviderClient fxProviderClient, Cache<String, FxRate> cache) {
        this.fxProviderClient = fxProviderClient;
        this.cache = cache;
    }

    @Override
    public FxRateResponse getRates(List<String> pairs) {
        FxRateResponse fxRateResponse = new FxRateResponse();
        fxRateResponse.setErrorMessage(Constant.ERR3);

        Set<String> pairSet = removeDuplicate(pairs);

        FxRate[] fRates = getFromCache(pairSet);

        if (fRates.length == pairSet.size()) {
            logger.info("Get currency rates from cache");
            fxRateResponse.setFxRates(fRates);
            fxRateResponse.setErrorMessage(null);
            return fxRateResponse;
        }

        String res = fxProviderClient.getRates(pairSet);

        if (res != null) {
            try {
                Gson gson = new Gson();
                FxRate[] fxRates = gson.fromJson(res, FxRate[].class);
                fxRateResponse.setFxRates(fxRates);
                fxRateResponse.setErrorMessage(null);
                addToCache(fxRates);
            } catch (JsonSyntaxException e) {
                logger.error(e.getMessage());
            }
        }

        return fxRateResponse;
    }

    private FxRate[] getFromCache(Set<String> pairSet) {
        List<FxRate> fList = new ArrayList<>();
        for (String pair : pairSet) {
            FxRate fxRate = this.cache.get(pair);
            if (fxRate != null) {
                long cacheTimestamp = Instant.parse(fxRate.getTimestamp()).toEpochMilli();
                long currentTimestamp = Instant.now().toEpochMilli();
                if (currentTimestamp - cacheTimestamp <= FIVE_MINUTES) {
                    fList.add(fxRate);
                }
            }
        }

        return fList.toArray(new FxRate[0]);
    }

    private void addToCache(FxRate[] fxRates) {
        for (FxRate fxRate : fxRates) {
            this.cache.set(fxRate.getFrom() + fxRate.getTo(), fxRate);
        }
    }

    private Set<String> removeDuplicate(List<String> pairs) {
        Set<String> pairSet = new HashSet<>();
        for (String pair : pairs) {
            pairSet.add(pair);
        }

        return pairSet;
    }
}
