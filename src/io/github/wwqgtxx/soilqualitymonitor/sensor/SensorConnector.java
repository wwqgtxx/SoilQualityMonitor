package io.github.wwqgtxx.soilqualitymonitor.sensor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/5/23.
 */
public class SensorConnector {
    private SensorConnector(){

    }
    private static SensorConnector sensorConnector = new SensorConnector();
    private static Logger logger = LogManager.getLogger(SensorConnector.class);
    private ReentrantLock lock;
    private ConnectWorker connectWorker;
    private Thread connectThread;
    Random ra =new Random();

    public boolean isInit() {
        return isInit;
    }

    private boolean isInit = false;
    public static final SensorConnector getSensorConnector(){
        return sensorConnector;
    }
    public void initTestMode(){isInit = true;}
    public void initClientMode(String host,int port){
        initClientMode(host,port,30);
    }
    public void initClientMode(String host,int port,long timeout){
        initClientMode(host,port,timeout,TimeUnit.SECONDS,(in,out)->{
            String str = String.format("%04d",ra.nextInt(9999));
            logger.debug(str);
            out.println(str);
            //in.readLine();
        });
    }
    public void initClientMode(String host,int port,long timeout,TimeUnit unit,DoHeartbeat doHeartbeat){
        init(new ClientModeConnectWorker(host, port, timeout, unit, new ArrayBlockingQueue<>(1),new ArrayBlockingQueue<>(1),doHeartbeat));
    }
    public void initServerMode(int port){
        initServerMode(port, 2);
    }
    public void initServerMode(int port,long timeout){initServerMode(port, timeout,TimeUnit.SECONDS);}
    public void initServerMode(int port,long timeout,TimeUnit unit){
        initServerMode(port, timeout, unit,(in,out)->{
            //out.println();
            //in.readLine();
        });
    }
    public void initServerMode(int port,long timeout,TimeUnit unit,DoHeartbeat doHeartbeat){
        init(new ServerModeConnectWorker(port, timeout, unit, new ArrayBlockingQueue<>(1),new ArrayBlockingQueue<>(1),doHeartbeat));
    }
    private void init(ConnectWorker connectWorker){
        if(isInit){
            connectThread.interrupt();
        }
        this.lock = new ReentrantLock();
        this.connectThread = new Thread(connectWorker,"SensorConnector.ConnectWorker");
        this.connectThread.start();
        this.connectWorker = connectWorker;
        this.isInit = true;

    }

    public void destory(){
        if (isInit&&connectThread!=null&&connectThread.isAlive()&&!connectThread.isInterrupted())
            connectThread.interrupt();
    }
    public String command(String str){
        return command((in,out)->{
            logger.debug(str);
            out.println(str);
//            StringBuilder sb = new StringBuilder();
//            while (in.ready()){
//                int a = in.read();
//                if (a == '\r'){
//                    continue;
//                }
//                if (a == '\n'){
//                    if (sb.length() !=0){
//                        break;
//                    }
//                    else{
//                        continue;
//                    }
//                }
//                char c = (char)a;
//
//                sb.append(c);
//                //logger.debug(c);
//            }
//            String result = sb.toString();
            String result = in.readLine();
            logger.debug(result);
            return result;
        });
    }
    public String command(DoCommand doCommand){
        if (isInit){
            lock.lock();
            String rusult = "";
            try {
                connectWorker.getInputQueue().put(doCommand);
                rusult = connectWorker.getOutputQueue().take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                connectWorker.getInputQueue().clear();
                connectWorker.getOutputQueue().clear();
                lock.unlock();
            }
            return rusult;
        }
        else{
            throw new ConnectorNotInitException();
        }

    }

    private abstract class ConnectWorker implements Runnable{
        ArrayBlockingQueue<DoCommand> getInputQueue() {
            return inputQueue;
        }

        ArrayBlockingQueue<String> getOutputQueue() {
            return outputQueue;
        }

        private ArrayBlockingQueue<DoCommand> inputQueue;
        private ArrayBlockingQueue<String> outputQueue;
        private long timeout;
        private TimeUnit unit;
        private DoHeartbeat doHeartbeat;
        public ConnectWorker(long timeout, TimeUnit unit, ArrayBlockingQueue<DoCommand> inputQueue, ArrayBlockingQueue<String> outputQueue, DoHeartbeat doHeartbeat){
            this.timeout = timeout;
            this.unit = unit;
            this.inputQueue = inputQueue;
            this.outputQueue = outputQueue;
            this.doHeartbeat= doHeartbeat;
        }
        private void parseSocket(Socket socket) throws IOException, InterruptedException {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"ISO-8859-1"));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"ISO-8859-1")), true);
            while (true){
                DoCommand doCommand = inputQueue.poll(timeout,unit);
                try{
                    if(doCommand!=null) {
                        String result = doCommand.doCommand(in,out);
                        if (result==null){
                            inputQueue.put(doCommand);
                            throw new IOException("result is null");
                        }
                        outputQueue.put(result);
                    }
                    else{
                        logger.debug("do a Heartbeat!");
                        doHeartbeat.doHeartbeat(in,out);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private class ClientModeConnectWorker extends ConnectWorker{
        private String host;
        private int port;
        public ClientModeConnectWorker(String host, int port, long timeout, TimeUnit unit, ArrayBlockingQueue<DoCommand> inputQueue, ArrayBlockingQueue<String> outputQueuee, DoHeartbeat doHeartbeat){
            super(timeout, unit, inputQueue,outputQueuee, doHeartbeat);
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
                    isInterrupted = true;
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        isInterrupted = true;
                        e.printStackTrace();
                    }
                }
            }

        }
    }
    private class ServerModeConnectWorker extends ConnectWorker{
        private int port;
        public ServerModeConnectWorker(int port, long timeout, TimeUnit unit, ArrayBlockingQueue<DoCommand> inputQueue, ArrayBlockingQueue<String> outputQueue, DoHeartbeat doHeartbeat){
            super(timeout, unit, inputQueue,outputQueue, doHeartbeat);
            this.port = port;
        }

        @Override
        public void run() {
            boolean isInterrupted=false;
            ServerSocket serverSocket = null;
            Socket socket = null;
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
                    isInterrupted = true;
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }
    public class ConnectorNotInitException extends RuntimeException{}

    @FunctionalInterface
    public interface DoHeartbeat {
        void doHeartbeat(BufferedReader in,PrintWriter out) throws IOException;
    }
    @FunctionalInterface
    public interface DoCommand {
        String doCommand (BufferedReader in,PrintWriter out) throws IOException;
    }
}
