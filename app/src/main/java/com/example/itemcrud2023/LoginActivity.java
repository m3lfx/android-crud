package com.example.itemcrud2023;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private Button mLogin;
    private EditText mEmail;
    private EditText mPassword;
    private String mJSONURLString = "http://192.168.1.11:8000/api/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        mActivity = LoginActivity.this;
        mLogin = (Button) findViewById(R.id.btnLogin);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin.setOnClickListener(view -> {
            JSONObject jsonItem = new JSONObject();
            try {
                jsonItem.put("email", mEmail.getText());
                jsonItem.put("password", mPassword.getText());

            }catch (JSONException e) {
                e.printStackTrace();
            }
            // Initialize a new RequestQueue instance
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            // Initialize a new JsonObjectRequest instance
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    mJSONURLString,
                    jsonItem,
                    response -> {
                        try{
                            String accessToken = response.getString("access_token");
                            Toast.makeText(mContext, accessToken, Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(mContext,MainActivity.class);
                            Intent intent = new Intent(mContext,ListItemActivity.class);
                            intent.putExtra("access_token",accessToken);
                            startActivity(intent);
                            finish();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            // Do something when error occurred
                            Log.i("laravel", "error");
                            Toast.makeText(getApplicationContext(),"wrong username", Toast.LENGTH_LONG).show();
                        }
                    }
            );

            // Add JsonObjectRequest to the RequestQueue
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        });
    }
}