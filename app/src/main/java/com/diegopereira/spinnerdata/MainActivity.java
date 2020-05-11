package com.diegopereira.spinnerdata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener{

    //Declaring an Spinner
    private Spinner spinner;

    //An ArrayList for Spinner Items
    private ArrayList<String> alimentos;

    //JSON Array
    private JSONArray result;
    private JSONObject result2; //test

    //TextViews to display details
    private TextView textViewQtde;
    private TextView textViewUnit;
    private TextView textViewProt;
    private TextView textViewCarb;
    private TextView textViewKcals;
    private TextView textViewFat;
    private TextView textViewFibra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the ArrayList
        alimentos = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        //Initializing TextViews
        textViewQtde = (TextView) findViewById(R.id.textViewQtde);
        textViewUnit = (TextView) findViewById(R.id.textViewUnit);
        textViewProt = (TextView) findViewById(R.id.textViewProt);
        textViewCarb = (TextView) findViewById(R.id.textViewCarb);
        textViewKcals = (TextView) findViewById(R.id.textViewKcals);
        textViewFat = (TextView) findViewById(R.id.textViewFat);
        textViewFibra = (TextView) findViewById(R.id.textViewFibra);

        //This method will fetch the data from the URL
        getData();
    }

    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getAlimentos to get the alimentos from the JSON Array
                            getAlimentos(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getAlimentos(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                alimentos.add(json.getString(Config.TAG_DESC));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, alimentos));
    }

    //Method to get student name of a particular position
    private String getBase_qty(int position){
        String base_qty="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            base_qty = json.getString(Config.TAG_BASE_QTY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return base_qty;
    }

    //Doing the same with this method as we did with getName()
    private String getUnit(int position){
        String unit="";
        try {
            JSONObject json = result.getJSONObject(position);
            unit = json.getString(Config.TAG_UNIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return unit;
    }

    //Doing the same with this method as we did with getName()
    private String getProt(int position){
       // double prot=0;
       // String p="";
        try {
           JSONObject attributes = result.getJSONObject(position).getJSONObject("attributes").getJSONObject("protein");

           return String.format("%.2f", attributes.getDouble(Config.TAG_PROTQTY));
           // p=String.format("%.2f", prot);

        } catch (JSONException e) {
            e.printStackTrace();
            return "0,00";
        }
        // return p;
    }

    //Doing the same with this method as we did with getName()
    private String getCarb(int position){
       // String carbohydrate="";
        try {
            JSONObject attributes = result.getJSONObject(position).getJSONObject("attributes").getJSONObject("carbohydrate");

            return String.format("%.2f", attributes.getDouble(Config.TAG_CARBQTY));

        } catch (JSONException e) {
            e.printStackTrace();
            return "0,00";

        }
       // return carbohydrate;
    }

    //Doing the same with this method as we did with getName()
    private String getKcals(int position){
        // String carbohydrate="";
        try {
            JSONObject attributes = result.getJSONObject(position).getJSONObject("attributes").getJSONObject("energy");

            return String.format("%.2f", attributes.getDouble(Config.TAG_KCALSQTY));

        } catch (JSONException e) {
            e.printStackTrace();
            return "0,00";

        }
        // return carbohydrate;
    }

    //Doing the same with this method as we did with getName()
    private String getFat(int position){
        // String carbohydrate="";
        try {
            JSONObject attributes = result.getJSONObject(position).getJSONObject("attributes").getJSONObject("lipid");

            return String.format("%.2f", attributes.getDouble(Config.TAG_FATQTY));

        } catch (JSONException e) {
            e.printStackTrace();
            return "0,00";

        }
        // return carbohydrate;
    }

    //Doing the same with this method as we did with getName()
    private String getFibra(int position){
        // String fiber="";
        try {
            JSONObject attributes = result.getJSONObject(position).getJSONObject("attributes").getJSONObject("fiber");

            return String.format("%.2f", attributes.getDouble(Config.TAG_FIBRAQTY));

        } catch (JSONException e) {
            e.printStackTrace();
            return "0,00";

        }
        // return fiber;
    }

    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item 
        textViewQtde.setText(getBase_qty(position));
        textViewUnit.setText(getUnit(position));
        textViewProt.setText(getProt(position));
        textViewCarb.setText(getCarb(position));
        textViewKcals.setText(getKcals(position));
        textViewFat.setText(getFat(position));
        textViewFibra.setText(getFibra(position));

    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewQtde.setText("");
        textViewUnit.setText("");
        textViewProt.setText("");
        textViewCarb.setText("");
        textViewKcals.setText("");
        textViewFat.setText("");
        textViewFibra.setText("");

    }
}