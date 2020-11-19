package edu.uw.comchat.ui.connection;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;

/**
 * Used as the view model for the connections list.
 *
 * @author Adam Hall, Jerry Springer
 * @version 19 November 2020
 */
public class ConnectionListViewModel extends AndroidViewModel {
    /**
     * Mutable live data to store connection list.
     */
    private MutableLiveData<List<Connection>> mFriendList;

    /**
     * Public constructor for the view model.
     * @param application the application context.
     */
    public ConnectionListViewModel(@NonNull Application application) {
        super(application);
        mFriendList = new MutableLiveData<>();
        mFriendList.setValue(new ArrayList<>());

    }

    /**
     * Adds an observer for our connection list.
     * @param owner The owner
     * @param observer The observer
     */
    public void addConnectionListObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super List<Connection>> observer) {
        mFriendList.observe(owner, observer);
    }

    /**
     * Called if we receive an error from the webservice on our call.
     * @param error The error
     */
    private void handleError(final VolleyError error) {
        //you should add much better error handling in a production release.
        //i.e. YOUR PROJECT
        //TODO
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }

    /**
     * This method handles the resulting JSON object and parses it into
     * list of strings, and then a list of connections.
     * @param result The resulting JSON object from webservice GET request
     */
    private void handleResult(final JSONObject result) {
        try {
            JSONArray contactsArray = result.getJSONArray("contacts");
            List<String> temp = new ArrayList<>();
            for (int i = 0; i < contactsArray.length(); i++) {
                temp.add(contactsArray.getJSONObject(i).getString("email"));

            }
            List<Connection> connList = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                connList.add(new Connection(temp.get(i)));
            }
            mFriendList.setValue(connList);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatPageViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * This method will make the GET call to our webservice to retrieve a list of contacts
     * a given user has.
     *
     * @param email The users email.
     * @param jwt The users JWT given after authentication.
     */
    public void getAllConnections(final String email, final String jwt) {
        String url = getApplication().getResources().getString(R.string.connections_url) + email;
        JSONObject j = new JSONObject();
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }
}