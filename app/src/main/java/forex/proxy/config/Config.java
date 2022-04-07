package forex.proxy.config;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private String baseUrl;
    private String httpToken;
    private String httpTimeout;
    private String appPort;

    public Config() throws Exception {
        List<String> errs = new ArrayList<String>();

        this.baseUrl = System.getenv("FX_PROXY_BASE_URL");
        if (this.baseUrl == null) {
            errs.add("envar FX_PROXY_BASE_URL is empty");
        }
        this.httpToken = System.getenv("FX_PROXY_HTTP_TOKEN");
        if (this.httpToken == null) {
            errs.add("envar FX_PROXY_HTTP_TOKEN is empty");
        }
        this.httpTimeout = System.getenv("FX_PROXY_HTTP_TIMEOUT");
        if (this.httpTimeout == null) {
            errs.add("envar FX_PROXY_HTTP_TIMEOUT is empty");
        }
        this.appPort = System.getenv("FX_PROXY_APP_PORT");
        if (this.appPort == null) {
            errs.add("envar FX_PROXY_APP_PORT is empty");
        }

        for (String err : errs) {
            logger.error(err);
        }

        if (errs.size() != 0) {
            throw new Exception("Missing envar");
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

}
