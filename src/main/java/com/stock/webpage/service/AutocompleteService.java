package com.stock.webpage.service;

import java.util.List;
import java.util.Map;

public interface AutocompleteService {

    List<Map<String, String>> search(String keyword);

}
