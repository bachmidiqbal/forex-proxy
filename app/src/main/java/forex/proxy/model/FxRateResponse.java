package forex.proxy.model;

public class FxRateResponse {
    private FxRate[] fxRates;
    private String status;

    public FxRateResponse() {
    }

    public FxRateResponse(FxRate[] fxRates, String status) {
        this.fxRates = fxRates;
        this.status = status;
    }

    public FxRate[] getFxRates() {
        return this.fxRates;
    }

    public void setFxRates(FxRate[] fxRates) {
        this.fxRates = fxRates;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                " fxRates='" + getFxRates() + "'" +
                ", status='" + getStatus() + "'" +
                "}";
    }

}
