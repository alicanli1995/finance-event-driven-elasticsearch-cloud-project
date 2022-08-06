package com.finance.stream.api.client;

import com.finance.stream.api.dto.FinanceApiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "finance-stream-service",
             url = "https://www.isyatirim.com.tr/_layouts/15/Isyatirim.Website/Common/Data.aspx/OneEndeks?endeks=")
public interface FinanceService {

    @GetMapping()
    List<FinanceApiDTO> getBISTInformation(@RequestParam String endeks);
}
