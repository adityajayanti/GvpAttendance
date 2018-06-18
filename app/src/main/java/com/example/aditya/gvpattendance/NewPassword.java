package com.example.aditya.gvpattendance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class NewPassword extends AppCompatActivity implements View.OnClickListener {
    Button button;
    TextView textView;
    EditText e1 , e2;
    String username , password;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        getSupportActionBar().setTitle("New Password");

        Intent intent = getIntent();


        button = (Button)findViewById(R.id.updatepass);
        textView =(TextView)findViewById(R.id.userText);
        e1 = (EditText)findViewById(R.id.editNewPass);
        e2 = (EditText)findViewById(R.id.editcnfPass);

        progressDialog = new ProgressDialog(this);
        textView.setText(intent.getStringExtra("user"));
        button.setOnClickListener(this);

    }

    public void updatePassword(){
        username = textView.getText().toString();
        password = e1.getText().toString();


        progressDialog.setMessage("Updating Password...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constansts.upPassURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")){
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                                startActivity(intent);

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
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("user_name",username);
                params.put("password",password);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.features_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.Logout:
                SharedPrefManager.getInstance(this).islogout();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.backto:
                startActivity(new Intent(getApplicationContext(),AttendanceMenu.class));
                finish();

            case R.id.hometo:
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                finish();

        }
        return  true;

    }
    @Override
    public void onClick(View v) {
        if(v == button){
            if(e1.getText().toString().equals(e2.getText().toString())) {
                updatePassword();
            }
            else{
                Toast.makeText(getApplicationContext(),"Password Mis Match",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
