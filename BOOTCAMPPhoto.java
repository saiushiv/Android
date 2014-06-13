package com.bottlerocketapps.bootcamp.flickr.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Flickr photo object.
 */
public class Photo implements Serializable {
 
    private static final long serialVersionUID = 1L;
    
    public String id;
    public String owner;
    public String secret;
    public String server;
    public int farm;
    public String title;
    public boolean isPublic;
    public boolean isFriend;
    public boolean isFamily;
    
    /**
     * Create single photo object from JSONObject.
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    public static Photo fromJsonObject(JSONObject json) throws JSONException {
        Photo photo = new Photo();
        photo.id = json.getString("id");
        photo.owner = json.getString("owner");
        photo.secret = json.getString("secret");
        photo.server = json.getString("server");
        photo.farm = json.getInt("farm");
        photo.title = json.getString("title");
        photo.isPublic = json.getInt("ispublic") == 1;
        photo.isFriend = json.getInt("isfriend") == 1;
        photo.isFamily = json.getInt("isfamily") == 1;
        return photo;
    }

}
