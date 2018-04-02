package com.connector.connectordialer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String message = "Waiting for call...";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new IncomeClient()).start();
        TextView tw = (TextView) findViewById(R.id.statusView);
        tw.setText(message);
        message = "";
        while (true){
            if(!message.isEmpty()){
                tw.setText(message);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+ message));
                startActivity(callIntent);
                break;
            }

        }


    }




    class IncomeClient  implements Runnable {


        @Override
        public void run() {
            try {
                Socket socket = new Socket("192.168.0.88", 1010);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                int i = 1;
                while (!bufferedReader.ready()){
                    System.out.println("wait, sec"+i);
                    i++;
                    Thread.sleep(1000);
                }
                message = bufferedReader.readLine();
                System.out.println("Receive message:"+message);


            } catch (IOException e) {
                System.out.println("Connection error");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getMessage() {
            return message;
        }
    }
}
