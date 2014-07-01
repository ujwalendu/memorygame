package com.up.games.memorygame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class ImageUrlFetcher extends AsyncTask<Void, Void, Void>
{

    interface UrlFetchedListener {
    
        public void onUrlsFetched(List<String> urls);
    }

    private static final String TAG = "ImageUrlFetcher";
    
    private UrlFetchedListener mListener;
    private String mImageSourceUrl;
    
    ImageUrlFetcher(String imageSource, UrlFetchedListener listener)
    {
        mListener = listener;
        mImageSourceUrl = imageSource;
    }
    
    private List<String> getImageUrlsResponse(HttpResponse response) throws IllegalStateException, IOException, JSONException {
        List<String> urls = new ArrayList<String>();
        
        if (response != null) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                
                JSONObject jsonResponse = new JSONObject(responseString);
                JSONArray urlItemsArray = jsonResponse.getJSONArray("items");
                
                for(int i = 0; i < urlItemsArray.length(); i++) {
                    JSONObject urlItem = urlItemsArray.getJSONObject(i);
                    JSONObject media = urlItem.getJSONObject("media");
                    String url = media.getString("m");
                    urls.add(url);
                }
            }
        }
        
        return urls;
    }
    
    @Override
    protected Void doInBackground(Void... arg0)
    {
        List<String> urls = new ArrayList<String>();
        HttpGet request = new HttpGet(mImageSourceUrl);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        HttpClient client = new DefaultHttpClient();
        try {
            urls = getImageUrlsResponse(client.execute(request));
        } catch (ClientProtocolException e) {
            Log.d(TAG, "Exception while fetching image urls " + e.getLocalizedMessage());
        } catch (IOException e) {
            Log.d(TAG, "Exception while fetching image urls " + e.getLocalizedMessage());
        } catch (IllegalStateException e) {
            Log.d(TAG, "Exception while fetching image urls " + e.getLocalizedMessage());
        } catch (JSONException e) {
            Log.d(TAG, "Exception while fetching image urls " + e.getLocalizedMessage());
        } finally {
            client.getConnectionManager().shutdown();
        }
        
        mListener.onUrlsFetched(urls);
        return null;
    }

}
