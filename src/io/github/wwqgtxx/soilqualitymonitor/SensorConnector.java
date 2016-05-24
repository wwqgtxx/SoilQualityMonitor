package io.github.wwqgtxx.soilqualitymonitor;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SensorConnector {
    private SensorConnector(){}
    private static SensorConnector sensorConnector = new SensorConnector();
    private ArrayBlockingQueue<String> inputQueue;
    private ArrayBlockingQueue<String> outputQueue;
    private ReentrantLock lock;
    private Thread connectThread;

    public boolean isInit() {
        return isInit;
    }

    private boolean isInit = false;
    public static SensorConnector getSensorConnector(){
        return sensorConnector;
    }
    public void initClientMode(String host,int port){
        initClientMode(host,port,2);
    }
    public void initClientMode(String host,int port,long timeout){
        initClientMode(host,port,timeout,TimeUnit.MINUTES);
    }
    public void initClientMode(String host,int port,long timeout,TimeUnit unit){
        inputQueue = new ArrayBlockingQueue<>(1);
        outputQueue = new ArrayBlockingQueue<>(1);
        lock = new ReentrantLock();
        connectThread = new Thread(new ClientModeConnectThread(host, port, timeout, unit, inputQueue,outputQueue),"SensorConnector.ConnectThread");
        connectThread.start();
        isInit = true;
    }
    public void initServerMode(int port){
        initServerMode(port, 2);
    }
    public void initServerMode(int port,long timeout){
        initServerMode(port, timeout,TimeUnit.MINUTES);
    }
    public void initServerMode(int port,long timeout,TimeUnit unit){
        inputQueue = new ArrayBlockingQueue<>(1);
        outputQueue = new ArrayBlockingQueue<>(1);
        lock = new ReentrantLock();
        connectThread = new Thread(new ServerModeConnectThread(port, timeout, unit, inputQueue,outputQueue),"SensorConnector.ConnectThread");
        connectThread.start();
        isInit = true;
    }
    public void destory(){
        if (isInit&&connectThread!=null&&connectThread.isAlive()&&!connectThread.isInterrupted())
            connectThread.interrupt();
    }
    public String command(String input){
        if (isInit){
            lock.lock();
            String rusult = "";
            try {
                inputQueue.put(input);
                rusult = outputQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            return rusult;
        }
        else{
            throw new ConnectorNotInitException();
        }

    }

    private abstract class ConnectThread implements Runnable{
        private ArrayBlockingQueue<String> inputQueue;
        private ArrayBlockingQueue<String> outputQueue;
        private long timeout;
        private TimeUnit unit;
        public ConnectThread(long timeout,TimeUnit unit, ArrayBlockingQueue<String> inputQueue, ArrayBlockingQueue<String> outputQueue){
            this.timeout = timeout;
            this.unit = unit;
            this.inputQueue = inputQueue;
            this.outputQueue = outputQueue;
        }
        private void parseSocket(Socket socket) throws IOException, InterruptedException {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            while (true){
                String input = inputQueue.poll(timeout,unit);
                if(input!=null) {
                    out.println(input);
                    String str = in.readLine();
                    outputQueue.put(str);
                }
                else{
                    out.println();
                    in.readLine();
                }
            }
        }
    }

    private class ClientModeConnectThread extends ConnectThread implements Runnable{
        private String host;
        private int port;
        public ClientModeConnectThread(String host,int port,long timeout,TimeUnit unit, ArrayBlockingQueue<String> inputQueue, ArrayBlockingQueue<String> outputQueuee){
            super(timeout, unit, inputQueue,outputQueuee);
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            boolean isInterrupted=false;
            InetAddress addr;
            Socket socket = null;

            while (!isInterrupted){
                try {
                    addr = InetAddress.getByName(host);
                    socket = new Socket(addr, port);
                    super.parseSocket(socket);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally
                {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
    private class ServerModeConnectThread extends ConnectThread implements Runnable{
        private int port;
        public ServerModeConnectThread(int port,long timeout,TimeUnit unit, ArrayBlockingQueue<String> inputQueue, ArrayBlockingQueue<String> outputQueue){
            super(timeout, unit, inputQueue,outputQueue);
            this.port = port;
        }

        @Override
        public void run() {
            boolean isInterrupted=false;
            ServerSocket serverSocket = null;
            Socket socket = null;
            BufferedReader in;
            PrintWriter out;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!isInterrupted){
                try {
                    serverSocket = new ServerSocket(port);
                    socket = serverSocket.accept();
                    super.parseSocket(socket);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally
                {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
    public class ConnectorNotInitException extends  RuntimeException{}
}
