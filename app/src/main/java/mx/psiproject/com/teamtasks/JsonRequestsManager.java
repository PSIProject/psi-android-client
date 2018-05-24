package mx.psiproject.com.teamtasks;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.Map;

public class JsonRequestsManager
{
    private RequestQueue requestQueue;
    private static JsonRequestsManager instance = null;
    private static Context context;

    private JsonRequestsManager(Context context)
    {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    private static JsonRequestsManager getInstance(Context context) {
        if (instance == null)
            instance = new JsonRequestsManager(context);

        return instance;
    }

    public static <T> void addToRequestQueue(Request<T> request, Context context)
    {
        getInstance(context).requestQueue.add(request);
    }
}
