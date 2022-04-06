package forex.proxy.service;

import java.io.IOException;

import forex.proxy.model.FxRate;
import forex.proxy.model.FxRateResponse;

public interface FxRatesService {
    public FxRateResponse getRates(String[] pairs) throws IOException;
}
