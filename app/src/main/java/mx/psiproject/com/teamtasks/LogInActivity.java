package mx.psiproject.com.teamtasks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.net.MalformedURLException;

public class LogInActivity extends AppCompatActivity
{
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViewById(R.id.logInButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText emailEditText = findViewById(R.id.emailEditText);
                String email = emailEditText.getText().toString();

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
                String password = passwordEditText.getText().toString();

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
                new LogIn(email, password).execute();
            }
        });
    }

    private class LogIn extends AsyncTask<Void, Void, Void>
    {
        private String email;
        private String password;

        public LogIn(String email, String password)
        {
            this.email = email;
            this.password = password;
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            WebRequest logInRequest = new WebRequest();

            try
            {
                String serverResponse = logInRequest.makeServiceCall("http://team-tasks.000webhostapp.com/src/php/log-in.php",
                        "email=" + email + "&password=" + password, WebRequest.RequestMethod.POST).getString("status");

                progressDialog.setProgress(1);
                progressDialog.dismiss();
                if (serverResponse.equals("ok"))
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
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
            catch (MalformedURLException e) {}
            catch (JSONException e) {}

            return null;
        }
    }
}
