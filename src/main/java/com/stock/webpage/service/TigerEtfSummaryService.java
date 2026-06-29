package com.stock.webpage.service;

import com.stock.webpage.dto.TigerEtfSummaryDTO;
import java.util.List;

public interface TigerEtfSummaryService {

    List<TigerEtfSummaryDTO> getAllSummaryList();

    List<TigerEtfSummaryDTO> search(String keyword);

    List<TigerEtfSummaryDTO> searchPureEtf(String keyword);
}
