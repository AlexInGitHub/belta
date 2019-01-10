package com.dmall;

import com.google.common.io.ByteStreams;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sinan.chen on 7/1/2019.
 */
public class MaxConnectionTest {

    static CountDownLatch latch = new CountDownLatch(2);
    static HttpClient client = HttpClientBuilder.create().setMaxConnTotal(2).build();

    public static void main(String[] args) throws Exception {
        for(int i=0; i<2; i++) {
            new Thread(new Worker()).start();
        }
        latch.await();
    }

    static class Worker implements Runnable {

        @Override
        public void run() {
            System.out.println("HttpClient is ready");
            HttpGet method = new HttpGet("http://localhost:8091/belta/greeting");
            try {
                HttpResponse response = client.execute(method);
                System.out.println(new String(ByteStreams.toByteArray(response.getEntity().getContent())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("HttpClient is Done");
            latch.countDown();
        }
    }

}
