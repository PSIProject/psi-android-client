package mx.psiproject.com.teamtasks;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
                    public void onResponse(final JSONArray teams) {
                        try {
                            CustomRecyclerViewAdapter.ElementData data [] = new CustomRecyclerViewAdapter.ElementData [teams.length()];

                            for (int i = 0; i < teams.length(); i++) {
                                JSONObject team = teams.getJSONObject(i);
                                data [i] = new CustomRecyclerViewAdapter.ElementData(team.getString("name"),
                                                                                     team.getInt("id"));
                            }

                            final RecyclerView teamsArea = getActivity().findViewById(R.id.elements_area);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            teamsArea.setLayoutManager(layoutManager);
                            RecyclerView.Adapter adapter = new CustomRecyclerViewAdapter(data, new CustomRecyclerViewAdapter.OnClickListener() {
                                @Override
                                public void onClick(CustomRecyclerViewAdapter.ElementData data) {
                                    onTeamClicked(data);
                                }
                            });
                            teamsArea.setAdapter(adapter);
                        } catch (JSONException e) {}
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
        JsonRequestsManager.addToRequestQueue(getUserTeamsRequest, getActivity());

        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    public void onTeamClicked(final CustomRecyclerViewAdapter.ElementData team)
    {
        StringRequest storeTeamIdRequest = new StringRequest(Request.Method.POST,
                "http://team-tasks.000webhostapp.com/src/php/store-team-id.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Fragment fragment = new GoalsFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_content, fragment);
                        fragmentTransaction.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(team.text);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("team_id", Integer.toString(team.id));
                return params;
            }
        };
        JsonRequestsManager.addToRequestQueue(storeTeamIdRequest, TeamsFragment.this.getActivity());
    }
}
