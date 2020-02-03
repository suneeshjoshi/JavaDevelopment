package com.suneesh.trading.core.technical;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.suneesh.trading.database.DatabaseConnection;
import com.suneesh.trading.utils.AutoTradingUtility;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
public class BollingerBand {
    @SerializedName("identifier")
    @Expose
    long identifier;

    @SerializedName("average")
    @Expose
    double average;

    @SerializedName("standard_deviation")
    @Expose
    double standardDeviation;

    @SerializedName("upper_band")
    @Expose
    double upperBand;

    @SerializedName("lower_band")
    @Expose
    double lowerBand;

    @SerializedName("bandwidth")
    @Expose
    double bandWidth;

    @SerializedName("candle_id")
    @Expose
    long candleIdentifier;

    @SerializedName("candle_epoch_string")
    @Expose
    String candle_epoch_string;

    List<Map<String, String>> candleList;

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

//        candleList has to be in descending order
        candleIdentifier = Integer.parseInt(candleList.get(0).get("identifier"));
        candle_epoch_string = candleList.get(0).get("epoch_string");

    }

    public void writeToDB(DatabaseConnection databaseConnection){
        databaseInsertStringList().forEach(f->{
            databaseConnection.executeNoResultSet(f);
        });
    }

    public List<String> databaseInsertStringList(){
        String query = "INSERT INTO bollinger_band ( average, standard_deviation, upper_band, lower_band, bandwidth, candle_id, candle_epoch_string) VALUES " +
                "(" + getAverage() + "," +
                getStandardDeviation() + "," +
                getUpperBand() + "," +
                getLowerBand() + "," +
                getBandWidth() + "," +
                getCandleIdentifier() + "," +
                AutoTradingUtility.quotedString(getCandle_epoch_string()) +");";

//        log.info("query = {}",query);
        return Arrays.asList(query);
    }
}
