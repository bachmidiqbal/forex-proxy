package forex.proxy.client;

import java.io.IOException;
import java.util.List;
import forex.proxy.constant.Constant;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FxProviderClient {
    private static final String TOKEN_HEADER = "token";
    private OkHttpClient okHttpClient;
    private String baseUrl;
    private String token;

    public FxProviderClient(OkHttpClient okHttpClient, String baseUrl, String token) {
        this.okHttpClient = okHttpClient;
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public String getRates(List<String> pairs) throws IOException {
        HttpUrl.Builder uBuilder = HttpUrl.parse(this.baseUrl + Constant.RATES).newBuilder();
        
        for (String pair : pairs) {
            uBuilder.addQueryParameter(Constant.PAIR, pair);
        }

        String url = uBuilder.build().toString();

        Request request = new Request.Builder()
            .url(url)
            .header(TOKEN_HEADER, this.token)
            .build();

        try (Response response = this.okHttpClient.newCall(request).execute();) {
            if (response.code() != 200) {
                return Constant.ERROR;
            }
            return response.body().string();
        }
    }
}
