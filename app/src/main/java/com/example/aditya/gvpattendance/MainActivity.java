package com.example.aditya.gvpattendance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener{
    EditText UserText , PassText;
    Button LogIn;
    ProgressDialog progressDialog;

    private void userLogin(){
        final String username = UserText.getText().toString().trim();
        final String password = PassText.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.LogURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                jsonObject.getString("username"),
                                                jsonObject.getString("fid")
                                        );
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                                intent.putExtra("username" ,jsonObject.getString("username") );
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > params = new HashMap<>();
                params.put("user_name", username);
                params.put("user_pass" , password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            //SharedPrefManager.getInstance(this).islogout();
            finish();
            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
            return;
        }

        UserText = (EditText) findViewById(R.id.UserText);
        PassText = (EditText) findViewById(R.id.PassText);
        LogIn = (Button) findViewById(R.id.LogIn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        LogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == LogIn)
            userLogin();
    }
}
