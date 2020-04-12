package com.example.know_your_govt;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class AsyncGovtDetailsLoader extends AsyncTask<String, Void, String>{
    private MainActivity mainActivity;
    private String requestURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBUtFofp8fj2WkjDcUe5VMIB5sqTgwpjmo&address=";
    private String locationpostalCode;
    private String locationcity;
    private String locationstate;

    public AsyncGovtDetailsLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected String doInBackground(String... params) {
        String location;
        if(params[0]!=""){
            location = params[0];
        }else {
            location = "Chicago";
        }
        StringBuilder fetchedData = new StringBuilder();
        Uri dataUri = Uri.parse(requestURL);
        String urlToString= dataUri.toString();
        try {
            URL url = new URL(urlToString+location);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream urlConnectionInputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(urlConnectionInputStream)));
            String nextline;
            while ((nextline= reader.readLine()) != null) {
                fetchedData.append(nextline).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fetchedData.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (result == null) {
                Toast.makeText(mainActivity, "Data not available at the Calling Service", Toast.LENGTH_SHORT).show();
                mainActivity.addOfficial(null);
                return;
            }
            if (result.isEmpty()) {
                Toast.makeText(mainActivity, "Data not available for the input location", Toast.LENGTH_SHORT).show();
                mainActivity.addOfficial(null);
                return;
            }
            ArrayList<OfficialPersonDetails> officialList = dataInJSONFormat(result);
            Object[] dataToReturn = new Object[2];
            dataToReturn[0] = officialList;
            if (locationcity.equals("")) {
                dataToReturn[1] = locationstate+" "+locationpostalCode;
            } else {
                dataToReturn[1] = locationcity+", "+locationstate+" " +locationpostalCode;
            }
            mainActivity.addOfficial(dataToReturn);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    private ArrayList<OfficialPersonDetails> dataInJSONFormat(String s){
        ArrayList<OfficialPersonDetails> officialPersonsList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(s);
            JSONObject normalizedJSONInput = jsonObject.getJSONObject("normalizedInput");
            JSONArray officedetails = jsonObject.getJSONArray("offices");
            JSONArray officialsdetails = jsonObject.getJSONArray("officials");
            locationcity = normalizedJSONInput.getString("city");
            locationstate = normalizedJSONInput.getString("state");
            locationpostalCode = normalizedJSONInput.getString("zip");
            for(int i = 0;i < officedetails.length(); i++){
                JSONObject JSONobj = officedetails.getJSONObject(i);
                String Office_name = JSONobj.getString("name");
                String officialIndices = JSONobj.getString("officialIndices");
                String dummy1 = officialIndices.substring(1,officialIndices.length()-1);
                String [] dummy2 = dummy1.split(",");
                int [] officialIndicesInts = new int [dummy2.length];
                for(int j = 0; j < dummy2.length; j++){
                    officialIndicesInts[j] = Integer.parseInt(dummy2[j]);
                }
                for(int j = 0; j < officialIndicesInts.length; j++ ){
                    JSONObject subJSONObjects = officialsdetails.getJSONObject(officialIndicesInts[j]);
                    String name = subJSONObjects.getString("name");
                    String officialaddress = "";
                    if(! subJSONObjects.has("address")){
                        officialaddress  = "No Data Provided";
                    }
                    else {
                        JSONArray address_Array = subJSONObjects.getJSONArray("address");
                        JSONObject adrs_Obj = address_Array.getJSONObject(0);
                        if (adrs_Obj.has("line1")) {
                            officialaddress += adrs_Obj.getString("line1") + "\n";
                        }
                        if (adrs_Obj.has("line2")) {
                            officialaddress += adrs_Obj.getString("line2") + "\n";
                        }
                        if (adrs_Obj.has("city")) {
                            officialaddress += adrs_Obj.getString("city") + " ";
                        }
                        if (adrs_Obj.has("state")) {
                            officialaddress += adrs_Obj.getString("state") + ", ";
                        }
                        if (adrs_Obj.has("zip")) {
                            officialaddress += adrs_Obj.getString("zip");
                        }
                    }
                    String partyName = (subJSONObjects.has("party") ? subJSONObjects.getString("party") : "Unknown" );
                    String phoneNumber = ( subJSONObjects.has("phones") ? subJSONObjects.getJSONArray("phones").getString(0) : "No Data Provided");
                    String urls_found = ( subJSONObjects.has("urls") ? subJSONObjects.getJSONArray("urls").getString(0) : "No Data Provided");
                    String emailID = (subJSONObjects.has("emails") ? subJSONObjects.getJSONArray("emails").getString(0) : "No Data Provided");
                    String photoLinkURL = (subJSONObjects.has("photoUrl") ? subJSONObjects.getString("photoUrl") : "No Data Provided");
                    JSONArray nameLink = ( subJSONObjects.has("channels") ? subJSONObjects.getJSONArray("channels") : null );
                    String googleplusID = "";
                    String facebookID = "";
                    String twitterID = "";
                    String youtubeID = "";
                    if(nameLink != null){
                        for(int k = 0; k < nameLink.length(); k++ ){
                            String type = nameLink.getJSONObject(k).getString("type");
                            switch (type){
                                case "GooglePlus":
                                    googleplusID = nameLink.getJSONObject(k).getString("id");
                                    break;
                                case "Facebook":
                                    facebookID = nameLink.getJSONObject(k).getString("id");
                                    break;
                                case "Twitter":
                                    twitterID = nameLink.getJSONObject(k).getString("id");
                                    break;
                                case "YouTube":
                                    youtubeID = nameLink.getJSONObject(k).getString("id");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    else{
                        googleplusID = "No Data Provided";
                        facebookID = "No Data Provided";
                        twitterID = "No Data Provided";
                        youtubeID = "No Data Provided";
                    }
                    OfficialPersonDetails o = new OfficialPersonDetails(name, Office_name, partyName,officialaddress, phoneNumber, urls_found, emailID, photoLinkURL, googleplusID, facebookID, twitterID, youtubeID);
                    officialPersonsList.add(o);
                }
            }
            return officialPersonsList;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}