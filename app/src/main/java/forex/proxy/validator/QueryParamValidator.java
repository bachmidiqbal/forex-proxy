package forex.proxy.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import forex.proxy.constant.Constant;
import io.javalin.http.Context;

public class QueryParamValidator implements Validator {

    private final Map<String, String> currencies = new HashMap<>() {
        {
            put(Constant.AUD, Constant.AUD);
            put(Constant.CAD, Constant.CAD);
            put(Constant.CHF, Constant.CHF);
            put(Constant.EUR, Constant.EUR);
            put(Constant.GBP, Constant.GBP);
            put(Constant.NZD, Constant.NZD);
            put(Constant.JPY, Constant.JPY);
            put(Constant.SGD, Constant.SGD);
            put(Constant.USD, Constant.USD);
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
                return Constant.ERR2;
            }
        }
        return null;
    }

}
