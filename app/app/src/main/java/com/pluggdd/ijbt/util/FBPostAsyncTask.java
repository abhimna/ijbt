package com.pluggdd.ijbt.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.pluggdd.ijbt.interfaces.FBPostListener;

public class FBPostAsyncTask extends AsyncTask<Bundle, Void, GraphResponse> {
    Activity context;
    FBPostListener fbPostListener;
    public GraphResponse graphResponsee;

    public FBPostAsyncTask(Activity context, FBPostListener fbPostListener) {
        this.context = context;
        this.fbPostListener = fbPostListener;
    }

    @Override
    protected GraphResponse doInBackground(Bundle... params) {
        Bundle param = params[0];
        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/me/feed", param, HttpMethod.POST,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        graphResponsee = graphResponse;
                        return;
                    }
                });
        request.executeAndWait();
        return graphResponsee;
    }

    @Override
    protected void onPostExecute(GraphResponse graphResponse) {
        super.onPostExecute(graphResponse);
        if (graphResponse.getError() == null) {
            fbPostListener.onPostComplete("photo");
        } else {
            fbPostListener.onPostError(graphResponse);
        }
    }
}
