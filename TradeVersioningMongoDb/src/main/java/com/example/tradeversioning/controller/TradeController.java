package com.example.tradeversioning.controller;

import com.example.tradeversioning.model.Trade;
import com.example.tradeversioning.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/{tradeId}")
    public Trade saveTradeVersion(@PathVariable String tradeId, @RequestBody Map<String, Object> tradeData) {
        return tradeService.saveTradeVersion(tradeId, tradeData);
    }

    @PostMapping("/{tradeId}/execute")
    public Trade executeTrade(@PathVariable String tradeId) {
        return tradeService.executeTrade(tradeId);
    }

    @PostMapping("/{tradeId}/unwind")
    public Trade unwindTrade(@PathVariable String tradeId) {
        return tradeService.unwindTrade(tradeId);
    }

    @PostMapping("/{tradeId}/upsize/{additionalQuantity}")
    public Trade upsizeTrade(@PathVariable String tradeId, @PathVariable int additionalQuantity) {
        return tradeService.upsizeTrade(tradeId, additionalQuantity);
    }

    @GetMapping("/{tradeId}/latest")
    public Trade getLatestTrade(@PathVariable String tradeId) {
        return tradeService.getLatestTrade(tradeId);
    }

    @GetMapping("/{tradeId}/previous/{currentVersion}")
    public Trade getPreviousTrade(@PathVariable String tradeId, @PathVariable int currentVersion) {
        return tradeService.getPreviousVersion(tradeId, currentVersion);
    }
}