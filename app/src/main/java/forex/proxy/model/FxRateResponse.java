package forex.proxy.model;

public class FxRateResponse {
    private FxRate[] fxRates;
    private String errorMessage;

    public FxRateResponse() {
    }

    public FxRateResponse(FxRate[] fxRates, String errorMessage) {
        this.fxRates = fxRates;
        this.errorMessage = errorMessage;
    }

    public FxRate[] getFxRates() {
        return this.fxRates;
    }

    public void setFxRates(FxRate[] fxRates) {
        this.fxRates = fxRates;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
