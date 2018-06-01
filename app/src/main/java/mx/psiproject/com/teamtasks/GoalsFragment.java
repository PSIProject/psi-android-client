package mx.psiproject.com.teamtasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class GoalsFragment extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.i("Flag", "Hola baby");
        JsonArrayRequest getGoalsRequest = new JsonArrayRequest(
                "http://team-tasks.000webhostapp.com/src/php/get-team-goals.php",
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(final JSONArray goals)
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    String goalsString [] = new String [goals.length()];

                                    for (int i = 0; i < goals.length(); i++)
                                    {
                                        JSONObject goal = goals.getJSONObject(i);
                                        Log.i("Goal: ",goal.getString("name"));
                                        goalsString [i] = goal.getString("name");
                                    }

                                    ListView goalsArea = getActivity().findViewById(R.id.teams_list_view);
                                    ArrayAdapter adapter = new ArrayAdapter<String> (getActivity(), R.layout.team, goalsString);
                                    goalsArea.setAdapter(adapter);
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
        JsonRequestsManager.addToRequestQueue(getGoalsRequest, getActivity());

        return inflater.inflate(R.layout.teams_layout, container, false);
    }
}
