package com.example.max;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.BoringLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public   class MessageContactIntent extends CommunicationIntent
{
    final int READ_CONTACT_PERMISSION=12;
    final int Send_Message_PERMISSION=10;

    boolean v_hasContactpermission=false,v_hasMessagePermission;
    String m_MessageForTheContact =null;
    TextView nameOfTheContact_TV ;
    TextView phoneOfTheContact_TV ;
    EditText textForTheContact_ET;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

         nameOfTheContact_TV = findViewById(R.id.NameOfTheContactId);
         phoneOfTheContact_TV = findViewById(R.id.telephoneOfTheContactId);
         textForTheContact_ET = findViewById(R.id.chatID);
         ImageButton sendMessageButton = findViewById(R.id.sendMessageD);
         m_settingsButton = findViewById(R.id.PermissionID);

        m_NameOfTheContact = getIntent().getStringArrayExtra("contact details")[0];
        m_MessageForTheContact = getIntent().getStringArrayExtra("contact details")[1];

        CheckPermission(Manifest.permission.READ_CONTACTS,READ_CONTACT_PERMISSION);

        sendMessageButton.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View v) {

                 CheckPermission(Manifest.permission.SEND_SMS,Send_Message_PERMISSION);
             }
         });

        m_settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                settingsIntent.setData(Uri.parse("package:"+getPackageName()));
                startActivity(settingsIntent);
               CheckPermission(Manifest.permission.READ_CONTACTS,READ_CONTACT_PERMISSION);

            }
        });

    }



    public void sendMessage()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage   (phoneOfTheContact_TV.getText().toString(), null, m_MessageForTheContact, null, null);
        String s = phoneOfTheContact_TV.getText().toString();
        Toast.makeText(this,"Message Sent ",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == READ_CONTACT_PERMISSION)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                String s  = GetContactPhoneNumber(m_NameOfTheContact, this);
                phoneOfTheContact_TV.setText( GetContactPhoneNumber(m_NameOfTheContact, this));
                nameOfTheContact_TV.setText(m_NameOfTheContact);
                textForTheContact_ET.setText("           "+m_MessageForTheContact);
                m_settingsButton.setVisibility(View.INVISIBLE);
                return;
            }

        }

        else if (requestCode == Send_Message_PERMISSION)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                sendMessage();
                return;
            }

        }

            m_settingsButton.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.Permission),Toast.LENGTH_SHORT).show();

    }

    public void CheckPermission(String i_ArrayRequest , int i_SpecificRequest)
    {
        if(Build.VERSION.SDK_INT>=23)
        {
            int permission= checkSelfPermission(i_ArrayRequest);
            if(i_SpecificRequest == READ_CONTACT_PERMISSION)
            {
                v_hasContactpermission=true;
                if (permission == PackageManager.PERMISSION_GRANTED)
                {
                    phoneOfTheContact_TV.setText(GetContactPhoneNumber(m_NameOfTheContact, this));
                    nameOfTheContact_TV.setText(m_NameOfTheContact);
                    textForTheContact_ET.setText(m_MessageForTheContact);
                    return;
                }
            }
            else if (i_SpecificRequest == Send_Message_PERMISSION)
                {
                    if (permission == PackageManager.PERMISSION_GRANTED)
                    {
                        m_settingsButton.setVisibility(View.INVISIBLE);
                        v_hasMessagePermission = true;
                        sendMessage();
                        return ;
                    }
                }

            requestPermissions(new String[]{i_ArrayRequest}, i_SpecificRequest);

    }
    }

}
