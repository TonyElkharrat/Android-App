package com.example.max;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;

public class Actions
{
    Context m_context ;
    public Actions(Context i_context)
    {
        m_context = i_context;
    }

    public void addAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(m_context.getPackageManager()) != null) {
            m_context.startActivity(intent);
        }
    }

    public void addSearchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        if (intent.resolveActivity(m_context.getPackageManager()) != null) {
            m_context.startActivity(intent);
        }
    }
//
    public void addEvent(String title, String location, long begin, long end)
    {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(m_context.getPackageManager()) != null)
        {
            m_context.startActivity(intent);
        }

    }
    public void PlaySong(String i_song, String i_Artist) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
        intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, i_song);

        intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, i_Artist);
        intent.putExtra(SearchManager.QUERY, i_Artist + " " + i_song);
        if (intent.resolveActivity(m_context.getPackageManager()) != null) {
            m_context.startActivity(intent);
        }
    }

}
