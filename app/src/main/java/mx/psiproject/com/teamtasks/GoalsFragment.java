package mx.psiproject.com.teamtasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class GoalsFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        JsonArrayRequest getGoalsRequest = new JsonArrayRequest(
                "http://team-tasks.000webhostapp.com/src/php/get-team-goals.php",
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(final JSONArray goals)
                    {
                        try
                        {
                            CustomRecyclerViewAdapter.ElementData data [] = new CustomRecyclerViewAdapter.ElementData [goals.length()];

                            for (int i = 0; i < goals.length(); i++)
                            {
                                JSONObject goal = goals.getJSONObject(i);
                                data [i] = new CustomRecyclerViewAdapter.ElementData(goal.getString("name"),
                                                                                     goal.getInt("id"));
                            }

                            final RecyclerView goalsArea = getActivity().findViewById(R.id.elements_area);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            goalsArea.setLayoutManager(layoutManager);
                            RecyclerView.Adapter adapter = new CustomRecyclerViewAdapter(data, new CustomRecyclerViewAdapter.OnClickListener() {
                                @Override
                                public void onClick(CustomRecyclerViewAdapter.ElementData data) {
                                    onGoalClicked(data);
                                }
                            });
                            goalsArea.setAdapter(adapter);
                        }
                        catch (JSONException e) {}
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i("GoalsFragment", error.getMessage());
                    }
                });
        JsonRequestsManager.addToRequestQueue(getGoalsRequest, getActivity());

        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    public void onGoalClicked(final CustomRecyclerViewAdapter.ElementData goal)
    {
        StringRequest storeGoaldIdRequest = new StringRequest(Request.Method.POST,
                "http://team-tasks.000webhostapp.com/src/php/store-goal-id.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Fragment fragment = new TasksFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_content, fragment);
                        fragmentTransaction.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(goal.text);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("goal_id", Integer.toString(goal.id));
                return params;
            }
        };
        JsonRequestsManager.addToRequestQueue(storeGoaldIdRequest, getActivity());
    }
}
