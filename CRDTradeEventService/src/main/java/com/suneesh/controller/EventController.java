package com.suneesh.controller;

import com.suneesh.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/execute")
    public String publishExecuteEvent(@RequestParam String tradeId, @RequestParam int quantity) {
        eventService.publishExecuteEvent(tradeId, quantity);
        return "Execute Event Published";
    }

    @PostMapping("/unwind")
    public String publishUnwindEvent(@RequestParam String tradeId, @RequestParam int quantity) {
        eventService.publishUnwindEvent(tradeId, quantity);
        return "Unwind Event Published";
    }

    @PostMapping("/upsize")
    public String publishUpsizeEvent(@RequestParam String tradeId, @RequestParam int additionalQuantity) {
        eventService.publishUpsizeEvent(tradeId, additionalQuantity);
        return "Upsize Event Published";
    }

//    @GetMapping("/eventHistory")
//    public void getTradeEventHistory(String tradeId) {
//        Trade trade = trades.get(tradeId);
//        if (trade != null) {
//            int count=0;
//            System.out.println("Trade Event History for Trade ID: " + tradeId);
//            for (TradeEvent event : trade.getEventHistory()) {
//                System.out.println(++count + " : " + event.eventType() +" : "+ event.eventPayload());
//            }
//        }
//    }
}
