package forex.proxy.model;

import com.google.gson.annotations.SerializedName;

public class FxRate {
    private String from;
    private String to;
    private double bid;
    private double ask;
    private double price;
    @SerializedName("time_stamp")
    private String timestamp;

    public FxRate() {
    }

    public FxRate(String from, String to, double bid, double ask, double price, String timestamp) {
        this.from = from;
        this.to = to;
        this.bid = bid;
        this.ask = ask;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getBid() {
        return this.bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return this.ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "{" +
            " from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", bid='" + getBid() + "'" +
            ", ask='" + getAsk() + "'" +
            ", price='" + getPrice() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            "}";
    }

}
