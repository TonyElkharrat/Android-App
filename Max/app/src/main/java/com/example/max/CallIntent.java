package com.example.max;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.security.Permission;

public class CallIntent extends CommunicationIntent {
    final int READ_CONTACT_PERMISSION=12;
    final int CALL_PERMISSION = 1;
     EditText NameOfThecontactET;
     EditText TelephoneOfTheContactET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);

        m_settingsButton = findViewById(R.id.PermissionID);

       NameOfThecontactET = findViewById(R.id.contactNameForCallId);
        TelephoneOfTheContactET = findViewById(R.id.phoneNumberforCallid);
        ImageButton validationCall = findViewById(R.id.validationCallID);
        m_NameOfTheContact = getIntent().getStringExtra("name of contact");
        CheckPermission(Manifest.permission.READ_CONTACTS,READ_CONTACT_PERMISSION);

//        m_phoneNumberOfTheContact = GetContactPhoneNumber(m_NameOfTheContact, CallIntent.this);
//        NameOfThecontactET.setText(m_NameOfTheContact);
//        TelephoneOfTheContactET.setText(m_phoneNumberOfTheContact);


        validationCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             CheckPermission(Manifest.permission.CALL_PHONE,CALL_PERMISSION);
            }
        });

        m_settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                settingsIntent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(settingsIntent);
                CheckPermission(Manifest.permission.CALL_PHONE,CALL_PERMISSION);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                m_settingsButton.setVisibility(View.INVISIBLE);
                dialPhoneNumber(m_phoneNumberOfTheContact);
                return;
            } else
                {
                m_settingsButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Permission), Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_CONTACT_PERMISSION)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                m_phoneNumberOfTheContact=GetContactPhoneNumber(m_NameOfTheContact, this);
                TelephoneOfTheContactET.setText( m_phoneNumberOfTheContact);
                NameOfThecontactET.setText(m_NameOfTheContact);
                m_settingsButton.setVisibility(View.INVISIBLE);
                return;
            }
            else
            {
                m_settingsButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Permission), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void dialPhoneNumber(String i_phoneNumber)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + i_phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


public void CheckPermission(String i_ArrayRequest , int i_SpecificRequest)
{
    if(Build.VERSION.SDK_INT>=23)
    {
        int permission= checkSelfPermission(i_ArrayRequest);
        if(i_SpecificRequest == READ_CONTACT_PERMISSION)
        {
            //v_hasContactpermission=true;

            if (permission == PackageManager.PERMISSION_GRANTED)
            {
                m_phoneNumberOfTheContact=GetContactPhoneNumber(m_NameOfTheContact, this);
                TelephoneOfTheContactET.setText( GetContactPhoneNumber(m_NameOfTheContact, this));
                NameOfThecontactET.setText(m_NameOfTheContact);
                m_settingsButton.setVisibility(View.INVISIBLE);
                return;
            }
        }

        else if (i_SpecificRequest == CALL_PERMISSION)
        {
            if (permission == PackageManager.PERMISSION_GRANTED)
            {

                m_settingsButton.setVisibility(View.INVISIBLE);
                //v_hasMessagePermission = true;
                dialPhoneNumber(m_phoneNumberOfTheContact);
                return ;
            }
        }

        requestPermissions(new String[]{i_ArrayRequest}, i_SpecificRequest);
    }
}
}