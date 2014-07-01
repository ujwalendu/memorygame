package com.up.games.memorygame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.up.games.memorygame.R;

public class Utility
{

    public Utility()
    {
    }
    
    public static int getRandomWithExclusion(int start, int end, List<Integer>exclude) {
        
        List<Integer> allNum = new ArrayList<Integer>();
        for(int i = start; i<= end; i++) {
            allNum.add(i);
        }
        
        allNum.removeAll(exclude);
        
        int random = new Random().nextInt(allNum.size());
        
        Log.d("Utility", "random " + allNum.get(random));
        return allNum.get(random);
    }
    
    public static DisplayImageOptions getDisplayImageOptions() {
        return new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_launcher)
        .showImageForEmptyUri(R.drawable.ic_launcher)
        .showImageOnFail(R.drawable.ic_launcher)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
        
    }
    
    public static ImageLoaderConfiguration getImageLoaderConfiguration(Context context) {
        return new ImageLoaderConfiguration.Builder(context)
        .defaultDisplayImageOptions(getDisplayImageOptions())
        .build();
    }
    
    public static List<String> getListOfDefaultImgaeUris() {
        List<String> urls = new ArrayList<String>();
        for(int i=0; i<9;i++) {
            urls.add("drawable://"+R.drawable.ic_launcher);
        }
        return urls;
    }
}
