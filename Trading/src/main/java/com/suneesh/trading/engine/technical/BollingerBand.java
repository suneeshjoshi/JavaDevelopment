package com.suneesh.trading.engine.technical;

import com.suneesh.trading.database.DatabaseConnection;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class BollingerBand {
    List<Map<String, String>> candleList;
    double average;
    double standardDeviation;
    double upperBand;
    double lowerBand;
    double bandWidth;
    long candleIdentifier;
    final int bandMultiple=2;

    public BollingerBand(List<Map<String, String>> subList){
        this.candleList = subList;
    }

    public void calculate(){
        List<Double> closeList = candleList.stream().map(ele->Double.parseDouble(ele.get("close"))).collect(Collectors.toList());
        setAverage(Arrays.stream(closeList.toArray()).mapToDouble(f-> (double)f).average().orElse(Double.NaN));

        if(average!=Double.NaN){
            double stdDevTempSum=0D;
            for(double element : closeList){
                stdDevTempSum+= Math.pow(element-average,2);
            }
            standardDeviation = Math.sqrt(stdDevTempSum/ candleList.size());
            upperBand = average+(bandMultiple*standardDeviation);
            lowerBand = average-(bandMultiple*standardDeviation);
            bandWidth = upperBand-lowerBand;
        }
        candleIdentifier = Integer.parseInt(candleList.get(candleList.size()-1).get("identifier"));

    }

    public void writeToDB(DatabaseConnection databaseConnection){
        databaseInsertStringList().forEach(f->{
            databaseConnection.executeNoResultSet(f);
        });
    }

    public List<String> databaseInsertStringList(){
        return Arrays.asList("INSERT INTO bollinger_band ( average, standardDeviation, upperBand, lowerBand, bandWidth, candle_id) VALUES " +
                "(" + getAverage() + "," +
                getStandardDeviation() + "," +
                getUpperBand() + "," +
                getLowerBand() + "," +
                getBandWidth() + "," +
                getCandleIdentifier() + ");");
    }
}
