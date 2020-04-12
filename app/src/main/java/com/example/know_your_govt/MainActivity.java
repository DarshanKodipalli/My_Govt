package com.example.know_your_govt;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<OfficialPersonDetails> officialList = new ArrayList<>();
    private OfficalPersonAdapter officialAdapter;
    private TextView locationTextView;
    private LocationDetector locationDetector;
    private MainActivity mainActivity;
    private TextView warning;
    private TextView warning_heading;
    private TextView warning_content;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        recyclerView = findViewById(R.id.myrecycler);
        officialAdapter = new OfficalPersonAdapter(officialList, this);
        locationTextView = findViewById(R.id.locationTextView);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        warning = findViewById(R.id.ma_warning);
        warning_heading = findViewById(R.id.warning_title);
        warning_content = findViewById(R.id.warning_content);
        spinner = findViewById(R.id.progress_loader);
        spinner.setVisibility(View.VISIBLE);
        if (networkCheck()){
            warning_heading.setVisibility(View.INVISIBLE);
            warning_content.setVisibility(View.INVISIBLE);
            locationDetector = new LocationDetector(this);
            locationDetector.shutdown();
        } else {
            displayNoNetworkErrorMessage("No Network Connection","Data cannot be accessed/loaded without an active Internet Connection");
        }
        if (locationTextView.getText().toString().equals("No Data For Location")) {
//            warningShow("Current Location Fetched","Please re-open the app to apply the detected location");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menuInfo:
                intent = new Intent(this, AboutAppActivity.class);
                startActivity(intent);
                return true;
            case R.id.home:
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuSearch:
                manualLocationSearch();

            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Intent intent = new Intent(this, OfficialPersonActivity.class);
        intent.putExtra("location", locationTextView.getText().toString());
        Log.d("MainActivity", "onClick: "+locationTextView.getText().toString());
        intent.putExtra("official", officialList.get(pos));
        startActivityForResult(intent, 1);
    }

    public void manualLocationSearch(){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter a City, State or Zip code");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText inputTextView = (EditText) view.findViewById(R.id.inputTextView);
                String input = inputTextView.getText().toString();
                AsyncGovtDetailsLoader asyncDataLoader = new AsyncGovtDetailsLoader(mainActivity);
                asyncDataLoader.execute(input);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setLocation(double lat, double lon){
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat,lon, 1);
            Address ad = addresses.get(0);
            String postalCode = ad.getPostalCode();
            spinner.setVisibility(View.INVISIBLE);
            new AsyncGovtDetailsLoader(mainActivity).execute(postalCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void addOfficial(Object[] officialPersonDetails){
        locationTextView.setText(officialPersonDetails[1].toString());
        ArrayList<OfficialPersonDetails> offList = (ArrayList<OfficialPersonDetails>) officialPersonDetails[0];
        clearOfficial();
        for(int i = 0; i < offList.size(); i++){
            officialList.add(offList.get(i));
        }
        officialAdapter.notifyDataSetChanged();
    }

    public void clearOfficial(){
        officialList.clear();
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

    public void displayNoNetworkErrorMessage(String heading,String content){
        warning_heading.setText(heading);
        warning_content.setText(content);
        warning_heading.setVisibility(View.VISIBLE);
        warning_content.setVisibility(View.VISIBLE);
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }
}
