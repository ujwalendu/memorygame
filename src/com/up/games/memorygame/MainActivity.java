package com.up.games.memorygame;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.up.games.memorygame.ImageUrlFetcher.UrlFetchedListener;

public class MainActivity extends Activity implements UrlFetchedListener
{

    private static final String TAG = "MAINACTIVITY";

    private static final String flickrUrl =
        "https://api.flickr.com/services/feeds/photos_public.gne?tags=monkey&format=json&nojsoncallback=1";

    private List<String> mImageUrls;
    
    private ProgressDialog mRingProgressDialog;
    
    private GridView mGridView;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    private DisplayImageOptions mDisplayImageOptions;
    
    private List<Integer> matchedImageIds = new ArrayList<Integer>();
    
    private volatile boolean isGameFinished = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRingProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Loading ...", true);
        mRingProgressDialog.setCancelable(false);
   
        mDisplayImageOptions = Utility.getDisplayImageOptions();
        ImageLoaderConfiguration imageLoaderConfig = Utility.getImageLoaderConfiguration(getApplicationContext());
        imageLoader.init(imageLoaderConfig);
        
        new ImageUrlFetcher(flickrUrl, this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private int mainImageId = Utility.getRandomWithExclusion(0, 8, matchedImageIds);
    private List<String> defaultImageUris = Utility.getListOfDefaultImgaeUris();
    
    private void displayDefaultImages() {
        runOnUiThread( new Runnable()
        {
           
            @Override
            public void run()
            {
                Log.d(TAG, "main image id " + mainImageId);
                final ImageView mainImage = (ImageView) findViewById(R.id.imageMatch);
                imageLoader.displayImage(mImageUrls.get(mainImageId), mainImage);
                
                
                mGridView.setAdapter(new ImageAdapter(defaultImageUris, 
                    getApplicationContext(), imageLoader, mDisplayImageOptions));
                
                mGridView.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView< ? > parent, View view, int position, long id)
                    {
                        Log.d(TAG, "Item clicked position : " + position + "  id : " + id + "  url : " + mImageUrls.get(position));
                        if (!isGameFinished) {
                            ViewHolder holder = (ViewHolder) view.getTag();
                            imageLoader.displayImage(mImageUrls.get(position), holder.imageView);
                            new Timer().schedule(new WaitAndUpdateImage(1, holder.imageView, position), 0, 500);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Restart The Game", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void displayGrid()
    {
        runOnUiThread( new Runnable()
        {
           
            @Override
            public void run()
            {
                mGridView = (GridView) findViewById(R.id.gridView1);
                mGridView.setAdapter(new ImageAdapter(mImageUrls, getApplicationContext(), imageLoader, mDisplayImageOptions));
                
                new Timer().schedule(new UpdateTimer(), 0, 1000);
            }
        });
    }

    @Override
    public void onUrlsFetched(List<String> urls)
    {
        mImageUrls = urls.subList(0, 9);;
        Log.d(TAG, "Image Urls fetched " + urls);
        try {
            mRingProgressDialog.dismiss();
            mRingProgressDialog = null;
        } catch (Exception e) {

        }
        displayGrid();
    }

    private class UpdateTimer extends TimerTask {

        private int count = 15;
        
        final UpdateTimer parent = this;
        
        @Override
        public void run()
        {
            Log.d(TAG, "update timer with " + count);
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    TextView tv = (TextView) findViewById(R.id.textView2);
                    tv.setText("Time remaining : 00:00:"+count);
                    count--;
                    if(count <= -1 ) {
                        parent.cancel();
                        displayDefaultImages();
                    }
                }
            });
        }
    }

    private class WaitAndUpdateImage extends TimerTask {

        private int _count = 1;
        private ImageView _imgView;
        private int _position;
        final WaitAndUpdateImage parent = this;
        
        WaitAndUpdateImage(int numTimes, ImageView imgView, int position)
        {
            _count = numTimes;
            _imgView = imgView;
            _position = position;
        }
        
        @Override
        public void run()
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    _count--;
                    if(_count <= -1 ) {
                        parent.cancel();
                        if(mainImageId == _position) {
                            matchedImageIds.add(_position);
                            
                            if(matchedImageIds.size() == 9) {
                                isGameFinished = true;
                                Toast.makeText(getApplicationContext(), "Game Finished", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //Log.d(TAG, "main image id " + mainImageId);
                            //Log.d(TAG, "matched images " + matchedImageIds);
                            ImageView mainImage = (ImageView) findViewById(R.id.imageMatch);               
                            mainImageId = Utility.getRandomWithExclusion(0, 8, matchedImageIds);
                            Log.d(TAG, "next main image id " + mainImageId);
                            imageLoader.displayImage(mImageUrls.get(mainImageId), mainImage);
                        } else {
                            imageLoader.displayImage(defaultImageUris.get(mainImageId), _imgView);
                        }
                    }
                }
            });
        }
    }

    public static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }

}
