package forex.proxy.config;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final String EMPTY = "";
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private String baseUrl;
    private String httpToken;
    private String httpTimeout;
    private String appPort;
    private String appSecret;

    public Config() throws Exception {
        List<String> errs = new ArrayList<String>();

        this.baseUrl = System.getenv("FX_PROXY_BASE_URL");
        if (this.baseUrl == null || EMPTY.equals(this.baseUrl)) {
            errs.add("envar FX_PROXY_BASE_URL is missing");
        }
        this.httpToken = System.getenv("FX_PROXY_HTTP_TOKEN");
        if (this.httpToken == null || EMPTY.equals(this.httpToken)) {
            errs.add("envar FX_PROXY_HTTP_TOKEN is missing");
        }
        this.httpTimeout = System.getenv("FX_PROXY_HTTP_TIMEOUT");
        if (this.httpTimeout == null || EMPTY.equals(this.httpTimeout)) {
            errs.add("envar FX_PROXY_HTTP_TIMEOUT is missing");
        }
        this.appPort = System.getenv("FX_PROXY_APP_PORT");
        if (this.appPort == null || EMPTY.equals(this.appPort)) {
            errs.add("envar FX_PROXY_APP_PORT is missing");
        }
        this.appSecret = System.getenv("FX_PROXY_APP_SECRET");
        if (this.appSecret == null || EMPTY.equals(this.appSecret)) {
            errs.add("envar FX_PROXY_APP_SECRET is missing");
        }

        for (String err : errs) {
            logger.error(err);
        }

        if (errs.size() != 0) {
            throw new Exception("Missing envar!");
        }
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getHttpToken() {
        return this.httpToken;
    }

    public void setHttpToken(String httpToken) {
        this.httpToken = httpToken;
    }

    public String getHttpTimeout() {
        return this.httpTimeout;
    }

    public void setHttpTimeout(String httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

    public String getAppPort() {
        return this.appPort;
    }

    public void setAppPort(String appPort) {
        this.appPort = appPort;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

}
