package forex.proxy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import forex.proxy.cache.Cache;
import forex.proxy.cache.MapCache;
import forex.proxy.client.FxProviderClient;
import forex.proxy.model.FxRate;
import forex.proxy.model.FxRateResponse;

@ExtendWith(MockitoExtension.class)
public class FxRatesServiceImplTest {

    FxRatesService fxRatesService;
    @Mock
    FxProviderClient fxProviderClient;

    @Test
    @DisplayName("Should remove duplicate pairs and return FX rates when the request is successful")
    void testGetRates() {
        Cache<String, FxRate> cache = new MapCache<>();
        Set<String> pairs = new HashSet<>();
        pairs.add("GBPUSD");
        String response = "[{\"from\":\"GBP\",\"to\":\"USD\",\"bid\":0.42890530850806075,\"ask\":0.12341870746956063,\"price\":0.27616200798881069,\"time_stamp\":\"2022-04-09T04:25:51.219Z\"}]";

        when(fxProviderClient.getRates(pairs)).thenReturn(response);

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);

        List<String> pairList = new ArrayList<>();
        pairList.add("GBPUSD");
        pairList.add("GBPUSD");
        FxRateResponse fxRateResponse = (FxRateResponse) fxRatesService.getRates(pairList);
        FxRate fxRate = fxRateResponse.getFxRates()[0];

        assertEquals(2, pairList.size());
        assertEquals("GBP", fxRate.getFrom());
        assertEquals("USD", fxRate.getTo());
        assertEquals(0.42890530850806075, fxRate.getBid());
        assertEquals(0.12341870746956063, fxRate.getAsk());
        assertEquals(0.27616200798881069, fxRate.getPrice());
        assertEquals("2022-04-09T04:25:51.219Z", fxRate.getTimestamp());
        assertNull(fxRateResponse.getErrorMessage());
    }

    @Test
    @DisplayName("Should return FX rates from cache when the pair is present and not older than 5 minutes")
    void testGetRatesFromCache() {
        FxRate fRate = new FxRate();
        fRate.setFrom("GBP");
        fRate.setTo("USD");
        fRate.setBid(0.42890530850806075);
        fRate.setAsk(0.12341870746956063);
        fRate.setPrice(0.27616200798881069);
        String timestamp = Instant.now().toString();
        fRate.setTimestamp(timestamp);

        Cache<String, FxRate> cache = new MapCache<>();
        cache.set("GBPUSD", fRate);

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);

        List<String> pairList = new ArrayList<>();
        pairList.add("GBPUSD");
        FxRateResponse fxRateResponse = (FxRateResponse) fxRatesService.getRates(pairList);
        FxRate fxRate = fxRateResponse.getFxRates()[0];

        assertEquals("GBP", fxRate.getFrom());
        assertEquals("USD", fxRate.getTo());
        assertEquals(0.42890530850806075, fxRate.getBid());
        assertEquals(0.12341870746956063, fxRate.getAsk());
        assertEquals(0.27616200798881069, fxRate.getPrice());
        assertEquals(timestamp, fxRate.getTimestamp());
        assertNull(fxRateResponse.getErrorMessage());

        verify(fxProviderClient, never()).getRates(any());
    }

    @Test
    @DisplayName("Should return FX rates firstly from FX server then secondly from cache for the same currency and not older than 5 mins in cache")
    void testGetRatesFromFxServerThenCache() {
        Cache<String, FxRate> cache = new MapCache<>();
        Set<String> pairs = new HashSet<>();
        pairs.add("GBPUSD");
        String timestamp = Instant.now().toString();
        String response = "[{\"from\":\"GBP\",\"to\":\"USD\",\"bid\":0.42890530850806075,\"ask\":0.12341870746956063,\"price\":0.27616200798881069,\"time_stamp\":\""
                + timestamp + "\"}]";

        when(fxProviderClient.getRates(pairs)).thenReturn(response);

        assertEquals(0, cache.size());

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);

        List<String> pairList = new ArrayList<>();
        pairList.add("GBPUSD");
        FxRateResponse fxRateResponse = (FxRateResponse) fxRatesService.getRates(pairList);
        FxRate fxRate = fxRateResponse.getFxRates()[0];

        assertEquals("GBP", fxRate.getFrom());
        assertEquals("USD", fxRate.getTo());
        assertEquals(0.42890530850806075, fxRate.getBid());
        assertEquals(0.12341870746956063, fxRate.getAsk());
        assertEquals(0.27616200798881069, fxRate.getPrice());
        assertEquals(timestamp, fxRate.getTimestamp());
        assertNull(fxRateResponse.getErrorMessage());

        FxRateResponse fxRateResponse2 = (FxRateResponse) fxRatesService.getRates(pairList);
        FxRate fxRate2 = fxRateResponse2.getFxRates()[0];

        assertEquals("GBP", fxRate2.getFrom());
        assertEquals("USD", fxRate2.getTo());
        assertEquals(0.42890530850806075, fxRate2.getBid());
        assertEquals(0.12341870746956063, fxRate2.getAsk());
        assertEquals(0.27616200798881069, fxRate2.getPrice());
        assertEquals(timestamp, fxRate2.getTimestamp());
        assertNull(fxRateResponse2.getErrorMessage());

        assertEquals(1, cache.size());
        verify(fxProviderClient, times(1)).getRates(any());
    }

    @Test
    @DisplayName("Should return error message when client returns null")
    void testGetRatesWithError() {
        Cache<String, FxRate> cache = new MapCache<>();
        Set<String> pairs = new HashSet<>();
        pairs.add("GBPUSD");

        when(fxProviderClient.getRates(pairs)).thenReturn(null);

        fxRatesService = new FxRatesServiceImpl(fxProviderClient, cache);

        List<String> pairList = new ArrayList<>();
        pairList.add("GBPUSD");
        FxRateResponse fxRateResponse = (FxRateResponse) fxRatesService.getRates(pairList);

        assertEquals("Failed to get response from FX Server!", fxRateResponse.getErrorMessage());
        assertNull(fxRateResponse.getFxRates());
    }
}
