package forex.proxy.validator;

import forex.proxy.constant.Constant;
import io.javalin.http.Context;

public class TokenValidator implements Validator {

    private String appSecret;

    public TokenValidator(String appSecret) {
        this.setAppSecret(appSecret);
    }

    @Override
    public String validate(Context ctx) {
        String token = ctx.header(Constant.TOKEN);
        if (!this.appSecret.equals(token)) {
            return Constant.ERR1;
        }
        return null;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
