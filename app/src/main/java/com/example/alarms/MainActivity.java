package com.example.alarms;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.AlarmClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //  public timeVal values[];
    int hour, minutes, amount, index = 0;
    EditText hourInput; // i
    EditText minuteInput;   // i
    EditText amountInput;
    EditText dayInput;
    //  timeVal temp;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    EditText number, friend;
    String message, day;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = findViewById(R.id.number);
        friend = findViewById(R.id.friend);
        // message = findViewById(R.id.message);
        send = findViewById(R.id.sendButton);

        send.setEnabled(false);
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            send.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }


        hourInput = (EditText) findViewById(R.id.hourInput);
        minuteInput = (EditText) findViewById(R.id.minuteInput);
        amountInput = (EditText) findViewById(R.id.alarmAmount);
        dayInput = (EditText) findViewById(R.id.dayInput);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.valueOf(amountInput.getText().toString());
                hour = Integer.valueOf(hourInput.getText().toString());
                minutes = Integer.valueOf(minuteInput.getText().toString());
                day = dayInput.getText().toString();
                if (day.equalsIgnoreCase("pm")) {
                    hour += 12;
                    day = "pm";
                    if (hour > 23) {
                        hour %= 24;
                    }
                }
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                startActivity(intent);


                for (int i = 1; i < amount; i++) {
                    if (minutes < 55) {
                        if (day.equalsIgnoreCase("pm")) {
                            hour += 12;
                            minutes += 5;
                            if (hour > 23) {
                                hour %= 24;
                                day = "am";
                            }
                            intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                            intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                            startActivity(intent);
                        } else if (day.equalsIgnoreCase("am")) {
                            minutes += 5;
                            if (hour > 23) {
                                hour %= 24;
                            }
                            intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                            intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                            startActivity(intent);
                        }
                    } else if (minutes >= 55) {
                        if (day.equalsIgnoreCase("pm")) {
                            hour += 12;
                            hour++;
                            if (hour > 22) {
                                hour %= 24;
                                day = "am";
                            }
                            minutes = 0;
                            intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                            intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                            startActivity(intent);
                        } else if (day.equalsIgnoreCase("am")) {
                            hour++;
                            if (hour > 23) {
                                hour %= 24;
                                day = "pm";
                            }
                            minutes = 0;
                            intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                            intent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
                            startActivity(intent);
                        }
                    }
                }


            }
        });

    }
    public void onSend(View v) {
        String phoneNumber = number.getText().toString();
        String name = friend.getText().toString();
        String hrTxt = Integer.toString(hour);
        String minTxt = Integer.toString(minutes);
        message = "Hello " + name + ", this message was sent by the SnoozePal program. If I don't" +
                " wake up by " + hrTxt + ":" + minTxt + day + ", could you come by and wake me up?";
        String smsMessage = message;

        if (checkPermission(Manifest.permission.SEND_SMS) && allFields()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Fill out all of the information please, then press set alarm.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean allFields() {
        if (hour != 0 && amount != 0 && number.getText().toString().length() != 0 &&
                friend.getText().toString().length() !=0 &&
                dayInput.getText().toString().length() != 0 )
            return true;
        return false;
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}
