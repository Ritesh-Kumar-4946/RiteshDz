package com.dz.ritesh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.dz.ritesh.Constant.Appconstant;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.github.florent37.expectanim.ExpectAnim;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

import static com.github.florent37.expectanim.core.Expectations.width;

/**
 * Created by ritesh on 18/2/17.
 */
@SuppressWarnings("deprecation")
public class MainActivity extends EasyLocationAppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar_Main;

    @BindView(R.id.containerView)
    FrameLayout frame_container;

    @BindView(R.id.rl_dr_home_main)
    RelativeLayout rl_Dr_Home;

    @BindView(R.id.rl_dr_my_product_main)
    RelativeLayout rl_Dr_My_Product;

    @BindView(R.id.rl_dr_wallet_main)
    RelativeLayout rl_Dr_Wallet;

    @BindView(R.id.rl_dr_get_free_product_main)
    RelativeLayout rl_Dr_Get_Free_Product;

    @BindView(R.id.rl_dr_setting_main)
    RelativeLayout rl_Dr_Setting;

    @BindView(R.id.rl_dr_call_us_main)
    RelativeLayout rl_Dr_Call_Us;

    @BindView(R.id.rl_dr_help_main)
    RelativeLayout rl_Dr_Help;

    @BindView(R.id.rl_profile_main_progressbar)
    RelativeLayout rl_profileprogressbar;

    @BindView(R.id.rl_dr_logout_main)
    RelativeLayout rl_Dr_Logout;

    @BindView(R.id.rl_go_pickup_location)
    RelativeLayout rl_pickup_location_screen;


    @BindView(R.id.tv_pickup_location)
    TextView TV_pickup_Location;

    @BindView(R.id.tv_location_location)
    TextView TV_Other_Location;


    @BindView(R.id.tv_geocode_address)
    TextView TV_Address_GeoCode;

    @BindView(R.id.iv_mylocation)
    ImageView map_IV_mylocation;


    @BindView(R.id.profile_main_image)
    CircleImageView IVMainProfileImage;


    @BindView(R.id.iv_myimage)
    ImageView map_IV_picture;

    @BindView(R.id.iv_marker)
    View IV_marker_icon;

    @BindView(R.id.iv_marker_shadow)
    View IV_marker_shadow;

    @BindView(R.id.iv_searching_places)
    ImageView IV_searching_places;


    @BindView(R.id.iv_feverate_places_plain)
    ImageView IV_feverate_places;


    @BindView(R.id.rl_geocode_progress)
    RelativeLayout Rl_geocode_progressview;

    @BindView(R.id.cv_location_loacation)
    CardView CVTransferLoc_to_Loc;
    CircularProgressBar mCircularGeocode_ProgressBar;


    String result = "", error = "", Str_user_Latitude = "", Str_user_Longitude = "";
    String str_username = "", str_emailid = "", str_password = "", STR_userID = "", STR_User_image = "",
            STR_user_name = "", STR_user_email = "", STR_user_hair_style = "";

    String Str_Lat = "", Str_Long = "", CompleteAddress = "", Address = "", City = "", State = "", Country = "", PostalCode = "", KnownName = "";

    /*https://www.learn2crack.com/2013/12/android-google-maps-api-v2-example.html*/

    //To store longitude and latitude from map
    private double latitude;
    private double longitude;


    private double Rlatitude;
    private double Rlongitude;


    MapFragment mapFragment;
    UiSettings mUiSettings;
    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //Our Map
    private GoogleMap googleMap;

    Geocoder geocoder;
    List<Address> addresses;


    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    private List<Data> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MultiDex.install(this);

        /*get current lat-long (Start)*/
        locationData();
        /*get current lat-long (End)*/

        setSupportActionBar(toolbar_Main);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainActivity.this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);


        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar_Main,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mCircularGeocode_ProgressBar = (CircularProgressBar) findViewById(R.id.cpb_geocode_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularGeocode_ProgressBar.getIndeterminateDrawable()).start();
        updateValues();


        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);

        STR_User_image = Appconstant.sh.getString("F_userimage", null);
        Log.e("User Profile image :", "" + STR_User_image);
        /*if (!STR_User_image.isEmpty()) {

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.getInstance().displayImage(STR_User_image, IVMainProfileImage, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    rl_profileprogressbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    rl_profileprogressbar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    rl_profileprogressbar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    rl_profileprogressbar.setVisibility(View.GONE);
                }
            });

        } else {
            rl_profileprogressbar.setVisibility(View.GONE);
            Log.e("No Image Found :", "OK");

        }*/


        horizontal_recycler_view = (RecyclerView) findViewById(R.id.horizontal_recycler_view);

        data = fill_with_data();


        horizontalAdapter = new HorizontalAdapter(data, getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        rl_Dr_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_My_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "My Product Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Wallet Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Get_Free_Product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Get Free Product Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_Dr_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Setting Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        rl_Dr_Call_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Call Us Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        rl_Dr_Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Help Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        rl_Dr_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                disconnectFromFacebook();
                Toast.makeText(getApplicationContext(), "Logout Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        TV_pickup_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "PickUp Location Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /*TV_Other_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Other Location Clicked", Toast.LENGTH_SHORT).show();
            }
        });*/

        map_IV_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        });

        IV_searching_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Search Location Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        IV_feverate_places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getApplicationContext(), "Feverate Location Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        rl_pickup_location_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent sendToPickupLOcationScreen = new Intent(getApplicationContext(),
                        Product_Pickup_Activity.class);
                startActivity(sendToPickupLOcationScreen);
            }
        });

        CVTransferLoc_to_Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent TransferLoc_to_Loc_Screen = new Intent(getApplicationContext(),
                        Transfer_location_location.class);
                startActivity(TransferLoc_to_Loc_Screen);
            }
        });

    }


    /*taxi types horizontal recyclerview (Start)*/
    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data(R.drawable.ic_taxi_one, "Taxi 1"));
        data.add(new Data(R.drawable.ic_taxi_two, "Taxi 2"));
        data.add(new Data(R.drawable.ic_taxi_three, "Taxi 3"));
        data.add(new Data(R.drawable.ic_taxi_four, "Taxi 4"));
        data.add(new Data(R.drawable.ic_taxi_one, "Taxi 1"));
        data.add(new Data(R.drawable.ic_taxi_two, "Taxi 2"));
        data.add(new Data(R.drawable.ic_taxi_three, "Taxi 3"));
        data.add(new Data(R.drawable.ic_taxi_four, "Taxi 4"));
        data.add(new Data(R.drawable.ic_taxi_one, "Taxi 1"));


        return data;
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<Data> horizontalList = Collections.emptyList();
        Context context;


        public HorizontalAdapter(List<Data> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView txtview;
            final RelativeLayout taxi_Bg;

            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageview);
                txtview = (TextView) view.findViewById(R.id.txtview);
                taxi_Bg = (RelativeLayout) view.findViewById(R.id.rl_bg_selected);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.taxi_recycler_items, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.imageView.setImageResource(horizontalList.get(position).imageId);
            holder.txtview.setText(horizontalList.get(position).txt);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    String list = horizontalList.get(position).txt.toString();
                    Toast.makeText(MainActivity.this, list, Toast.LENGTH_SHORT).show();
                }

            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Log.e(" Adapter Position :", "" + position);
                    Log.e(" Item Position :", "" + data.get(holder.getLayoutPosition()));
                    Log.e(" List Size :", "" + data.size());
                }
            });

        }


        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }
    /*taxi types horizontal recyclerview (End)*/


    /*location data (Start)*/
    @Override
    public void onLocationPermissionGranted() {
        showToast("Location permission granted");
    }

    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionDenied() {
        showToast("Location permission denied");
        locationData();
    }

    @Override
    public void onLocationReceived(Location location) {

//        showToast(location.getProvider() + "," + location.getLatitude() + "," + location.getLongitude());

        /*Toast.makeText(getApplicationContext(), "Latitude :" + "  " + location.getLatitude()
                + "Longitude " + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();*/


        Str_user_Latitude = Double.toString(location.getLatitude());
        Str_user_Longitude = Double.toString(location.getLongitude());

        latitude = Double.parseDouble(Str_user_Latitude);
        longitude = Double.parseDouble(Str_user_Longitude);

        Log.e("Lat :", "" + latitude + "\t" + "Long :" + "" + longitude);
        Log.e("Str_user_Latitude :", "" + Str_user_Latitude);
        Log.e("Str_user_Longitude :", "" + Str_user_Longitude);


        initMap();

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.e("address :", "" + address);
            Log.e("city :", "" + city);
            Log.e("state :", "" + state);
            Log.e("country :", "" + country);
            Log.e("postalCode :", "" + postalCode);
            Log.e("knownName :", "" + knownName);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationProviderEnabled() {
        showToast("Location services are now ON");

    }

    @Override
    public void onLocationProviderDisabled() {
        showToast("Location services are still Off");
        locationData();
    }


    /*progressbar data (Start)*/
    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(this)
                .colors(getResources().getIntArray(R.array.gplus_colors))
                /*.sweepSpeed(mSpeed)
                .rotationSpeed(mSpeed)
                .strokeWidth(dpToPx(mStrokeWidth))*/
                .style(CircularProgressDrawable.STYLE_ROUNDED);
       /* if (mCurrentInterpolator != null) {
            b.sweepInterpolator(mCurrentInterpolator);
        }*/
        mCircularGeocode_ProgressBar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularGeocode_ProgressBar.getWidth(),
                mCircularGeocode_ProgressBar.getHeight());
        mCircularGeocode_ProgressBar.setVisibility(View.INVISIBLE);
        mCircularGeocode_ProgressBar.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    /*get current lat lon of the user (Start)*/
    public void locationData() {
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();
        requestSingleLocationFix(easyLocationRequest);
    }
    /*get current lat lon of the user (Start)*/
    /*location data (End)*/


    public void initMap() {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                googleMap.setOnCameraIdleListener(MainActivity.this);
                googleMap.setOnCameraMoveStartedListener(MainActivity.this);
                googleMap.setOnCameraMoveListener(MainActivity.this);
                googleMap.setOnCameraMoveCanceledListener(MainActivity.this);




               /* mUiSettings = googleMap.getUiSettings();
                mUiSettings.setMyLocationButtonEnabled(true);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                googleMap.setMyLocationEnabled(true);
//                mUiSettings.setTiltGesturesEnabled(false);
//                mUiSettings.setRotateGesturesEnabled(false);
*/

               /*this is used for setting marker on map center  01 (Start)*/
                /*int height = 100;
                int width = 60;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_ic_marker);
                Bitmap b = bitmapdraw.getBitmap();
                final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);*/
               /*this is used for setting marker on map center  01 (End)*/


                Log.e("onMapReady Str_user_Latitude :", "" + Str_user_Latitude);
                Log.e("onMapReady Str_user_Longitude :", "" + Str_user_Longitude);

                Log.e("onMapReady double latitude :", "" + latitude);
                Log.e("onMapReady double longitude :", "" + longitude);

                LatLng latLng = new LatLng(latitude, longitude);
//        LatLng latLng = new LatLng(Str_user_Latitude, Str_user_Longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

               /*this is used for setting marker on map center  02 (Start)*/
               /* googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title("Ritesh"));*/
                /*this is used for setting marker on map center  02 (End)*/


                // Showing the current location in Google Map
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);







                /*method one to get lat long to address 01 (Start)*/
                /*geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    Log.e("lat long to address :", "Address" + address);
                    Log.e("lat long to city :", "city" + city);
                    Log.e("lat long to state :", "state" + state);
                    Log.e("lat long to country :", "country" + country);
                    Log.e("lat long to postalCode :", "postalCode" + postalCode);
                    Log.e("lat long to knownName :", "knownName" + knownName);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                /*method one to get lat long to address 01 (End)*/

                /*method two to get lat long to address 02 (Start)*/
                /*method two to get lat long to address 02 (Start)*/
                /*http://maps.googleapis.com/maps/api/geocode/json?latlng=22.7028148,75.8716865&sensor=true*/


                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        Log.e("onMarkerDragStart.... :", "" + marker.getPosition().latitude + "" + marker.getPosition().longitude);

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        Log.e("onMarkerDrag.... :", "" + marker.getPosition().latitude + "" + marker.getPosition().longitude);
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Log.e("System out", "onMarkerDrag...");
                        Log.e("onMarkerDragEnd.... :", "" + marker.getPosition().latitude + "" + marker.getPosition().longitude);
                    }
                });


                LatLng center = googleMap.getCameraPosition().target;


                VisibleRegion visibleRegion = googleMap.getProjection()
                        .getVisibleRegion();

                Point x = googleMap.getProjection().toScreenLocation(
                        visibleRegion.farRight);

                Point y = googleMap.getProjection().toScreenLocation(
                        visibleRegion.nearLeft);

                Point centerPoint = new Point(x.x / 2, y.y / 2);

                LatLng centerFromPoint = googleMap.getProjection().fromScreenLocation(
                        centerPoint);

                Log.e("MapFragment: ", "Center From camera: Long: " + center.longitude
                        + " Lat" + center.latitude);

                Log.e("Punto x", "x:" + x.x + "y:" + x.y);
                Log.e("Punto y", "y:" + y.x + "y:" + y.y);

                Log.e("MapFragment: ", "Center From Point: Long: "
                        + centerFromPoint.longitude + " Lat"
                        + centerFromPoint.latitude);


                centerFromPoint = googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();


                map_IV_mylocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng latLng = new LatLng(latitude, longitude);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18.0f);
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
                        googleMap.animateCamera(cameraUpdate);
                    }
                });


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        /*googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                .title("Ritesh"));*/
                        Toast.makeText(getApplicationContext(), "Map Latnlg" + latLng, Toast.LENGTH_SHORT).show();
                    }
                });


                /*googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {

                        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                            Toast.makeText(getApplicationContext(), "The user gestured on the map.",Toast.LENGTH_SHORT).show();
                        } else if (i == GoogleMap.OnCameraMoveStartedListener
                                .REASON_API_ANIMATION) {
                            Toast.makeText(getApplicationContext(), "The user tapped something on the map.",Toast.LENGTH_SHORT).show();
                        } else if (i == GoogleMap.OnCameraMoveStartedListener
                                .REASON_DEVELOPER_ANIMATION) {
                            Toast.makeText(getApplicationContext(), "The app moved the camera.",Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {

                        Toast.makeText(getApplicationContext(), "The camera is moving.",
                                Toast.LENGTH_SHORT).show();

                    }
                });

                googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
                    @Override
                    public void onCameraMoveCanceled() {
                        Toast.makeText(getApplicationContext(), "Camera movement canceled.",
                                Toast.LENGTH_SHORT).show();


                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {

                        Toast.makeText(getApplicationContext(), "The camera has stopped moving.",
                                Toast.LENGTH_SHORT).show();

                    }
                });
*/


            }
        });
    }


    /*check for google play service is available or not (Start)*/
    /*old depricated but working*/
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /*old depricated but working*/
    /*new*/
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 1).show();
            }

            return false;
        }

        return true;
    }
    /*new*/
    /*check for google play service is available or not (End)*/

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("lifecycle", "onDestroy invoked");
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {

            Log.e(" You are already loged out from facebook :", "ok");

            Appconstant.editor.remove("loginTest");
            Appconstant.editor.commit();
            Intent sendToLoginandRegistration = new Intent(getApplicationContext(),
                    SignInSignUpSelectActivity.class);
            startActivity(sendToLoginandRegistration);

            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.e("Successfully logout from facebook :", "Congratulations");
                AccessToken.setCurrentAccessToken(null);
                LoginManager.getInstance().logOut();
                Log.e("Successfully logout from facebook :", "Congrates");


                Appconstant.editor.remove("loginTest");
                Appconstant.editor.commit();
                Intent sendToLoginandRegistration = new Intent(getApplicationContext(),
                        SignInSignUpSelectActivity.class);
                startActivity(sendToLoginandRegistration);


            }
        }).executeAsync();
    }


    @Override
    public void onCameraMoveStarted(int i) {
        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            /*Toast.makeText(this, "The user gestured on the map.",
                    Toast.LENGTH_SHORT).show();*/
            Log.e("The user gestured on the map :", "");

//            setMargins(IV_marker_icon, 0, -50, 0, 0);  // (left, top, right, bottom)
//            setMargins(IV_marker_shadow, 50, 0, 0, 0);

        } else if (i == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            /*Toast.makeText(this, "The user tapped something on the map.",
                    Toast.LENGTH_SHORT).show();*/
            Log.e("The user tapped something on the map :", "");


        } else if (i == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            /*Toast.makeText(this, "The app moved the camera.",
                    Toast.LENGTH_SHORT).show();*/

            Log.e("The app moved the camera :", "");
        }
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    @Override
    public void onCameraMove() {
        /*Toast.makeText(this, "The camera is moving.",
                Toast.LENGTH_SHORT).show();*/
        Rl_geocode_progressview.setVisibility(View.VISIBLE);
        TV_Address_GeoCode.setText("Please Wait We Trying To Get Your New Pickup Location......");
        LatLng centerP = googleMap.getCameraPosition().target;
        Log.e("MapFragment: ", "Center From camera: Lat: " + centerP.latitude
                + " Long" + centerP.longitude);

        Rlatitude = centerP.latitude;
        Rlongitude = centerP.longitude;

        Log.e("The camera is moving :", "" + Rlatitude + "\t" + "" + Rlongitude);


    }


    @Override
    public void onCameraMoveCanceled() {
        /*Toast.makeText(this, "Camera movement canceled.",
                Toast.LENGTH_SHORT).show();*/
        Log.e("Camera movement canceled :", "");
    }

    @Override
    public void onCameraIdle() {
//        Toast.makeText(this, "The camera has stopped moving.",Toast.LENGTH_SHORT).show();
        Log.e("The onCameraIdle :", "" + Rlatitude + "\t" + "" + Rlongitude);
        Log.e("The onCameraIdle :", "" + Rlatitude + "\t" + "" + Rlongitude);
        Log.e("The onCameraIdle :", "" + Rlatitude + "\t" + "" + Rlongitude);


        getCompleteAddressString(Rlatitude, Rlongitude);

        /*GeoCodeAddresstask task = new GeoCodeAddresstask();
        task.execute();*/


    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                CompleteAddress = strReturnedAddress.toString();
                Log.e("My Current loction address", "" + strReturnedAddress.toString());
                TV_Address_GeoCode.setText(CompleteAddress);
                Rl_geocode_progressview.setVisibility(View.GONE);

            } else {
                Log.e("My Current loction address", "No Address returned!");
                Rl_geocode_progressview.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Rl_geocode_progressview.setVisibility(View.GONE);
            Log.e("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }


    private class GeoCodeAddresstask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);

            Log.e("******* NOW GeoCodeAddresstask WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW GeoCodeAddresstask WEB SERVICE IS RUNNING *******", "YES");

            Log.e("GeoCodeAddresstask onPreExecute :", "" + Rlatitude + "\t" + "" + Rlongitude);
            geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//            Rl_login_SignIn_progressview.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW GeoCodeAddresstask doInBackground TASK IS RUNNING *******", "YES");
            Log.e("******* NOW GeoCodeAddresstask doInBackground TASK IS RUNNING *******", "YES");

            try {
                addresses = geocoder.getFromLocation(Rlatitude, Rlongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null) {
                    result = "success";
                    Address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    City = addresses.get(0).getLocality();
                    State = addresses.get(0).getAdminArea();
                    Country = addresses.get(0).getCountryName();
                    PostalCode = addresses.get(0).getPostalCode();
                    KnownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                    Log.e("lat long to address :", "Address" + Address);
                    Log.e("lat long to city :", "city" + City);
                    Log.e("lat long to state :", "state" + State);
                    Log.e("lat long to country :", "country" + Country);
                    Log.e("lat long to postalCode :", "postalCode" + PostalCode);
                    Log.e("lat long to knownName :", "knownName" + KnownName);
                }

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
//            Rl_login_SignIn_progressview.setVisibility(View.GONE);
            Log.e("onPostExecute :", "OK");
            if (!iserror) {
                if (result.equalsIgnoreCase("success")) {
                    Log.e("lat long to address :", "Address" + Address);
                    Log.e("lat long to city :", "city" + City);
                    Log.e("lat long to state :", "state" + State);
                    Log.e("lat long to country :", "country" + Country);
                    Log.e("lat long to postalCode :", "postalCode" + PostalCode);
                    Log.e("lat long to knownName :", "knownName" + KnownName);


                    Toast.makeText(MainActivity.this, "GeoCode Detail: " + "\n"
                                    + "Address" + "" + Address + "\n"
                                    + "City" + "" + City + "\n"
                                    + "State" + "" + State + "\n"
                                    + "Country" + "" + Country + "\n"
                                    + "PostalCode" + "" + PostalCode + "\n"
                                    + "KnownName" + "" + KnownName
                            , Toast.LENGTH_LONG).show();

//                    TV_Address_GeoCode.setText(Address + " " + City + " " + State + " " + Country + " " + PostalCode + " " + KnownName);

                }
            }
        }

    }


}
