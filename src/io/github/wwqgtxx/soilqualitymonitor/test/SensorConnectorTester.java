package io.github.wwqgtxx.soilqualitymonitor.test;

import io.github.wwqgtxx.soilqualitymonitor.sensor.SensorConnector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by Administrator on 2016/5/24.
 */
public class SensorConnectorTester {
    public static void main(String args[]) throws Exception{
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        PrintStream stdout = System.out;
        SensorConnector sensorConnector = SensorConnector.getSensorConnector();
        stdout.println("input mode(server or client)");

        String mode = stdin.readLine();
        switch (mode){
            case "server":{
                stdout.println("input port");
                int port = Integer.parseInt(stdin.readLine());
                sensorConnector.initServerMode(port);
                break;
            }
            case "client":{
                stdout.println("input host");
                String host = stdin.readLine();
                stdout.println("input port");
                int port = Integer.parseInt(stdin.readLine());
                sensorConnector.initClientMode(host,port);
                break;
            }
            default:{
                stdout.println("error type");
                main(args);
            }
        }
        stdout.println("now please input string");
        while (true){
            stdout.println(sensorConnector.command(stdin.readLine()));
        }

    }
}
