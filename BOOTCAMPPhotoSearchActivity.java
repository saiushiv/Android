package com.bottlerocketapps.bootcamp.flickr.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.bottlerocketapps.bootcamp.R;
import com.bottlerocketapps.bootcamp.flickr.model.Photo;
import com.bottlerocketapps.bootcamp.flickr.ui.PhotoSearchGridFragment.PhotoSearchGridFragmentListener;

public class PhotoSearchActivity extends FragmentActivity implements PhotoSearchGridFragmentListener {

    private static final String FRAG_PHOTO_SEARCH_GRID = "photoSearchGrid";
    private static final String FRAG_PHOTO_SEARCH_DETAIL = "photoDetail";

    private static final String BACKSTACK_DETAIL = "backstackDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_search_activity);

        if (savedInstanceState == null) {
            /*
             * If there is no savedInstanceState then there are no Fragments
             * in the fragment_container. Load the initial Fragment.
             */
            PhotoSearchGridFragment frag = PhotoSearchGridFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.psa_fragment_container, frag, FRAG_PHOTO_SEARCH_GRID).commit();
        }
    }

    @Override
    public void onPhotoSelected(Photo selectedPhoto) {
        //A photo has been selected, display the detail fragment and add the transaction to the back stack.
        PhotoDetailFragment frag = PhotoDetailFragment.newInstance(selectedPhoto);
        getSupportFragmentManager().beginTransaction().replace(R.id.psa_fragment_container, frag, FRAG_PHOTO_SEARCH_DETAIL).addToBackStack(BACKSTACK_DETAIL).commit();
    }

}
