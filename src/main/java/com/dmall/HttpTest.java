package com.dmall;

import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sinan.chen on 4/1/2019.
 */
public class HttpTest {

    static CountDownLatch latch = new CountDownLatch(2);


    static HttpClient client = HttpClientBuilder.create().setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
        @Override
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
            return 0;
        }
    }).build();
    static PipedOutputStream os = new PipedOutputStream();
    static PipedInputStream is;

    static {
        try {
            is = new PipedInputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        for(int i=0; i<1; i++) {
            new Thread(new Writer()).start();
            Thread.sleep(10 * 1000);
            new Thread(new Worker()).start();
        }

        latch.await();
    }

    static class Worker implements Runnable {

        @Override
        public void run() {
            System.out.println("HttpClient is ready");
            HttpPost method = new HttpPost("http://localhost:8091/belta/greeting");
            try {
                BasicHttpEntity entity = new BasicHttpEntity();
                entity.setContent(is);
                method.setEntity(entity);
                HttpResponse response = client.execute(method);
                System.out.println(new String(ByteStreams.toByteArray(response.getEntity().getContent())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("HttpClient is Done");
            latch.countDown();
        }
    }

    static class Writer implements Runnable {

        @Override
        public void run() {

            System.out.println("Writer is ready");
            long start = System.currentTimeMillis()/1000;
            for (int i=0; i<100 * 1024; i++) {
                try {
                    os.write(("Line"+i).getBytes());
                    os.flush();
                    long end = System.currentTimeMillis()/1000;
                    if(end - start > 0) {
                        System.out.println("Write " + i + " in " + (end-start) + " seconds");
                    }
                    start = end;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Writer is Done");
            latch.countDown();
        }
    }
}
