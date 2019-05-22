package com.example.max;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ImageButton;

import org.apache.commons.lang3.StringUtils;

public  class CommunicationIntent extends Activity
 {
    protected String m_NameOfTheContact=null;
     protected String m_phoneNumberOfTheContact=null;
     protected ImageButton m_settingsButton;




     public   String GetContactPhoneNumber (String i_nameOfTheContact, Context i_context)
     {

         String number="";


         Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
         String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                 ContactsContract.CommonDataKinds.Phone.NUMBER};

         Cursor people = this.getContentResolver().query(uri, projection, null, null, null);

         int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
         int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

         people.moveToFirst();
         do {
             String Name   = people.getString(indexName);
             String Number = people.getString(indexNumber);
             String s = i_nameOfTheContact.toUpperCase();
             Name = Name.toUpperCase();

             if(Name.startsWith(s))
             {
                 m_NameOfTheContact = Name;
                 return Number.replace("-", "");
             }

         } while (people.moveToNext());


         if(!number.equalsIgnoreCase(""))
         {
             return number.replace("-", "");
         }
         else if(number == "")
         {
             m_NameOfTheContact=getResources().getString(R.string.Contact_Not_found);
             return number;
         }
         else
         {
             return number;
         }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults)
     {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);

     }
 }
