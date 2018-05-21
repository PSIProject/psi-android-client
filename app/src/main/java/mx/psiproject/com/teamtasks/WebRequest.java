package mx.psiproject.com.teamtasks;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class WebRequest
{
    enum RequestMethod
    {
        GET,
        POST
    };

    public JSONObject makeServiceCall(String requestedURL, String params, RequestMethod requestMethod) throws MalformedURLException
    {
        JSONObject jsonObject = new JSONObject();

        try
        {
            HttpURLConnection connection = null;
            URL url;

            switch (requestMethod)
            {
                case GET:
                    url = new URL(requestedURL + "?" + params);
                    connection = (HttpURLConnection) url.openConnection();
                    break;

                case POST:
                    url = new URL(requestedURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(params);
                    writer.flush();
                    writer.close();
            }

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            jsonObject = new JSONObject(convertStreamToString(inputStream));
        }
        catch (MalformedURLException muE) { }
        catch (ProtocolException pE) {}
        catch (IOException ioE) {}
        catch (Exception e) {}

        return jsonObject;
    }

    private String convertStreamToString(InputStream inputStream)
    {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try
        {
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        }
        catch (IOException ioE) {}

        return stringBuilder.toString();
    }
}