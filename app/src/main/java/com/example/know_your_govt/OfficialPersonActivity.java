package com.example.know_your_govt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.w3c.dom.Text;


public class OfficialPersonActivity extends AppCompatActivity{

    private TextView locationTextView;
    private TextView officeTextView;
    private TextView nameTextView;
    private TextView partyTextView;
    private TextView addressTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView webTextView;

    private ImageButton photoImageView;
    private ImageButton facebookImageView;
    private ImageButton twitterImageView;
    private ImageButton googlePllusImageView;
    private ImageButton youtubeImageView;
    private ImageButton partyLogo;

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
        setContentView(R.layout.activity_official);
        locationTextView = findViewById(R.id.oa_location);
        officeTextView = findViewById(R.id.oa_office);
        nameTextView = findViewById(R.id.oa_name);
        partyTextView =findViewById(R.id.oa_party);
        addressTextView = findViewById(R.id.oa_address);
        phoneTextView = findViewById(R.id.oa_phone);
        emailTextView = findViewById(R.id.oa_email);
        webTextView = findViewById(R.id.oa_website);
        photoImageView = findViewById(R.id.oa_photo);
        partyLogo = findViewById(R.id.oa_party_logo);
        facebookImageView = findViewById(R.id.facebook);
        twitterImageView = findViewById(R.id.twitter);
        googlePllusImageView = findViewById(R.id.googlePlus);
        youtubeImageView = findViewById(R.id.youtube);

        intent = getIntent();
        locationTextView.setText(intent.getCharSequenceExtra("location"));
        officialPersonDetails = (OfficialPersonDetails) intent.getSerializableExtra("official");
        officeTextView.setText(officialPersonDetails.getOfficeName());
        officeTextView.setPaintFlags(officeTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        nameTextView.setText(officialPersonDetails.getPersonName());
        if (officialPersonDetails.getPartyName() != null) {

            if (officialPersonDetails.getPartyName().equals("Republican Party") || officialPersonDetails.getPartyName().equals("Republican")){
                getWindow().getDecorView().setBackgroundColor(Color.RED);
                partyTextView.setText('('+officialPersonDetails.getPartyName()+')');
                partyLogo.setImageResource(R.drawable.republican);
            }
            else if (officialPersonDetails.getPartyName().equals("Democratic Party") || officialPersonDetails.getPartyName().equals("Democratic")) {
                partyTextView.setText('('+officialPersonDetails.getPartyName()+')');
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                partyLogo.setImageResource(R.drawable.democratic);
            }
            else {
                partyTextView.setText('('+officialPersonDetails.getPartyName()+')');
                partyLogo.setVisibility(View.INVISIBLE);
                getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            }
        }
        if (officialPersonDetails.getAddress() != null) {
            addressTextView.setText(officialPersonDetails.getAddress());
        }
        if (officialPersonDetails.getPhone() != null) {
            phoneTextView.setText(officialPersonDetails.getPhone());
        }
        Log.d("DisplayEmail", "onCreate: "+officialPersonDetails.getEmailId());
        if (officialPersonDetails.getEmailId().equals("No Data Provided")) {
            emailTextView.setText("");
            googlePllusImageView.setVisibility(View.INVISIBLE);

        }else {
            emailTextView.setText(officialPersonDetails.getEmailId());
            googlePllusImageView.setVisibility(View.VISIBLE);
        }
        if (officialPersonDetails.getUrl() != null) {
            webTextView.setText(officialPersonDetails.getUrl());
        }

        if(networkCheck()) {
            photoImageView.setImageResource(R.drawable.placeholder);
            if (officialPersonDetails.getPhotoLink().equals("No Data Provided")) {
                photoImageView.setImageResource(R.drawable.missingimage);
            } else {
                final String activityOfficialPhotoUrl = officialPersonDetails.getPhotoLink();
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        final String changedUrl = activityOfficialPhotoUrl.replace("http:", "https:");
                        picasso.load(changedUrl)
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder)
                                .into(photoImageView);

                    }
                }).build();

                picasso.load(activityOfficialPhotoUrl)
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(photoImageView);
            }
        } else {
            photoImageView.setImageResource(R.drawable.brokenimage);
            Toast.makeText(this, "No Active Internet Connection. Image could not be Loaded", Toast.LENGTH_LONG).show();
        }

        if (officialPersonDetails.getSocialLinks() == null) {
            facebookImageView.setVisibility(View.INVISIBLE);
            youtubeImageView.setVisibility(View.INVISIBLE);
            twitterImageView.setVisibility(View.INVISIBLE);
            googlePllusImageView.setVisibility(View.INVISIBLE);
        }else{
            if (officialPersonDetails.getSocialLinks().getFacebookIdentifier().equals("")||officialPersonDetails.getSocialLinks().getFacebookIdentifier().equals("No Data Provided")){
                facebookImageView.setVisibility(View.INVISIBLE);
            }
            if (officialPersonDetails.getSocialLinks().getYoutubeIdentifier().equals("")||officialPersonDetails.getSocialLinks().getYoutubeIdentifier().equals("No Data Provided")){
                youtubeImageView.setVisibility(View.INVISIBLE);
            }
            if (officialPersonDetails.getSocialLinks().getTwitterIdentifier().equals("")||officialPersonDetails.getSocialLinks().getTwitterIdentifier().equals("No Data Provided")){
                twitterImageView.setVisibility(View.INVISIBLE);
            }
            if (officialPersonDetails.getSocialLinks().getGooglePlusIdentifier().equals("") || officialPersonDetails.getSocialLinks().getGooglePlusIdentifier().equals("No Data Provided")){
                googlePllusImageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void photoClick(View v){
        if (officialPersonDetails.getPhotoLink() == null){
            return;
        }
        Intent intent = new Intent(this, ImageOfAPersonActivity.class);
        intent.putExtra("official", officialPersonDetails);
        intent.putExtra("location", locationTextView.getText());
        Log.d("before go to pa", locationTextView.getText().toString());
        startActivityForResult(intent, 1);
    }

    public void partyLogoClick(View v) {
        String partyName = officialPersonDetails.getPartyName();
        if(partyName.equals("Democratic Party") || partyName.equals("Democratic")){
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org/"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }else if(partyName.equals("Republican Party") || partyName.equals("Republican")){
            getWindow().getDecorView().setBackgroundColor(Color.RED);
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gop.com/"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nonpartisanreformers.org/"));
                startActivity(browserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    public void youtubeClick(View v) {
        String youtubeIdentifier= officialPersonDetails.getSocialLinks().getYoutubeIdentifier();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youtubeIdentifier));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youtubeIdentifier)));
        }
    }

    public void googlePlusClick(View v) {
        String googleplusIdentifier = officialPersonDetails.getSocialLinks().getGooglePlusIdentifier();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", googleplusIdentifier);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + googleplusIdentifier)));
        }
    }


    public void facebookClick(View v){
        String FACEBOOK_URL = "https://www.facebook.com/" + officialPersonDetails.getSocialLinks().getFacebookIdentifier();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }else {
                urlToUse = "fb://page/" + officialPersonDetails.getSocialLinks().getFacebookIdentifier();
            }
        }catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClick(View v){
        Intent intent = null ;
        String name = officialPersonDetails.getSocialLinks().getTwitterIdentifier();
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public Boolean networkCheck(){
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
