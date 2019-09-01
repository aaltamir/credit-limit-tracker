package com.ebay.assignment.creditlimittracker.datainput;

import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;

import java.util.List;

public interface DataReceiver {
    List<CreditLimitInfo> receive();
}
