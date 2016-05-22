package com.github.nikolaymakhonin.android_app_example.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.nikolaymakhonin.android_app_example.R;
import com.github.nikolaymakhonin.android_app_example.contracts.IFragmentWithHeader;
import com.github.nikolaymakhonin.android_app_example.contracts.IHasTitle;
import com.github.nikolaymakhonin.android_app_example.helpers.PermissionsHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/** Docs: <a href="https://developers.google.com/places/android-api/start?hl=ru#connect-client">developers.google.com introduce</a> */

/** Docs: <a href="https://developers.google.com/android/guides/api-client#handle_connection_failures">developers.google.com details</a> */
public class GoogleMapFragment extends Fragment implements IHasTitle, IFragmentWithHeader, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback
{

    private static final int LAYOUT          = R.layout.fragment_google_map;
    private static final int HEADER_COLOR    = R.color.toolBarForBackground7;
    private static final int HEADER_DRAWABLE = R.drawable.navigation_header_background_7;

    private static final String LOG_TAG = "GoogleMapFragment";

    private View                 _contentView;
    private ObservableScrollView _scrollView;
    private MapView              _mapView;
    private GoogleMap            _map;

    //region Override methods

    @Override
    public String getTitle() {
        return "Google Map";
    }

    @Override
    public int getHeaderColorResId() {
        return HEADER_COLOR;
    }

    @Override
    public int getHeaderDrawableResId() {
        return HEADER_DRAWABLE;
    }

    //endregion

    //region Create Instance

    public static GoogleMapFragment getInstance() {
        Bundle            args     = new Bundle();
        GoogleMapFragment fragment = new GoogleMapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //endregion

    //region Init Controls

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState)
    {
        _contentView = inflater.inflate(LAYOUT, container, false);
        initControls();
        initGoogleApiClient();

        return _contentView;
    }

    private void initControls() {
        _scrollView = (ObservableScrollView) _contentView.findViewById(R.id.scrollView);

        initScrollView();
    }

    private void initScrollView() {
        MaterialViewPagerHelper.registerScrollView(getActivity(), _scrollView, null);
    }

    //region Init Google Maps

    private void initMapView(Bundle savedInstanceState) {
        // Gets the MapView from the XML layout and creates it
        _mapView = (MapView) _contentView.findViewById(R.id.mapView);
        _mapView.onCreate(savedInstanceState);
        _mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Gets to GoogleMap from the MapView and does initialization stuff
        _map = googleMap;
        _map.getUiSettings().setMyLocationButtonEnabled(false);

        PermissionsHelper.requestPermissions(getActivity(), new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        })
            .subscribe(granted -> {
                if (granted) {
                    //noinspection MissingPermission
                    _map.setMyLocationEnabled(true);
                }
            });


        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        int errorCode = MapsInitializer.initialize(getActivity());
        Log.e(LOG_TAG, "Error maps initialize, errorCode = " + errorCode);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(22.277872, 114.1762067), 16.45f); //Hong-Kong
        _map.animateCamera(cameraUpdate);

    }

    //endregion

    //endregion

    //region Init Google API

    private GoogleApiClient _googleApiClient;

    private void initGoogleApiClient() {

        _googleApiClient = new GoogleApiClient
            .Builder(getContext().getApplicationContext())
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
    }

    //endregion

    //region Life cycle

    @Override
    public void onStart() {
        super.onStart();
        _googleApiClient.connect();
    }

    @Override
    public void onStop() {
        _googleApiClient.disconnect();
        super.onStop();
    }

    //endregion

    //region Google Services callbacks

    //region Error Handler

    // Request code to use when launching the resolution activity
    private static final int    REQUEST_RESOLVE_ERROR = 1001;
    // Bool to track whether the app is already resolving an error
    private boolean             _resolvingError;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (_resolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                _resolvingError = true;
                result.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                _googleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            _resolvingError = true;
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        Dialog errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(
            getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        errorDialog.setOnDismissListener(dialog -> _resolvingError = false);
        errorDialog.show();
    }

    //endregion

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initMapView(bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //endregion
}
