package forex.proxy.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.javalin.http.Context;

@ExtendWith(MockitoExtension.class)
public class TokenValidatorTest {

    TokenValidator tokenValidator;
    @Mock
    Context ctx;

    @Test
    @DisplayName("Should return null when token is valid")
    void testValidate() {
        String secret = "test";

        tokenValidator = new TokenValidator(secret);

        when(ctx.header("token")).thenReturn("test");

        String result = tokenValidator.validate(ctx);

        assertNull(result);
    }

    @Test
    @DisplayName("Should return error message when token is not valid")
    void testValidateWithInvalidToken() {
        String secret = "test";

        tokenValidator = new TokenValidator(secret);

        when(ctx.header("token")).thenReturn("test1");

        String result = tokenValidator.validate(ctx);

        assertEquals("Invalid token!", result);
    }
}
