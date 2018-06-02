package mx.psiproject.com.teamtasks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        JsonArrayRequest getUpcomingTasksRequest = new JsonArrayRequest(
                "http://team-tasks.000webhostapp.com/src/php/get-upcoming-tasks.php",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray tasks) {
                        try
                        {
                            CustomRecyclerViewAdapter.ElementData data [] = new CustomRecyclerViewAdapter.ElementData [tasks.length()];

                            for (int i = 0; i < tasks.length(); i++)
                            {
                                JSONObject task = tasks.getJSONObject(i);
                                data [i] = new CustomRecyclerViewAdapter.ElementData(task.getString("name"),
                                        task.getInt("id"));
                            }

                            final RecyclerView tasksArea = getActivity().findViewById(R.id.elements_area);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            tasksArea.setLayoutManager(layoutManager);
                            RecyclerView.Adapter adapter = new CustomRecyclerViewAdapter(data, new CustomRecyclerViewAdapter.OnClickListener() {
                                @Override
                                public void onClick(CustomRecyclerViewAdapter.ElementData data) {
                                    onTaskClicked(data);
                                }
                            });
                            tasksArea.setAdapter(adapter);
                        }
                        catch (JSONException e) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                });
        JsonRequestsManager.addToRequestQueue(getUpcomingTasksRequest, getActivity());
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    public void onTaskClicked(CustomRecyclerViewAdapter.ElementData data)
    {
    }
}
