package forex.proxy.validator;

import io.javalin.http.Context;

public interface Validator {
    public String validate(Context ctx);
}
