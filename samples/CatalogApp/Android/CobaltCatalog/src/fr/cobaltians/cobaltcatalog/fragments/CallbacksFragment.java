package fr.cobaltians.cobaltcatalog.fragments;

import android.app.AlertDialog;
import android.util.Log;
import fr.cobaltians.cobaltcatalog.R;

import fr.cobaltians.cobalt.fragments.HTMLFragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CallbacksFragment extends HTMLFragment {

	protected static String JSAddValues = "addValues";
    protected static String JSValuesCallback = "valuesCallback";
    protected static String JSEcho = "echo";
    protected static String kResult = "result";
    protected static String kValues = "values";

	private Button btnDoSomeMath, btnTestAuto;

	@Override
	protected int getLayoutToInflate() {
		return R.layout.callbacks_fragment;
	}

	@Override
	protected void setUpViews(View rootView) {
		super.setUpViews(rootView);
        btnDoSomeMath = (Button) rootView.findViewById(R.id.btnDoSomeMaths);
        btnTestAuto = (Button) rootView.findViewById(R.id.btnTestAuto);
		}

	@Override
	protected void setUpListeners() {
        btnDoSomeMath.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				JSONObject data = new JSONObject();
				try {
					ArrayList<Integer> values = new ArrayList<Integer>();
					values.add(1);
					values.add(3);
					data.put(kValues, new JSONArray(values));
					sendEvent(JSAddValues, data, JSValuesCallback);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

        btnTestAuto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //TODO
                /*JSONObject data = new JSONObject();
                try {
                    ArrayList<Integer> values = new ArrayList<Integer>();
                    values.add(1);
                    values.add(3);
                    data.put(kValues, new JSONArray(values));
                    sendEvent(JSAddValues, data, JSValuesCallback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });
	}

	// unhandled JS messages
	@Override
	protected void onUnhandledMessage(JSONObject message) {
		
	}
	@Override
	protected boolean onUnhandledEvent(String name, JSONObject data, String callback) {
        if (name.equals(JSAddValues)) {
            try {
                JSONArray values = data.getJSONArray(kValues);
                int val1 = values.getInt(0);
                int val2 = values.getInt(1);
                JSONObject result = new JSONObject();
                result.put(kResult, val1+val2);
                sendCallback(callback, result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (name.equals(JSEcho)) {
            Log.d(TAG, "data received and send to web : "+data.toString());
            sendCallback(callback, data);
            return true;
        }
		return false;
	}
	@Override
	protected boolean onUnhandledCallback(String name, JSONObject data) {
        if (name.equals(JSValuesCallback)) {
            int result = data.optInt(kResult, 0);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("result is : "+ result);
            AlertDialog mAlert = alert.create();
            mAlert.setCanceledOnTouchOutside(true);
            mAlert.show();
            return true;
        }
		return false;
	}

	@Override
	protected void onPullToRefreshRefreshed() {
	}

	@Override
	protected void onInfiniteScrollRefreshed() {		
	}
}
