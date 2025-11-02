package com.maheshbhatt.financialnuggets.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public final class MoneyUtils {
    private static final Pattern NON_NUMERIC = Pattern.compile("[^0-9.\\-]");
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal ONE_LAKH = BigDecimal.valueOf(100_000);

    private MoneyUtils() {
    }

    /**
     * Parse an Indian-formatted amount like "₹1,23,45,678.90" or "1,23,45,678.90" to BigDecimal.
     */
    public static BigDecimal parseIndianAmount(String amountStr) {
        if (amountStr == null || amountStr.isBlank()) {
            throw new IllegalArgumentException("Amount string cannot be null or blank");
        }
        String cleaned = NON_NUMERIC.matcher(amountStr).replaceAll("");
        if (cleaned.isBlank()) {
            throw new IllegalArgumentException("No numeric value found in: " + amountStr);
        }
        BigDecimal amount = new BigDecimal(cleaned);
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Convert rupees to value in lakhs (1 lakh = 100000).
     */
    public static BigDecimal toLakhs(BigDecimal rupees) {
        return rupees.divide(ONE_LAKH, 6, RoundingMode.HALF_UP); // 6 decimal places for precision
    }

    /**
     * Convert paise (long) back to rupees BigDecimal scaled to 2 decimals.
     */
    public static BigDecimal fromPaise(long paise) {
        return BigDecimal.valueOf(paise).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    // to rupee from lakhs
    public static BigDecimal fromLakhsToRupees(BigDecimal lakhs) {
        return lakhs.multiply(ONE_LAKH).setScale(2, RoundingMode.HALF_UP);
    }
}