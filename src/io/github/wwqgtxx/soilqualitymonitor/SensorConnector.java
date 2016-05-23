package io.github.wwqgtxx.soilqualitymonitor;

import java.io.*;
import java.net.InetAddress;
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
    private static SensorConnector sensorConnector;
    private ArrayBlockingQueue<String> queue;
    private ReentrantLock lock;
    private Thread connectThread;
    private boolean isInit = false;
    public static SensorConnector getSensorConnector(){
        return sensorConnector;
    }
    public void init(String host,int port,long timeout,TimeUnit unit){
        queue = new ArrayBlockingQueue<>(1);
        lock = new ReentrantLock();
        connectThread = new Thread(new ConnectThread(host, port, timeout, unit),"SensorConnector.ConnectThread");
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
                queue.put(input);
                rusult = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            return rusult;
        }
        return "";
    }

    public class ConnectThread implements Runnable{
        private String host;
        private int port;
        private long timeout;
        private TimeUnit unit;
        public ConnectThread(String host,int port,long timeout,TimeUnit unit){
            this.host = host;
            this.port = port;
            this.timeout = timeout;
            this.unit = unit;
        }

        @Override
        public void run() {
            boolean isInterrupted=false;
            InetAddress addr;
            Socket socket = null;
            BufferedReader in;
            PrintWriter out;
            while (!isInterrupted){
                try {
                    addr = InetAddress.getByName(host);
                    socket = new Socket(addr, port);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    while (true){
                        String input = queue.poll(timeout,unit);
                        if(input!=null) {
                            out.println(queue.take());
                            String str = in.readLine();
                            queue.put(str);
                        }
                        else{
                            out.println();
                        }
                    }

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
}
