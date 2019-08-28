package com.ebay.assignment.creditlimittracker.tracker;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents A credit limit information for one specific person
 */
@Builder
@Getter
public class CreditLimitInfo {
    final String name;

    final String address;

    final String postcode;

    final String phone;

    final BigDecimal creditLimit;

    final LocalDate birthday;

    /**
     * Source of this credit limit information
     */
    final String source;
}
