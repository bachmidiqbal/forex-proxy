package forex.proxy.service;

import java.io.IOException;
import java.util.List;
import forex.proxy.model.FxRateResponse;

public interface FxRatesService {
    public FxRateResponse getRates(List<String> pairs) throws IOException;
}
