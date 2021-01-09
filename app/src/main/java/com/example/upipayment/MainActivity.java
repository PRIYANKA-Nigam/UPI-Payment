package com.example.upipayment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity { private static final int PAY_REQUEST = 1;EditText e1,e2,e3,e4,e5,e6;Button b;@Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);
        e1=(EditText)findViewById(R.id.editTextTextPersonName);e2=(EditText)findViewById(R.id.editTextTextPersonName2);e3=(EditText)findViewById(R.id.editTextTextPersonName3);
        e4=(EditText)findViewById(R.id.editTextTextPersonName4);e5=(EditText)findViewById(R.id.editTextTextPersonName5);e6=(EditText)findViewById(R.id.editTextTextPersonName6);
        b=(Button)findViewById(R.id.bt); b.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) { String name=e1.getText().toString();String upiId=e2.getText().toString();String amt=e3.getText().toString();
                String msg=e4.getText().toString();String trId=e5.getText().toString();String ReId=e6.getText().toString();
                if (name.isEmpty()||upiId.isEmpty()){ Toast.makeText(MainActivity.this,"Name & UPI ID is necessary",Toast.LENGTH_SHORT).show();
                }else { PayUsingUPI(name,upiId,amt,msg,trId,ReId); } }});
    }private  void PayUsingUPI(String name,String upiId,String amt,String msg,String trId,String ReId){
        Uri uri=new Uri.Builder().scheme("upi").authority("pay").appendQueryParameter("pa",upiId)
                .appendQueryParameter("name",name).appendQueryParameter("msg",msg).appendQueryParameter("amount",amt)
                .appendQueryParameter("transaction",trId).appendQueryParameter("reference",ReId).appendQueryParameter("cu","INR").build();
        Intent intent=new Intent(Intent.ACTION_VIEW);intent.setData(uri);Intent chooser=Intent.createChooser(intent,"Pay");
        if (chooser.resolveActivity(getPackageManager())!=null){ startActivityForResult(chooser,PAY_REQUEST);
        }else { Toast.makeText(this,"No UPI app found",Toast.LENGTH_SHORT).show(); } }@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PAY_REQUEST){ if (isInternetAvailable(MainActivity.this)){
    if (data==null){ ArrayList<String> dataList=new ArrayList<>();dataList.add("nothing");String temp="nothing";
        Toast.makeText(this,"Transaction Not completed!!!!",Toast.LENGTH_SHORT).show();
    }else { String text=data.getStringExtra("response");ArrayList<String> dataList=new ArrayList<>();dataList.add(text);upiPaymentCheck(text); } } } }
    void upiPaymentCheck(String data){ String str=data; String payment_cancel=""; String status="";String response[]=str.split("&");
       for (int i=0;i<response.length;i++){ String equalStr[]=response[i].split("");
           if (equalStr.length>=2){ if (equalStr[0].toLowerCase().equals("Status".toLowerCase())){ status=equalStr[1].toLowerCase(); }
           }else { payment_cancel="Payment Cancelled"; }
           if (status.equals("success")){ Toast.makeText(this,"Transaction Successfull",Toast.LENGTH_SHORT).show();
           }else if("Payment Cancelled".equals(payment_cancel)){ Toast.makeText(this,"payment cancelled by user",Toast.LENGTH_SHORT).show();
           }else { Toast.makeText(this,"Transaction failed!!!",Toast.LENGTH_SHORT).show(); } } }
    public static boolean isInternetAvailable(Context context){ ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){ NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo.isConnected() && networkInfo.isConnectedOrConnecting()&&networkInfo.isAvailable()){ return true; } }return true; }}