package com.example.know_your_govt;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutAppActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void clickTheSourceText(View v) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.google.com/civic-information/docs/using_api"));
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
