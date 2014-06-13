package com.bottlerocketapps.bootcamp.flickr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Flickr photo search result.
 */
public class PhotoSearchResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    //Implementation is an ArrayList to permit serialization.
    private ArrayList<Photo> mPhotos;
    
    public PhotoSearchResult() {
        mPhotos = new ArrayList<Photo>();
    }

    public void addPhoto(Photo photo) {
        mPhotos.add(photo);
    }
    
    public List<Photo> getPhotos() {
        return mPhotos;
    }
    
    /**
     * Process a JSON string into a PhotoSearchResult object. 
     * @param response
     * @return
     * @throws JSONException
     */
    public static PhotoSearchResult fromJson(String response) throws JSONException {
        if (response != null) {
            PhotoSearchResult result = new PhotoSearchResult();
            JSONObject json = new JSONObject(response);

            //Get the photo array from the photos object.
            JSONArray array = json.getJSONObject("photos").getJSONArray("photo");
            
            for (int i = 0; i < array.length(); i++) {
                //Parse each individual photo.
                Photo photo = Photo.fromJsonObject(array.getJSONObject(i));
                
                //If the parse results in a photo object, add it to the list.
                if (photo != null) result.addPhoto(photo);
            }
            return result;
        }
        return null;
    }

}
