package com.hdfcbank.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger custSeq = new AtomicInteger(1000);
    private static final AtomicInteger accSeq = new AtomicInteger(2000);
    private static final AtomicInteger txnSeq = new AtomicInteger(3000);

    public static String generateCustomerId() {
        return "CUST" + custSeq.getAndIncrement();
    }

    public static String generateAccountId() {
        return "ACC" + accSeq.getAndIncrement();
    }

    public static String generateTxnId() {
        return "TXN" + txnSeq.getAndIncrement();
    }
}
