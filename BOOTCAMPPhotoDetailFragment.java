package com.bottlerocketapps.bootcamp.flickr.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bottlerocketapps.bootcamp.R;
import com.bottlerocketapps.bootcamp.flickr.api.PhotoApi;
import com.bottlerocketapps.bootcamp.flickr.model.Photo;
import com.squareup.picasso.Picasso;

public class PhotoDetailFragment extends Fragment {

    private static final String ARG_PHOTO = "photo";
    
    private View mRoot;
    private ImageView mPhotoView;
    
    private Photo mPhoto;

    /**
     * Create a new instance of the PhotoDetailFragment to display the supplied photo object.
     * 
     * @param selectedPhoto
     * @return
     */
    public static PhotoDetailFragment newInstance(Photo selectedPhoto) {
        PhotoDetailFragment frag = new PhotoDetailFragment();

        //Serialize the photo object into the arguments bundle
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, selectedPhoto);
        frag.setArguments(args);
        
        return frag;
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Extract the photo object from the arguments bundle. 
        mPhoto = (Photo) getArguments().getSerializable(ARG_PHOTO);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.photo_detail_fragment, container, false);
        
        //Find the image view
        mPhotoView = (ImageView) mRoot.findViewById(R.id.pd_image);

        //Use Picasso to load the normal sized photo
        Picasso.with(getActivity()).load(PhotoApi.getNormalPhotoUri(getActivity(), mPhoto)).into(mPhotoView);
        
        return mRoot;
    }
}
