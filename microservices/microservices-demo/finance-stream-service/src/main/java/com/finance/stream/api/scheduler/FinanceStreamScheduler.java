package com.finance.stream.api.scheduler;

import com.finance.stream.api.client.FinanceService;
import com.finance.stream.api.service.ProcessData;
import com.microservices.demo.config.FinanceDataStreamConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceStreamScheduler {
    private final FinanceService financeService;
    private final FinanceDataStreamConfig financeDataStreamConfig;

    private final ProcessData processData;

    @Scheduled(fixedRate = 20000)
    public void getBistInformation() {
        var startedTime =  System.currentTimeMillis();
        log.info("Bist information is getting with scheduler...");
        financeDataStreamConfig
                .getShareList()
                .forEach(bist ->
                {
                    var endeksUrl = bist.concat(financeDataStreamConfig.getUrlAppend());
                    var bistShare = financeService.getBISTInformation(endeksUrl);

                    processData.processData(bistShare);

                    if (Objects.isNull(bistShare))
                        log.error("Error on getting information... Try later !");

                    var logMessage = "Share: ".concat(bist) + " -> Last Value: ".concat(bistShare.get(0).last());
                    log.info(logMessage);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        log.error("Error in interrupted : {}",  e.getMessage());
                    }
                });
        var finishTime = BigDecimal.valueOf(System.currentTimeMillis() - startedTime)
                .divide(BigDecimal.valueOf(1000),2, RoundingMode.DOWN);
        log.info("Bist information is finish with scheduler, Total complete second is : {}", finishTime );

    }

}

