package com.stock.webpage.service;

import com.stock.webpage.dto.KodexEtfSummaryDTO;
import java.util.List;

public interface KodexEtfSummaryService {

    List<KodexEtfSummaryDTO> getAllSummaryList();

    List<KodexEtfSummaryDTO> search(String keyword);
}
