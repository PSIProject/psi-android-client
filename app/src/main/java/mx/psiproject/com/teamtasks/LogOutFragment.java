package mx.psiproject.com.teamtasks;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 5/30/18.
 */

public class LogOutFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String url = "http://team-tasks.000webhostapp.com/src/php/log-out.php";
        final StringRequest logOutRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String responseText)
            {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.logOutMessage), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        JsonRequestsManager.addToRequestQueue(logOutRequest, getActivity());

        return inflater.inflate(R.layout.teams_layout, container, false);
    }
}
