package forex.proxy.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.javalin.http.Context;

@ExtendWith(MockitoExtension.class)
public class QueryParamValidatorTest {

    QueryParamValidator queryParamValidator;
    @Mock
    Context ctx;

    @Test
    @DisplayName("Should return null when param is valid")
    void testValidate() {
        List<String> pairs = new ArrayList<>();
        pairs.add("EURUSD");

        queryParamValidator = new QueryParamValidator();

        when(ctx.queryParams("pair")).thenReturn(pairs);

        String result = queryParamValidator.validate(ctx);

        assertNull(result);
    }

    @Test
    @DisplayName("Should return error message when pair length is not 6")
    void testValidateWithInvalidPairLength() {
        List<String> pairs = new ArrayList<>();
        pairs.add("EUR/USD");

        queryParamValidator = new QueryParamValidator();

        when(ctx.queryParams("pair")).thenReturn(pairs);

        String result = queryParamValidator.validate(ctx);

        assertEquals("Invalid currency pair!", result);
    }

    @Test
    @DisplayName("Should return error message when base currency is not supported")
    void testValidateWithUnsupportedBaseCurrency() {
        List<String> pairs = new ArrayList<>();
        pairs.add("ABCUSD");

        queryParamValidator = new QueryParamValidator();

        when(ctx.queryParams("pair")).thenReturn(pairs);

        String result = queryParamValidator.validate(ctx);

        assertEquals("Invalid currency pair!", result);
    }

    @Test
    @DisplayName("Should return error message when quote currency is not supported")
    void testValidateWithUnsupportedQuoteCurrency() {
        List<String> pairs = new ArrayList<>();
        pairs.add("EURABC");

        queryParamValidator = new QueryParamValidator();

        when(ctx.queryParams("pair")).thenReturn(pairs);

        String result = queryParamValidator.validate(ctx);

        assertEquals("Invalid currency pair!", result);
    }
}
