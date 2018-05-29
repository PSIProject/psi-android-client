package mx.psiproject.com.teamtasks;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TeamsFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        JsonArrayRequest getUserTeamsRequest = new JsonArrayRequest(
                "http://team-tasks.000webhostapp.com/src/php/get-user-teams.php",
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(final JSONArray teams)
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    String teamsString [] = new String [teams.length()];

                                    for (int i = 0; i < teams.length(); i++)
                                    {
                                        JSONObject team = teams.getJSONObject(i);
                                        teamsString [i] = team.getString("name") + " - " +
                                                          Integer.toString(team.getInt("members")) + " " +
                                                          getString(R.string.members);
                                    }

                                    ListView teamsArea = getActivity().findViewById(R.id.teams_list_view);
                                    ArrayAdapter adapter = new ArrayAdapter<String> (getActivity(), R.layout.team, teamsString);
                                    teamsArea.setAdapter(adapter);
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
        JsonRequestsManager.addToRequestQueue(getUserTeamsRequest, getActivity());

        return inflater.inflate(R.layout.teams_layout, container, false);
    }
}
