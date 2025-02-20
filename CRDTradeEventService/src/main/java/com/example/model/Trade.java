//package com.example.model;
//
//import com.example.events.TradeEvent;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Trade {
//    @Id
//    private Long id;
//    private String tradeId;
//    private int quantity;
//    List<TradeEvent> eventHistory;
//
//    public Trade(String tradeId){
//        this(tradeId,0,new java.util.ArrayList<>());
//    }
//
//    public Trade(String tradeId, int quantity) {
//        this(tradeId,quantity,new java.util.ArrayList<>());
//    }
//
//    public void addEvent(TradeEvent event) {
//        eventHistory.add(event);
//    }
//
//}
package com.example.model;

import com.example.events.TradeEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tradeId;
    private int quantity;

    // Store Event History
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "trade_event_history", joinColumns = @JoinColumn(name = "trade_id"))
    @Column(name = "event")
    private List<String> eventHistory = new ArrayList<>();

    public Trade(String tradeId){
        this.tradeId = tradeId;
        this.quantity = 0;
        this.eventHistory = new java.util.ArrayList<>();
    }

    public Trade(String tradeId, int quantity) {
        this.tradeId = tradeId;
        this.quantity = quantity;
        this.eventHistory = new java.util.ArrayList<>();
    }

    public void addEvent(TradeEvent event) {
        eventHistory.add(event.eventType() + ": " + event.eventPayload());
    }
}
