package com.example.setapakhouse.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateChangedReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {


        val isAirplanModeEnabled=intent?.getBooleanExtra("state",false)?:return
        if(isAirplanModeEnabled){
            Toast.makeText(context,"TIME CHANGED",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"TIME NOT CHANGED",Toast.LENGTH_SHORT).show()
        }
    }
}