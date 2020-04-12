package com.example.know_your_govt;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ImageOfAPersonActivity extends AppCompatActivity {
    private String Republican = "Republican";
    private String RepublicanParty = "Republican Party";
    private String Democratic = "Democratic";
    private String DemocraticParty = "Democratic Party";
    private TextView locationTextView;
    private TextView officeTextView;
    private TextView nameTextView;
    private ImageView photoView;
    private ImageView partyLogo;
    private Intent intent;
    private OfficialPersonDetails officialPersonDetails;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.home:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        locationTextView = findViewById(R.id.pa_location);
        officeTextView = findViewById(R.id.pa_office);
        nameTextView = findViewById(R.id.pa_name);
        photoView = findViewById(R.id.pa_photo);
        partyLogo = findViewById(R.id.pa_party_logo);
        intent = getIntent();
        officialPersonDetails = (OfficialPersonDetails) intent.getSerializableExtra("official");
        if (networkCheck()) {
            photoView.setImageResource(R.drawable.placeholder);
            if (officialPersonDetails.getPhotoLink().equals("No Data Provided")) {
                photoView.setImageResource(R.drawable.missingimage);
            } else {
                final String activityOfficialPhotoUrl = officialPersonDetails.getPhotoLink();
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        final String changedUrl = activityOfficialPhotoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(photoView);

                    }
                }).build();

                picasso.load(activityOfficialPhotoUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(photoView);
            }
        } else {
            photoView.setImageResource(R.drawable.brokenimage);
            Toast.makeText(this, "No Active Internet Connection. Image could not be Loaded", Toast.LENGTH_LONG).show();
        }
        Log.d("after go to pa", locationTextView.getText().toString());
        CharSequence ch = intent.getCharSequenceExtra("location");
        if (ch == null) {
            Log.d("ImageOfAPersonActivity", "Location Not Available");
        }else {
            Log.d("ImageOfAPersonActivity", ch.toString());
        }
        locationTextView.setText(intent.getCharSequenceExtra("location"));
        officeTextView.setText(officialPersonDetails.getOfficeName());
        officeTextView.setPaintFlags(officeTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        nameTextView.setText(officialPersonDetails.getPersonName());
        if (officialPersonDetails.getPartyName() != null) {
            if (officialPersonDetails.getPartyName().equals(RepublicanParty) || officialPersonDetails.getPartyName().equals(Republican)) {
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                partyLogo.setImageResource(R.drawable.republican);
            }else if (officialPersonDetails.getPartyName().equals(DemocraticParty) || officialPersonDetails.getPartyName().equals(Democratic)) {
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                partyLogo.setImageResource(R.drawable.democratic);
            }else {
                getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                partyLogo.setVisibility(View.INVISIBLE);
            }
        } else {
            getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        }
    }
    public void partyLogoClick(View v) {
        String partyName = officialPersonDetails.getPartyName();
        if(partyName.equals(DemocraticParty) || partyName.equals(Democratic)){
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org/"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }else if(partyName.equals(RepublicanParty) || partyName.equals(Republican)){
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gop.com/"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nonpartisanreformers.org/"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean networkCheck() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            Log.d("IsDeviceOnline", "isDeviceOnline: " + (reachable = (returnVal == 0)));
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
