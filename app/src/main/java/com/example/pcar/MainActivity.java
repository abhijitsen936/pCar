package com.example.pcar;

import static android.content.ContentValues.TAG;
import static com.example.pcar.imgSql.COLUMN_ID;
import static com.example.pcar.imgSql.COLUMN_NAME;
import static com.example.pcar.imgSql.DATA;
import static com.example.pcar.sqlite.COLUMN_CARMAKE;
import static com.example.pcar.sqlite.COLUMN_CARMODEL;
import static com.example.pcar.sqlite.TABLE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;



public class MainActivity extends AppCompatActivity {

    private Spinner carMakeSpinner;
    private Spinner carModelSpinner;
    Button saveButton, RefreshButton, logout;

    ListView lv;
    String[] carMake;
    String[] carModel;
    int[] id;
    byte[] b;
    ArrayList<String[]> arrayListOfArrays = new ArrayList<>();
    ArrayList<byte[]> imgArray = new ArrayList<>();
    sqlite data = new sqlite(MainActivity.this);




    private static final int PICK_IMAGE_REQUEST = 1;


    Bitmap bitmap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = data.getWritableDatabase();


        ContentValues values = new ContentValues();
        lv = findViewById(R.id.lv);
//        dbHelper = new imgSql(MainActivity.this);
//        db2 = dbHelper.getWritableDatabase();





        carMakeSpinner = findViewById(R.id.car_make_spinner);
        carModelSpinner = findViewById(R.id.car_model_spinner);

        saveButton = findViewById(R.id.button2);
        RefreshButton=findViewById(R.id.button);
        logout=findViewById(R.id.button3);

        // Call API #1 to get the list of vehicle makes



        // Create an empty ArrayList to hold the Person objects
        ArrayList<String> makeList = new ArrayList<>();
        // Create an ArrayList to hold the model names
        ArrayList<String> modelList = new ArrayList<>();

// Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

// Set the URL of the API endpoint that returns the JSON data
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the array of Person objects from the JSON response
                            JSONArray jsonArray = response.getJSONArray("Results");

                            // Loop through the array and create a new Person object for each item
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject personJson = jsonArray.getJSONObject(i);
                                String name = personJson.getString("Make_Name");
                                String id = personJson.getString("Make_ID");

                                // Combine the name and id with a semicolon separator
                                String personString = id + "=" + name;

                                // Add the combined string to the ArrayList
                                makeList.add(personString);


                            }




                            ArrayAdapter<String> carMakeAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, makeList);
                            carMakeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            carMakeSpinner.setAdapter(carMakeAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

                );

// Add the request to the RequestQueue
        queue.add(jsonObjectRequest);


        // Set an OnItemSelectedListener for the car make Spinner
        carMakeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMake = parent.getItemAtPosition(position).toString();
//                carMakeSpinner.setContentDescription(selectedMake);

                // Split the string into an array of strings using the semicolon separator
                String[] parts = selectedMake.split("=");

                // The first part is the id, the second part is the name
                String makeId = parts[0];
//                Toast.makeText(MainActivity.this, "make", Toast.LENGTH_SHORT).show();

                String makeName = parts[1];

                // Call API #2 to get the list of vehicle models for the selected make
                String url = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMakeId/" + makeId + "?format=json";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Get the array of model objects from the JSON response
                                    JSONArray jsonArray = response.getJSONArray("Results");


                                    // Loop through the array and add the model names to the ArrayList
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject modelJson = jsonArray.getJSONObject(i);
                                        String modelName = modelJson.getString("Model_Name");
                                        String modelId = modelJson.getString("Model_ID");
                                        modelList.clear();
                                        modelList.add(modelId + "=" + modelName);
                                    }

                                    //  ArrayAdapter using the modelList ArrayList and set it as the adapter for the carModelSpinner
                                    ArrayAdapter<String> carModelAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, modelList);
                                    carModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    carModelSpinner.setAdapter(carModelAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                // Add the request to the RequestQueue
                Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);


                carModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedModel = parent.getItemAtPosition(position).toString();
//                        carMakeSpinner.setContentDescription(selectedModel);
//
                        // Split the string into an array of strings using the semicolon separator
                        String[] parts2 = selectedModel.split("=");

                        // The first part is the id, the second part is the name
                        String modelId = parts2[0];
                        String modelName = parts2[1];

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                values.put(COLUMN_CARMAKE, makeName);
                                values.put(COLUMN_CARMODEL, selectedModel);

                                long newRowId = db.insert(TABLE_NAME, null, values);

                                if (newRowId == -1) {
                                    Toast.makeText(MainActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(MainActivity.this, "Car Make = " + makeName + "    Car Model =" + modelName + "Car ID = " + modelId, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                    makeList();


                                }

                            }

                        });

                        RefreshButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeList();
                                Toast.makeText(MainActivity.this, "List Refreshed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, login.class);
                                startActivity(intent);
                                finish();
                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        carModelSpinner.setContentDescription("Select a Model");
                    }
                });


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                carMakeSpinner.setContentDescription("Select a Make");
            }
        });

        makeList();



    }
//    public Bitmap saveImg(int id) {
//        this.id2 = id;
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, 1);
//        return bitmap;
//    }

   public void makeList(){


       Cursor t = data.getinfo();
       if (t.getCount() == 0) {
           Toast.makeText(MainActivity.this, "no data found", Toast.LENGTH_SHORT).show();
       }
       while (t.moveToNext()) {
           String[] dataa = new String[3];
           dataa[0] = t.getString(0);
           dataa[1] = t.getString(1);
           dataa[2] = t.getString(2);
           arrayListOfArrays.add(dataa);
       }

       Cursor r = data.getinfo();
       if (r.getCount() == 0) {
           Toast.makeText(MainActivity.this, "no data found", Toast.LENGTH_SHORT).show();
       }
       while (r.moveToNext()){
           byte[] dataaa;

           dataaa= r.getBlob(1);

           imgArray.add(dataaa);
       }




       carMake = new String[arrayListOfArrays.size()];
       carModel = new String[arrayListOfArrays.size()];
       id = new int[arrayListOfArrays.size()];
       b= new byte[imgArray.size()];
       for (int q = 0; q < arrayListOfArrays.size(); q++) {
           carMake[q] = arrayListOfArrays.get(q)[1];
       }
       for (int p = 0; p < arrayListOfArrays.size(); p++) {
           carModel[p] = arrayListOfArrays.get(p)[2];
       }
       for (int k = 0; k < arrayListOfArrays.size(); k++) {
           id[k] = Integer.parseInt(arrayListOfArrays.get(k)[0]);
       }
       for (int s = 0; s < arrayListOfArrays.size(); s++) {
           b[s] = imgArray.get(s)[0];
       }

       programAdapter Adapter = new programAdapter(this, carMake, carModel, id, b);

       lv.setAdapter(Adapter);

        arrayListOfArrays.clear();
       Adapter.notifyDataSetChanged();
   }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                byte[] bytes = getBytes(inputStream);

                imgSql data2= new imgSql(getApplicationContext());
                SQLiteDatabase db2=data2.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME, "abhi");
                values.put(DATA, bytes);

                long newRowId = db2.insert(TABLE_NAME, null, values);
//                Toast.makeText(this, "Image saved with ID: " + newRowId, Toast.LENGTH_LONG).show();

                if (newRowId != -1) {
                    Toast.makeText(this, "Image saved with ID: " + newRowId, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error inserting image into database", Toast.LENGTH_LONG).show();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}




