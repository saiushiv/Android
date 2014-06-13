package com.bottlerocketapps.bootcamp.flickr.loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.bottlerocketapps.bootcamp.flickr.api.PhotoApi;
import com.bottlerocketapps.bootcamp.flickr.model.PhotoSearchResult;
import com.squareup.okhttp.OkHttpClient;

public class PhotoSearchLoader extends AsyncTaskLoader<PhotoSearchResult>{

    private static final String TAG = PhotoSearchLoader.class.getSimpleName();

    private Uri mSearchUri;
    private PhotoSearchResult mResults;
    private OkHttpClient mOkHttpClient;

    public PhotoSearchLoader(Context context, String searchTag) {
        super(context);
        mSearchUri = PhotoApi.getSearchUri(context, searchTag);
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        
        if (mResults != null) {
            //If we already have results, just deliver them. 
            deliverResult(mResults);
        } else {
            //We must call forceLoad to start loadInBackground().
            forceLoad();
        }
    }

    @Override
    public PhotoSearchResult loadInBackground() {
        PhotoSearchResult result = null;
        
        try {
            //Request the data from the flickr web service.
            URL url = new URL(mSearchUri.toString());
            
            Log.d(TAG, "Downloading " + url);
            String response = get(url);

            //Parse the result into a PhotoSearchResult object.
            result = PhotoSearchResult.fromJson(response);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException in loadInBackground()", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException in loadInBackground()", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSONException in loadInBackground()", e);
        }

        return result;
    }

    /**
     * Perform an HTTP GET request for the specified URL then 
     * return the result as a String.
     * 
     * @param url
     * @return
     * @throws IOException
     */
    private String get(URL url) throws IOException {
        HttpURLConnection connection = mOkHttpClient.open(url);
        InputStream in = null;
        try {
            // Read the response.
            in = connection.getInputStream();
            byte[] response = readFully(in);
            return new String(response, HTTP.UTF_8);
        } finally {
            if (in != null) in.close();
        }
    }

    /**
     * Read the entirety of an input stream into a byte array.
     * 
     * @param in
     * @return
     * @throws IOException
     */
    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }

    @Override
    protected void onReset() {
        super.onReset();
        
        //Discard the result. This exact result will no longer be needed.
        mResults = null;
    }
}
