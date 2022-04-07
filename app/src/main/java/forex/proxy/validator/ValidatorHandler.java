package forex.proxy.validator;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forex.proxy.model.FxRateResponse;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ValidatorHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorHandler.class);
    private Validator[] validators;

    public ValidatorHandler(Validator[] validators) {
        this.validators = validators;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        for (Validator validator : validators) {
            String result = validator.validate(ctx);
            if (result != null) {
                logger.error(result);
                FxRateResponse fxRateResponse = new FxRateResponse();
                fxRateResponse.setErrorMessage(result);
                Gson gson = new Gson();
                throw new BadRequestResponse(gson.toJson(fxRateResponse));
            }
        }
    }

    public Validator[] getValidators() {
        return this.validators;
    }

    public void setValidators(Validator[] validators) {
        this.validators = validators;
    }

}
