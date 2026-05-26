package com.optimizers.backend.dto.response;

public class TrendDataDTO {

    private String period;
    private long count;

    public TrendDataDTO() {
    }

    public TrendDataDTO(String period, long count) {
        this.period = period;
        this.count = count;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}