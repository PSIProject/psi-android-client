package mx.psiproject.com.teamtasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity
{
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        findViewById(R.id.logInButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText emailEditText = findViewById(R.id.emailEditText);
                final String email = emailEditText.getText().toString();

                if (! email.matches(".+@.+\\..+"))
                {
                    emailEditText.setError(getString(R.string.wrongEmail));
                    return;
                }

                if (email.isEmpty())
                {
                    emailEditText.setError(getString(R.string.requiredField));
                    return;
                }

                EditText passwordEditText = findViewById(R.id.passwordEditText);
                final String password = passwordEditText.getText().toString();

                if (password.isEmpty())
                {
                    emailEditText.setError(getString(R.string.requiredField));
                    return;
                }

                progressDialog = new ProgressDialog(LogInActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(getString(R.string.loggingIn));
                progressDialog.setMax(1);
                progressDialog.setProgress(0);
                progressDialog.show();

                String url = "http://team-tasks.000webhostapp.com/src/php/log-in.php";
                final StringRequest logInRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String responseText)
                    {
                        try
                        {
                            String response = new JSONObject(responseText).getString("status");

                            progressDialog.dismiss();
                            if (response.equals("ok")) {
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                            }
                            else
                            {
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(LogInActivity.this, getString(R.string.wrongData), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        catch (JSONException e) {}
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };

                JsonRequestsManager.addToRequestQueue(logInRequest, LogInActivity.this);
            }
        });
    }
}
