package com.suneesh.trading.monitor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainApplication {
    static int count;
    public static Process startAutoTradingApplication(){
        log.info("Starting application count = {}",++count);
        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("notepad.exe");

        try {
            process = processBuilder.start();
            boolean b = process.waitFor(1, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return process;
    }
    public static void main(String[] args) {
        log.info("Starting Automated Trading Application monitor");
        Process process = startAutoTradingApplication();

        while (process!=null)
        {
            while(process.isAlive()){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!process.isAlive()){
                process = startAutoTradingApplication();
            }
        }
    }
}
