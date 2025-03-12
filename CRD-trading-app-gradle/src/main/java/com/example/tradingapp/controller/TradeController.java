package com.example.tradingapp.controller;

import com.example.tradingapp.service.TradeService;
import com.example.tradingapp.model.Trade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {
    
    private final TradeService tradeService;

    @PostMapping("/add")
    public ResponseEntity<Trade> addTrade(@RequestBody String tradeJson) {
        try {
            return ResponseEntity.ok(tradeService.saveTrade(tradeJson));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/load")
    public ResponseEntity<Trade> getTrade(@RequestParam(name="tradeId") String tradeId, @RequestParam(name="version") int version) {
        return tradeService.getTrade(tradeId, version)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTrade(@RequestParam(name="tradeId") String tradeId, @RequestParam(name="version") int version) {
        tradeService.deleteTrade(tradeId, version);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/versions")
    public ResponseEntity<List<Trade>> getTradeVersions(@RequestParam(name="tradeId") String tradeId) {
        return ResponseEntity.ok(tradeService.getTradeVersions(tradeId));
    }
}