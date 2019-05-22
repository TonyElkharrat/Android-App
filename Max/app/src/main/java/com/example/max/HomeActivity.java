package com.example.max;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.varunest.sparkbutton.SparkButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HomeActivity extends Activity implements TextToSpeech.OnInitListener
{


    private SparkButton buttonOfVocalAssistant;
    private final int CAMERA_REQUEST=2, SPEECH_REQUEST =3,WEBSITE_ADRESS_REQUEST=4,CITY_REQUEST=5,CONTACTS_REQUEST=6,Location_REQUEST=7,
    Build_REQUEST=9,ALARM_REQUEST=15,ALARM_MINUTES=16, ERROR_REQUEST=19,MESSAGE_TO_SEND_REQUEST =13,MESSAGE_OF_THE_CONTACT=14,NAME_OF_NEW_CONTACT_REQUEST=17,
      PHONE_NUMBER_OF_THE_NEW_CONTACT=18,NOTE_REQUEST=20,BODY_OF_NOTE_REQUEST=21,SONG_REQUEST=34,ARTIST_REQUEST=35,HOUR_END=32,HOUR_BEGIN=60;
    private int m_MinutesOfTheAlarm,m_HoursOfTheAlarm,hour_begin,hour_end;
    private static int  count =0;
    private String Location,m_NameOfTheContact,m_MessageOfTheContact,m_TitleOfTheNote=null,m_BodyOfTheNote=null,m_NameOfTheNewContact=null,m_PhoneNumberOfTheNewContact=null;;
    private ImageView resultImageView;

    private TextToSpeech textToSpeech;
    private boolean isAcall =false;
    private  String [] temp = new String[2];
    private String languageOs,Title,m_NameOfTheArtist,m_Song;
    private TextView sttHebrew;
    Actions actions ;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        buttonOfVocalAssistant = findViewById(R.id.spark_button);
        textToSpeech = new TextToSpeech(this,this);
        buttonOfVocalAssistant.setInactiveImage(R.drawable.voicerecognitioninactive);

        buttonOfVocalAssistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                buttonOfVocalAssistant.setInactiveImage(R.drawable.voicerecognitionactive);
                buttonOfVocalAssistant.playAnimation();
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        try{

                            Thread.sleep(700);
                            Intent intentRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intentRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,5);
                            intentRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT,getResources().getString(R.string.Max_Command));
                            startActivityForResult(intentRecognizeSpeech,SPEECH_REQUEST);
                        }catch (InterruptedException e)
                        {

                        }
                    }
                }.start();

            }

        });

        actions = new Actions(this);
        final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        resultImageView = findViewById(R.id.result_pic);
        ImageView helpbtn = findViewById(R.id.HelpButtonHomeID);
        sttHebrew = findViewById(R.id.HebrewSttID);
        ImageButton buttonUp = findViewById(R.id.upbtn);
        ImageButton buttonDown = findViewById(R.id.downbtn);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            }
        });


        helpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,DocumentationActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onInit(int status)
    {
        Set<String> a=new HashSet<>();
        a.add("male");//here you can give male if you want to select male voice.

        if (Build.VERSION.SDK_INT >= 21)
        {


            if(Locale.getDefault().getLanguage()=="fr" || Locale.getDefault().getCountry()=="FR")
            {
                Voice v = new Voice("fr-fr-x-frc-local", new Locale("fr", "FR"), 500, 400, true, a);
                textToSpeech.setVoice(v);
            }
            else if (Locale.getDefault().getCountry()=="GB"||Locale.getDefault().getCountry()=="US" ||Locale.getDefault().getLanguage()=="en" )
            {
                Voice v = new Voice("en-gb-x-rjs#male_1-local", new Locale("en", "GB"), 500, 400, true, a);
                textToSpeech.setVoice(v);

            }



        }


        GuiLangage(getResources().getString(R.string.Max_Presentation),4000,SPEECH_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

        if(requestCode==SPEECH_REQUEST&&resultCode==RESULT_OK)
        {
            CheckSpeechRecognition(results);
        }

        else if ( requestCode == WEBSITE_ADRESS_REQUEST && resultCode == RESULT_OK)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+results.get(0)+".com"));
            startActivity(intent);
        }

        else if ( requestCode == NAME_OF_NEW_CONTACT_REQUEST && resultCode == RESULT_OK)
        {
           GuiLangage(getResources().getString(R.string.Telephone_Request), 2500,PHONE_NUMBER_OF_THE_NEW_CONTACT);
            m_NameOfTheContact = results.get(0);

        }

        else if ( requestCode == PHONE_NUMBER_OF_THE_NEW_CONTACT && resultCode == RESULT_OK)
        {
            Intent newContactIntent = new Intent(HomeActivity.this,NewContactIntent.class);
            m_PhoneNumberOfTheNewContact = results.get(0);

            temp[0]= m_NameOfTheContact;
            temp[1]= m_PhoneNumberOfTheNewContact;
            newContactIntent.putExtra("New contact details",temp);
            startActivity(newContactIntent);
        }

        else if ( requestCode == CITY_REQUEST && resultCode == RESULT_OK)
        {
            try
            {
                GetLongitudeLatitudeFromACity(results);
            }
            catch(Exception e )
            {
                GuiLangage(getResources().getString(R.string.City_Exception), 2500, CITY_REQUEST);

            }
        }

        else  if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK )
        {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            resultImageView.setImageBitmap(bitmap );
        }

        else if(requestCode == CONTACTS_REQUEST && resultCode == RESULT_OK)
        {
            m_NameOfTheContact = results.get(0);
            if(!isAcall)
            {
                GuiLangage(getResources().getString(R.string.Message_Request), 2500, MESSAGE_OF_THE_CONTACT);
            }
            else
            {
                Intent callintent = new Intent(HomeActivity.this,CallIntent.class);
                 callintent.putExtra("name of contact",m_NameOfTheContact);
                 startActivity(callintent);
            }
        }


        else if(requestCode == BODY_OF_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            m_BodyOfTheNote = results.get(0);
            Intent noteIntent = new Intent(HomeActivity.this , CallIntent.class);

            temp[0]=m_TitleOfTheNote;
            temp[1]=m_BodyOfTheNote;
            noteIntent.putExtra("call details",temp);
            startActivity(noteIntent);
        }

        else if(requestCode == ALARM_REQUEST && resultCode == RESULT_OK)
        {
            try {
                m_HoursOfTheAlarm = Integer.parseInt(results.get(0), ALARM_REQUEST);
                GuiLangage(getResources().getString(R.string.Minutes_Request), 2500, ALARM_MINUTES);
            }
            catch (Exception e )
            {
                GuiLangage(getResources().getString(R.string.Number_Exception), 2500, ALARM_REQUEST);
            }
        }

        else if(requestCode == Location_REQUEST && resultCode == RESULT_OK)
        {
            Location = results.get(0);
            GuiLangage(getResources().getString(R.string.Start_time_request), 2500, HOUR_BEGIN);
        }

        else if(requestCode == MESSAGE_OF_THE_CONTACT&& resultCode == RESULT_OK)
        {
            m_MessageOfTheContact = results.get(0);
            Intent intentMessage = new Intent(HomeActivity.this, MessageContactIntent.class);

            temp[0]= m_NameOfTheContact;
            temp[1] = m_MessageOfTheContact;
            intentMessage.putExtra("contact details",temp);
            startActivity(intentMessage);
        }

        else if(requestCode == ALARM_MINUTES&& resultCode == RESULT_OK)
        {
            try {
                m_MinutesOfTheAlarm = Integer.parseInt(results.get(0));
                actions.addAlarm("Alarm By Max", m_HoursOfTheAlarm, m_MinutesOfTheAlarm);
            }
            catch (Exception e )
            {
                GuiLangage(getResources().getString(R.string.Number_Exception), 2500, ALARM_MINUTES);
            }

        }

        else if(requestCode == HOUR_BEGIN && resultCode == RESULT_OK)
        {

            try {
                hour_begin = Integer.parseInt(results.get(0));
                GuiLangage(getResources().getString(R.string.End_Time_request), 2500, HOUR_END);
            }
             catch (Exception e )
            {
                GuiLangage(getResources().getString(R.string.Number_Exception), 2500, HOUR_BEGIN);
            }
        }

        else if(requestCode == HOUR_END && resultCode == RESULT_OK)
        {
            try {
                hour_end = Integer.parseInt(results.get(0));
                actions.addEvent(Title, Location, hour_begin, hour_end);
            }
              catch (Exception e )
            {
                GuiLangage(getResources().getString(R.string.Number_Exception), 2500, HOUR_END);
            }
        }

        else if(requestCode == Build_REQUEST && resultCode == RESULT_OK)
        {
            Title= results.get(0);
            GuiLangage(getResources().getString(R.string.Location_Request), 2500, Location_REQUEST);
        }
        else if(requestCode == ARTIST_REQUEST&& resultCode == RESULT_OK)
        {
            m_NameOfTheArtist =results.get(0);
            GuiLangage(getResources().getString(R.string.Song_Request), 2500, SONG_REQUEST);
        }
        else if(requestCode == SONG_REQUEST&& resultCode == RESULT_OK)
        {
            m_Song= results.get(0);
            actions.PlaySong(m_Song,m_NameOfTheArtist);
        }
         }
        }

    public void TextToSpeechCommand (String i_MaxSpeach, int i_timeForSleep,int i_RequestCode)
    {
        if(Build.VERSION.SDK_INT>=21) {
            textToSpeech.speak(i_MaxSpeach, TextToSpeech.QUEUE_FLUSH, null, null);
            try {
                Thread.sleep(i_timeForSleep);
            }
            catch(InterruptedException ex)
            {

            }
            Intent intentRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
            intentRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.Max_Command));
            startActivityForResult(intentRecognizeSpeech, i_RequestCode);
        }
    }

    public void CheckSpeechRecognition(ArrayList<String> results )
    {
        for(String result: results)
        {
            if (result.contains("camera") ||result.contains("מצלמה")||result.contains("appareil")||result.contains("photo") )
            {
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST);
                break;
            }

            else if (result.contains("event")|| result.contains("événement")|| result.contains("אירוע")||result.contains("לוח שנה"))
            {

                GuiLangage(getResources().getString(R.string.Reminder_explain), 8000, Build_REQUEST);
                break;
            }

            else if (result.contains("alarm")||result.contains("שעון")||result.contains("מעורר")||result.contains("Réveil"))
            {
                GuiLangage(getResources().getString(R.string.Alarm_Request),6500,ALARM_REQUEST);
                break;
            }

            else if (result.contains("add")|| result.contains("contact")|| result.contains("ajoute")|| (result.contains("תוסיף")&&result.contains("איש קשר")))
            {
                GuiLangage(getResources().getString(R.string.Contact_Request),3000,NAME_OF_NEW_CONTACT_REQUEST);
                break;
            }

            else if (result.contains("browser")||result.contains("navigateur")||result.contains("internet")||result.contains("דפדפן"))
            {
                GuiLangage(getResources().getString(R.string.Website_Request),3000,WEBSITE_ADRESS_REQUEST);
                break;
            }

            else if(result.contains("message")||result.contains("הודעה")||result.contains("sms")||result.contains("אס אם אס"))
            {
                isAcall=false;
                GuiLangage(getResources().getString(R.string.Contact_Request),2500,CONTACTS_REQUEST);
                break;
            }

            else if(result.contains("maps")||result.contains("carte")||result.contains("מפה"))
            {
                GuiLangage(getResources().getString(R.string.City_request),3000,CITY_REQUEST);

                break;
            }

            else if(result.contains("call")||result.contains("appel")||result.contains("appelle")||result.contains("תקשר"))
            {
                isAcall=true;
                GuiLangage(getResources().getString(R.string.Contact_Request),2500,CONTACTS_REQUEST);


                break;
            }

            else if (result.contains("music")|| result.contains("musique")|| result.contains("מוזיקה")||result.contains("play")||result.contains("joue")||result.contains("נגן"))
            {

                GuiLangage(getResources().getString(R.string.Artist_Request), 3000, ARTIST_REQUEST);
                break;
            }
            else if(result.contains("search")||result.contains("cherche")||result.contains("חפש"))
            {
                String s = results.get(0);
                String t=  s.replace("search","");
                 t=  t.replace("cherche","");
                 t=  t.replace("חפש","");

                actions.addSearchWeb(t);
            }

            else
            {
                GuiLangage(getResources().getString(R.string.Error_commans),5500,SPEECH_REQUEST);
                break;

            }
        }
    }

    public void GetLongitudeLatitudeFromACity(ArrayList<String> results)
    {
        String input = results.get(0);
        String longitude = null;
        String latitude = null;

        List<android.location.Address> allAddresses= null;
        if(Geocoder.isPresent()){
            try {
                String location = input;
                Geocoder gc = new Geocoder(this);
                List<android.location.Address> addresses= gc.getFromLocationName(location, 5);
                allAddresses =addresses;

            } catch (IOException e) {
                // handle the exception
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+allAddresses.get(0).getLatitude()+","+allAddresses.get(0).getLongitude()));
        startActivity(intent);
    }

  public void GuiLangage ( String i_SpeechOfMax ,int i_TimeOfSleep, int i_Request)
 {
     if(Locale.getDefault().getLanguage()=="iw")
     {
         sttHebrew.setText(i_SpeechOfMax);
         TextToSpeechCommand("", 500, i_Request);
     }
     else
     {
         TextToSpeechCommand(i_SpeechOfMax, i_TimeOfSleep, i_Request);
     }

 }
    }



