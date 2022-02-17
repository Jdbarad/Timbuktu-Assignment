package com.builditcreative.timbuktuassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private Button show;
    private EditText nameEdit;
    private ImageView gender;

    private HashMap<String, Object> data = new HashMap<>();
    private String country = null;
    private String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CountryCodePicker cpp = findViewById(R.id.ccp);

        show = findViewById(R.id.show_button);
        nameEdit = findViewById(R.id.name_edit);
        gender = findViewById(R.id.gender);

        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country = cpp.getSelectedCountryNameCode();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                country = cpp.getSelectedCountryNameCode().toUpperCase();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url ="https://api.agify.io/?name="+ name +"&country_id="+country;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                data = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>(){}.getType());
                                TextView nameText = findViewById(R.id.name);
                                TextView ageText = findViewById(R.id.age);
                                try {
                                    nameText.setText(data.get("name").toString());
                                    ageText.setText(data.get("age").toString());
                                    switch (data.get("gender").toString()){
                                        case "male": gender.setImageResource(R.drawable.ic_outline_male_24);
                                            break;
                                        case "female": gender.setImageResource(R.drawable.ic_baseline_female_24);
                                            break;
                                        default: gender.setVisibility(View.GONE);
                                    }
                                }catch (Exception e){

                                }                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check You Internet", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);

                url ="https://api.genderize.io/?name="+ name +"&country_id="+country;
                stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                data = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>(){}.getType());
                                TextView nameText = findViewById(R.id.name);
                                TextView ageText = findViewById(R.id.age);
                                try {
                                    nameText.setText(data.get("name").toString());
                                    switch (data.get("gender").toString()){
                                        case "male": gender.setImageResource(R.drawable.ic_outline_male_24);
                                            break;
                                        case "female": gender.setImageResource(R.drawable.ic_baseline_female_24);
                                            break;
                                        default: gender.setVisibility(View.GONE);
                                    }
                                }catch (Exception e){

                                }                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(stringRequest);
            }
        });
    }
}