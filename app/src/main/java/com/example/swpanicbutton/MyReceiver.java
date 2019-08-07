package com.example.swpanicbutton;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    String body, number;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle!=null){

            Object[] obj = (Object[])bundle.get("pdus");
            if (obj!=null){
                for (int i=0; i<obj.length; i++){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj[i]);
                    body = smsMessage.getMessageBody().toString();
                    number = smsMessage.getOriginatingAddress().toString();
                }
                String longitud = Main2Activity.getLongitud();
                String latitud = Main2Activity.getLatitud();
                if (body.compareTo("Sent from your Twilio trial account - I sent this message in under 10 minutes!")==0) {
                    Toast.makeText(context, "Localización enviada al C-5", Toast.LENGTH_LONG).show();
                    SmsManager sms = SmsManager.getDefault();
                    String mens2 = "lvlong " + longitud + "lat " + latitud;
                    sms.sendTextMessage("+13602007275", null, mens2, null, null);
                }else {

                    Toast.makeText(context, "Mensaje entrante"+body, Toast.LENGTH_LONG).show();
                }

            }
        }
    }

}
