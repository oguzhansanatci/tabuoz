package com.example.staha.tabuoz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NextActivity extends AppCompatActivity {

    private TextView mTextMessage;
    LinearLayout container;

    private List<Integer> listX = new ArrayList<Integer>();
    private List<Integer> listY = new ArrayList<Integer>();

    private Button btn_reset;
    private Button btn_send;
    private Button btn_start;

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String address = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        container = (LinearLayout) findViewById(R.id.buttons);
        btn_reset = (Button) findViewById(R.id.reset);
        btn_send = (Button) findViewById(R.id.send);
        btn_start = (Button) findViewById(R.id.start);

        btn_1 = (Button) findViewById(R.id._1);
        btn_2 = (Button) findViewById(R.id._2);
        btn_3 = (Button) findViewById(R.id._3);
        btn_4 = (Button) findViewById(R.id._4);
        btn_5 = (Button) findViewById(R.id._5);
        btn_6 = (Button) findViewById(R.id._6);
        btn_7 = (Button) findViewById(R.id._7);
        btn_8 = (Button) findViewById(R.id._8);
        btn_9 = (Button) findViewById(R.id._9);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<1>");
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<2>");
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<3>");
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<4>");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<5>");
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<6>");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<7>");
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<8>");
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMsg("<9>");
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReset();
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStart();
            }
        });

        /*for(int y = 0; y < 10; y ++){
            LinearLayout lin = new LinearLayout(this);
            lin.setLayoutParams(new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, 0, 1));
            lin.setBackgroundColor(Color.WHITE);
            for(int x = 0; x < 10; x ++){
                final int a = x;
                final int b = y;
                Button btn = new Button(this);
                btn.setHeight(0);
                AppBarLayout.LayoutParams q = new AppBarLayout.LayoutParams(0, AppBarLayout.LayoutParams.MATCH_PARENT, 1);
                btn.setLayoutParams(q);
                btn.generateViewId();
                //btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 50));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        oneBtnClick(a, b, v);
                    }
                });
                lin.addView(btn);
            }
            container.addView(lin);
        }*/

        Intent newint = getIntent();

        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);

        new ConnectBT().execute();
    }
    public void oneBtnClick(int x, int y, View v){
        listX.add(x);
        listY.add(y);
        String str;
        if(((Button)v).getText() == ""){
            str = String.valueOf(listX.size());
        }
        else{
            str = ((Button)v).getText().toString() + " - " + String.valueOf(listX.size());
        }
        ((Button)v).setText(str);
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(NextActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private void send(){
        changeMove(5, 5);
    }

    private void changeMove(Integer l, Integer r)
    {
        String xArray = "";
        String yArray = "";

        for(int i = 0; i < listX.size(); i++){
            xArray += listX.get(i).toString();
        }
        for(int i = 0; i < listY.size(); i++){
            yArray += listY.get(i).toString();
        }

        String msg = "<s><x>" + xArray + "<-x><y>" + yArray + "<-y><end>";
        sendMessage(msg);
    }
    public void sendStart(){
        String msg = "<start>";
        sendMessage(msg);
    }
    public void newMsg(String msg){
        sendMessage(msg);
    }
    public void sendReset(){
        String msg = "<reset>";
        sendMessage(msg);
    }
    private void sendMessage(String msg){
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(msg.toString().getBytes());
                //Toast.makeText(this, ("<start>l:" + a + "r:" + b + "<stop>").toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
}