package com.suneesh.trading.monitor;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainApplication {
    static int count;
    public static Process startAutoTradingApplication(){
        log.info("Starting application count = {}",++count);
        Process process = null;
        ProcessBuilder processBuilder = new ProcessBuilder();
        Path pathToApplication = Paths.get("C:\\Windows\\System32");
//        String application ="notepad.exe";
        String commandToRun = "";
//
//        Path pathToApplication = Paths.get("C:\\Windows\\System32");
        String application ="runAutoTradingWindows.bat";
//        String commandToRun = "";

//        String finalCommand = commandToRun + " "+ pathToApplication + "\\" + application;
        String finalCommand = application;

        processBuilder.command(finalCommand);

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
