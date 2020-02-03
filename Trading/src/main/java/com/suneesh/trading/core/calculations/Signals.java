package com.suneesh.trading.core.calculations;

import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.core.technical.BollingerBand;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Slf4j
public class Signals {
    final int BOLLINGER_BAND_DATA_COUNT=20;
    private DatabaseConnection databaseConnection;

    public Signals(DatabaseConnection databaseConnection){
        this.databaseConnection = databaseConnection;
    }

    public void calculateBollingerBands(List<Map<String, String>> candleDataFromDB, Optional<String> candleSource) {
        String source = candleSource.isPresent()?candleSource.get():"";

        if(CollectionUtils.isNotEmpty(candleDataFromDB)) {
            if(candleDataFromDB.size()>=BOLLINGER_BAND_DATA_COUNT) {
                int lastElementCounter = candleDataFromDB.size();
                if(source.equalsIgnoreCase("CANDLES")) {
                    lastElementCounter = candleDataFromDB.size()-1;
                }

                for(int i=lastElementCounter;i>=BOLLINGER_BAND_DATA_COUNT; --i){
                    int start = i-BOLLINGER_BAND_DATA_COUNT ;
                    int end = i;
//                    log.info("start : end = {} : {} ", start, end);

                    List<Map<String, String>> candleSubList = candleDataFromDB.subList(start, end);

                    BollingerBand bollingerBand = new BollingerBand(candleSubList);
                    bollingerBand.calculate();
//                    log.info(bollingerBand.toString());

                    bollingerBand.writeToDB(databaseConnection);
                }
            }
        }
        log.debug("Bollinger Band calculations done.");
    }

}
