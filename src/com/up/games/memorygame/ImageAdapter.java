package com.up.games.memorygame;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.up.games.memorygame.MainActivity.ViewHolder;

public class ImageAdapter extends BaseAdapter
{
    private DisplayImageOptions mDisplayImageOptions;
    
    private List<String> mImageUrls;
    private Context mContext;
    private ImageLoader imageLoader;
    
    public ImageAdapter(List<String> urls, Context context, ImageLoader imgLoader, DisplayImageOptions options)
    {
        mImageUrls = urls;
        mContext = context;
        imageLoader = imgLoader;
        mDisplayImageOptions = options;
    }

    /*
     * public ImageAdapter(int drawableImageId, Context context, ImageLoader imgLoader) { mContext = context;
     * imageLoader = imgLoader; for(int i = 0; i< 9; i++) { mImageUrls.add("drawable://" + drawableImageId); } }
     */
    
    @Override
    public int getCount()
    {
        return mImageUrls.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }


    
    @Override
    public View getView(int position, View counterView, ViewGroup parent)
    {
        final ViewHolder holder;
        View view = counterView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        imageLoader.displayImage(mImageUrls.get(position), holder.imageView, mDisplayImageOptions, new SimpleImageLoadingListener()
        {
            @Override
            public void onLoadingStarted(String imageUri, View view)
            {
                holder.progressBar.setProgress(0);
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason)
            {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
            {
                holder.progressBar.setVisibility(View.GONE);
            }
        }, new ImageLoadingProgressListener()
        {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total)
            {
                holder.progressBar.setProgress(Math.round(100.0f * current / total));
            }
        });
        return view;
    }

}
