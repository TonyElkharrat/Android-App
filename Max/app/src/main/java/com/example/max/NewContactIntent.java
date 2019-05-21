package com.example.max;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class NewContactIntent extends Activity
{
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact_intent);

        final EditText NameOfTheNewContact = findViewById(R.id.NameOfTheNewContact) ;
        final EditText PhoneOfTheNewContact = findViewById(R.id.TelephoneOfTheNewContactID);
        ImageButton validation = findViewById(R.id.validationNewContactID);

        NameOfTheNewContact.setText(getIntent().getStringArrayExtra("New contact details")[0]);
        PhoneOfTheNewContact.setText(getIntent().getStringArrayExtra("New contact details")[1]);

        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = NameOfTheNewContact.getText().toString();
                String phone = PhoneOfTheNewContact.getText().toString();
                insertContact(name,phone);


            }
        });
    }

    public void insertContact(String name, String Phone) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, Phone);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
