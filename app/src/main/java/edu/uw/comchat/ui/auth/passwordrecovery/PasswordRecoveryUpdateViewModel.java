package edu.uw.comchat.ui.auth.passwordrecovery;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.uw.comchat.R;
import edu.uw.comchat.io.RequestQueueSingleton;
import edu.uw.comchat.util.HandleRequestError;
import org.json.JSONObject;




/**
 * Store response from server for password recovery (second page).
 */
// Ignore checkstyle member name error.
public class PasswordRecoveryUpdateViewModel extends AndroidViewModel {
  private MutableLiveData<JSONObject> mResponse;

  /**
   * Constructor.
   *
   * @param application the application
   */
  public PasswordRecoveryUpdateViewModel(@NonNull Application application) {
    super(application);
    mResponse = new MutableLiveData<>();
    mResponse.setValue(new JSONObject());
  }

  /**
   * Add observer to a view model instance.
   *
   * @param owner life cycle of the class bound to model instance
   * @param observer the response
   */
  public void addResponseObserver(@NonNull LifecycleOwner owner,
                                  @NonNull Observer<? super JSONObject> observer) {
    mResponse.observe(owner, observer);
  }


  /**
   * PUT request to webservice to reset password.
   *
   * @param email user's email address
   * @param pin pin code
   * @param password a new password
   */
  public void connect(final String email, final String pin, final String password) {
    String url = getApplication().getResources().getString(R.string.base_url)
            + "resetpassword?email=" + email
            + "&pin=" + pin
            + "&password=" + password;

    Request request = new JsonObjectRequest(
            Request.Method.PUT,
            url,
            null, //no body for this get request
            mResponse::setValue,
            error -> HandleRequestError.handleError(error, mResponse));
    request.setRetryPolicy(new DefaultRetryPolicy(
            10_000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //Instantiate the RequestQueue and add the request to the queue
    RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
            .addToRequestQueue(request);
  }
}