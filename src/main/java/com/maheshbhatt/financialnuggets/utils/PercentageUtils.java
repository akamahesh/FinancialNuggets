package com.maheshbhatt.financialnuggets.utils;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class PercentageUtils {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal ONE_HUNDRED_THOUSAND = BigDecimal.valueOf(100_000);
    private static final int DEFAULT_SCALE = 6;

    private PercentageUtils(){}

    // Example usage:
    // BigDecimal frac = PercentageUtils.parsePercentToFraction("6.09%");
    // String s = PercentageUtils.formatFractionAsPercentString(frac, 2); // "6.09%"
    // long bps = PercentageUtils.fractionToBasisPoints(frac); // 609

    /**
     * Parse a string like "6.09%" (or "6.09") into a fraction BigDecimal (0.0609).
     */
    public static BigDecimal parsePercentToFraction(String percentStr) {
        Objects.requireNonNull(percentStr, "percentStr is null");
        String cleaned = percentStr.trim().replace("%", "");
        BigDecimal percent = new BigDecimal(cleaned);
        return percent.divide(ONE_HUNDRED, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Format fraction (0.0609) as percent string "6.09%".
     */
    public static String formatFractionAsPercentString(BigDecimal fraction, int decimalPlaces) {
        Objects.requireNonNull(fraction, "fraction is null");
        BigDecimal percent = fraction.multiply(ONE_HUNDRED);
        return percent.toPlainString() + "%";
    }


    /**
     * Convert fraction (0.0609) to basis points (609).
     */
    public static long fractionToBasisPoints(BigDecimal fraction) {
        Objects.requireNonNull(fraction, "fraction is null");
        return fraction.multiply(ONE_HUNDRED_THOUSAND).setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    /**
     * Convert basis points (609) to fraction (0.0609).
     */
    public static BigDecimal basisPointsToFraction(long bps) {
        return BigDecimal.valueOf(bps).divide(ONE_HUNDRED_THOUSAND, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

}
