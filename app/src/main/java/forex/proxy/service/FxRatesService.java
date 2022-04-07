package forex.proxy.service;

import java.util.List;

public interface FxRatesService {
    public Object getRates(List<String> pairs);
}
