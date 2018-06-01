package mx.psiproject.com.teamtasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 5/31/18.
 */

public class TasksFragment extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        JsonArrayRequest getTasksRequest = new JsonArrayRequest(
                "http://team-tasks.000webhostapp.com/src/php/get-tasks.php",
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(final JSONArray tasks)
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    String tasksString [] = new String [tasks.length()];

                                    for (int i = 0; i < tasks.length(); i++)
                                    {
                                        JSONObject task = tasks.getJSONObject(i);
                                        tasksString [i] = task.getString("name");
                                    }

                                    ListView tasksArea = getActivity().findViewById(R.id.teams_list_view);
                                    ArrayAdapter adapter = new ArrayAdapter<String> (getActivity(), R.layout.team, tasksString);
                                    tasksArea.setAdapter(adapter);
                                }
                                catch (JSONException e) {}
                            }

                        });
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
        JsonRequestsManager.addToRequestQueue(getTasksRequest, getActivity());

        return inflater.inflate(R.layout.teams_layout, container, false);
    }
}
