package com.microservice.erp.model;

import com.infoworks.lab.rest.models.Response;

public class LoginRetryCount extends Response {

    private final int maxRetryCount;
    private long lastRetryTimestamp;
    private int failCount = 0;

    public LoginRetryCount() {
        this(5);
    }

    public LoginRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public synchronized int incrementFailedCount(){
        lastRetryTimestamp = System.currentTimeMillis();
        return ++failCount;
    }

    public synchronized int resetFailedCount(){
        failCount = 0;
        return failCount;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public long getLastRetryTimestamp() {
        return lastRetryTimestamp;
    }

    public void setLastRetryTimestamp(long lastRetryTimestamp) {
        this.lastRetryTimestamp = lastRetryTimestamp;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public boolean isMaxTryExceed() {
        return getFailCount() >= getMaxRetryCount();
    }

    public boolean isTimePassed(long blockDurationInMillis) {
        return timeElapsed() >= blockDurationInMillis;
    }

    public long timeElapsed() {
        long timeElapsed = System.currentTimeMillis() - getLastRetryTimestamp();
        return timeElapsed;
    }
}
