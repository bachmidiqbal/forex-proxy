package forex.proxy.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import forex.proxy.constant.Constant;
import io.javalin.http.Context;

public class QueryParamValidator implements Validator {

    private final Map<String, String> currencies = new HashMap<>() {
        {
            put("AUD", "AUD");
            put("CAD", "CAD");
            put("CHF", "CHF");
            put("EUR", "EUR");
            put("GBP", "GBP");
            put("NZD", "NZD");
            put("JPY", "JPY");
            put("SGD", "SGD");
            put("USD", "USD");
        }
    };

    public QueryParamValidator() {
    }

    @Override
    public String validate(Context ctx) {
        List<String> pairs = ctx.queryParams(Constant.PAIR);
        for (String pair : pairs) {
            if (pair.length() != 6 || currencies.get(pair.substring(0, 3)) == null
                    || currencies.get(pair.substring(3, 6)) == null) {
                return "Invalid currency pair!";
            }
        }
        return null;
    }

}
