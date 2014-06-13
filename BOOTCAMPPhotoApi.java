package com.bottlerocketapps.bootcamp.flickr.api;

import java.util.Locale;

import android.content.Context;
import android.net.Uri;

import com.bottlerocketapps.bootcamp.R;
import com.bottlerocketapps.bootcamp.flickr.model.Photo;

public class PhotoApi {
    
    /**
     * Returns a URI to perform a search with the Flickr REST API.
     * 
     * @param context
     * @param searchQuery
     * @return
     */
    public static Uri getSearchUri(Context context, String searchQuery) {
        //Create the base URI object.
        Uri baseUri = Uri.parse("http://api.flickr.com/services/rest/");

        //Begin building upon the base URL
        Uri.Builder builder = baseUri.buildUpon();
        
        //Add query parameters to the request to specify the data format etc. 
        builder.appendQueryParameter("format", "json");
        builder.appendQueryParameter("method", "flickr.photos.search");
        builder.appendQueryParameter("safe_search", "1");
        builder.appendQueryParameter("api_key", context.getString(R.string.flickr_api_key));
        builder.appendQueryParameter("nojsoncallback", "1");
        builder.appendQueryParameter("text", searchQuery);
        return builder.build();
    }
    
    /**
     * Returns the URI to obtain a small, square thumbnail image.
     * 
     * @param context
     * @param photo
     * @return
     */
    public static String getThumbnailPhotoUri(Context context, Photo photo) { 
        return String.format(Locale.US, "http://farm%1$d.static.flickr.com/%2$s/%3$s_%4$s_s.jpg", photo.farm, photo.server, photo.id, photo.secret);
    }
    
    /**
     * Returns the URI to obtain the full sized image.
     * 
     * @param context
     * @param photo
     * @return
     */
    public static String getNormalPhotoUri(Context context, Photo photo) { 
        return String.format(Locale.US, "http://farm%1$d.static.flickr.com/%2$s/%3$s_%4$s.jpg", photo.farm, photo.server, photo.id, photo.secret);
    }
}
