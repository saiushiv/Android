package com.bottlerocketapps.bootcamp.flickr.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.bottlerocketapps.bootcamp.R;
import com.bottlerocketapps.bootcamp.flickr.api.PhotoApi;
import com.bottlerocketapps.bootcamp.flickr.loader.PhotoSearchLoader;
import com.bottlerocketapps.bootcamp.flickr.model.Photo;
import com.bottlerocketapps.bootcamp.flickr.model.PhotoSearchResult;
import com.squareup.picasso.Picasso;

public class PhotoSearchGridFragment extends Fragment {

    private static final int LOADER_ID_SEARCH = 1;

    private static final String LOADER_ARG_QUERY = "query";
    
    private static final String SAVED_STATE_QUERY = "query";

    private View mRoot;
    private GridView mGridView;
    private TextView mStatusText;
    
    private String mQuery;
    private boolean mSearchLoaderStarted;    
    private PhotoGridAdapter mPhotoGridAdapter;

    public static PhotoSearchGridFragment newInstance() {
        PhotoSearchGridFragment frag = new PhotoSearchGridFragment();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Required in order for a Fragment to get a call to onCreateOptionsMenu()
        setHasOptionsMenu(true);
        
        if (savedInstanceState != null) {
            //If we have a savedInstanceState, pull the query value from it. 
            mQuery = savedInstanceState.getString(SAVED_STATE_QUERY);            
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        //Save the last query value to be restored later.
        outState.putString(SAVED_STATE_QUERY, mQuery);        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        //If there is a pre-existing value for query when the activity is created, start the search.
        if (!TextUtils.isEmpty(mQuery)) {
            performSearch(mQuery);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        //Inflate the layout resource.
        mRoot = inflater.inflate(R.layout.photo_search_grid_fragment, container, false);
        
        //Obtain the grid view from the layout.
        mGridView = (GridView) mRoot.findViewById(R.id.psg_grid);
        //Create a new photo grid adapter.
        mPhotoGridAdapter = new PhotoGridAdapter(getActivity());
        //Attach the adapter to the grid view.
        mGridView.setAdapter(mPhotoGridAdapter);
        //Setup the on item click listener.
        mGridView.setOnItemClickListener(mPhotoItemClickListener);
        
        //Obtain the text view to display status.
        mStatusText = (TextView) mRoot.findViewById(R.id.psg_status);
        return mRoot;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        
        //Allow parent to inflate its menu.
        super.onCreateOptionsMenu(menu, inflater);
        
        //Inflate our menu using the base menu.
        inflater.inflate(R.menu.photo_search_grid, menu);
        
        //Find the search ActionView and attach a listener to it.
        MenuItem item = menu.findItem(R.id.psg_menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(mOnQueryTextListener);
    }

    /*
     * Listener for changes to the search ActionView. 
     */
    private OnQueryTextListener mOnQueryTextListener = new OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            performSearch(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    /**
     * Start or restart the search loader for the provided search query.
     * @param query
     */
    protected void performSearch(String query) {
        if (!TextUtils.isEmpty(query)) {           
            
            //Inform the user that we are searching.
            mStatusText.setVisibility(View.VISIBLE);
            mStatusText.setText(R.string.pgs_status_searching);
            
            //Remove previous results.
            mPhotoGridAdapter.clear();
            
            //Put the search argument into a bundle.
            Bundle args = new Bundle();
            args.putString(LOADER_ARG_QUERY, query);
            
            if (mSearchLoaderStarted && !TextUtils.equals(mQuery, query)) {
                /*
                 * If this instance of the activity has already started a loader and we 
                 * aren't searching for the same term again, restart the loader.
                 */
                getLoaderManager().restartLoader(LOADER_ID_SEARCH, args, mSearchLoaderCallbacks);
            } else {
                /*
                 * We have never started a loader in this instance or are searching for the same thing again.
                 */
                getLoaderManager().initLoader(LOADER_ID_SEARCH, args, mSearchLoaderCallbacks);
            }
             
            //Save the search value and the fact that this instance has started the loader.
            mQuery = query;
            mSearchLoaderStarted = true;
        }
    }

    private LoaderCallbacks<PhotoSearchResult> mSearchLoaderCallbacks = new LoaderCallbacks<PhotoSearchResult>() {

        @Override
        public Loader<PhotoSearchResult> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case LOADER_ID_SEARCH:
                    if (args != null) {
                        //Create a new instance of the photo search loader with the provided query.
                        return new PhotoSearchLoader(getActivity(), args.getString(LOADER_ARG_QUERY));
                    }
                    break;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<PhotoSearchResult> loader, PhotoSearchResult result) {
            switch (loader.getId()) {
                case LOADER_ID_SEARCH:
                    //Display the results.
                    updateResult(result);
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<PhotoSearchResult> loader) {
            switch (loader.getId()) {
                case LOADER_ID_SEARCH:
                    //Clear the grid.
                    updateResult(null);
                    break;
            }
        }
    };

    private PhotoSearchGridFragmentListener mFragmentListener;

    private void updateResult(PhotoSearchResult result) {
        if (result != null) {
            mPhotoGridAdapter.swapItems(result.getPhotos());
        } else {
            mPhotoGridAdapter.clear();
        }
        
        if (mPhotoGridAdapter.getCount() > 0) {
            //Hide status text if we received results.
            mStatusText.setVisibility(View.INVISIBLE);
        } else {
            //Display no results message.
            mStatusText.setVisibility(View.VISIBLE);
            mStatusText.setText(R.string.pgs_status_no_results);
        }
    }

    private class PhotoGridAdapter extends BaseAdapter {

        private ArrayList<Photo> mPhotos;
        private LayoutInflater mLayoutInflater;

        public PhotoGridAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            mPhotos = new ArrayList<Photo>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            //Get the data for this position in the grid.
            Photo photo = getItem(position);

            Holder holder;
            if (convertView == null) {
                //The convertview has not been inflated, inflate an item now.
                convertView = mLayoutInflater.inflate(R.layout.photo_search_grid_item, parent, false);
                //Create a new holder object
                holder = new Holder();
                //Set the photo attribute of the holder
                holder.photo = (ImageView) convertView.findViewById(R.id.psgi_photo);
                //Save the holder in this convertView
                convertView.setTag(holder);
            } else {
                //Retrieve the holder from the existing convertView
                holder = (Holder) convertView.getTag();
            }

            //Load the thumbnail image into the photo imageview
            Picasso.with(getActivity()).load(PhotoApi.getThumbnailPhotoUri(getActivity(), photo)).into(holder.photo);

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Photo getItem(int position) {
            return mPhotos.get(position);
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }

        public void swapItems(List<Photo> photos) {
            /*
             * Create a shallow copy of the supplied list to avoid 
             * updates to list outside of this adapter.
             */
            mPhotos = new ArrayList<Photo>(photos);
            
            //Notify the adapter that the data has been modified.
            notifyDataSetChanged();
        }
        
        public void clear() {
            //Empty the list
            mPhotos.clear();
            
            //Notify the adapter that the data has been modified.
            notifyDataSetChanged();
        }

        private class Holder {
            public ImageView photo;
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        //When the activity is attached we enforce that it is a listener for this fragment and store a reference.
        if (activity instanceof PhotoSearchGridFragmentListener) {
            mFragmentListener = (PhotoSearchGridFragmentListener) activity;
        } else {
            throw new ClassCastException(activity.getClass().getCanonicalName() + " must implement " + PhotoSearchGridFragmentListener.class.getCanonicalName());
        }
    }
    
    /*
     * Listener for individual photos being touched in the grid. 
     */
    private OnItemClickListener mPhotoItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Photo selectedPhoto = mPhotoGridAdapter.getItem(position);
            mFragmentListener.onPhotoSelected(selectedPhoto);
        }
    };
    
    /**
     * Listener that will receive callbacks from the PhotoSearchGridFragment when a photo is selected.
     */
    public interface PhotoSearchGridFragmentListener {
        public void onPhotoSelected(Photo selectedPhoto);
    }
    
}
