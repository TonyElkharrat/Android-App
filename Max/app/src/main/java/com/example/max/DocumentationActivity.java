package com.example.max;

import android.app.Activity;
import android.os.Bundle;

public class DocumentationActivity extends Activity
{
    final int READ_CONTACT_PERMISSION=12;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.documentation_activity);
    }
}
