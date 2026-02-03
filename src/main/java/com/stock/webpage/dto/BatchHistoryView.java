package com.stock.webpage.dto;

import java.time.LocalDate;

public interface BatchHistoryView {

    LocalDate getExecDate();
    String getType();   // "IN" / "OUT"
}
