package com.dz.ritesh;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dz.ritesh.Constant.Appconstant;
import com.dz.ritesh.Constant.HttpUrlPath;
import com.dz.ritesh.Constant.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.LocationRequest;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * Created by ritesh on 17/2/17.
 */
@SuppressWarnings("deprecation")
public class SignInActivity extends EasyLocationAppCompatActivity {

    @BindView(R.id.toolbar_signin)
    Toolbar toolbar_signin;

    @BindView(R.id.cv_btn_login_facebook)
    CardView CV_btn_fb;

    @BindView(R.id.cv_et_sign_btn_signup)
    CardView CV_btn_signin;

    /*@BindView(R.id.edt_login_password)
    EditText login_password;*/

    @BindView(R.id.rl_Signin_progress)
    RelativeLayout Rl_login_SignIn_progressview;

    CircularProgressBar mCircularSignin_ProgressBarlogin;


    @BindView(R.id.edt_login_user_email)
    EditText EDT_Login_user_email;

    @BindView(R.id.edt_login_password)
    EditText EDT_Login_user_password;

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    String Str_Fb_UserEmail = "", Str_Fb_ID = "", Str_Fb_Name = "", Str_Fb_Gender = "",
            Str_Fb_Profile_url = "", Str_Fb_Tocken = "", result = "",
            STR_User_Socail_Server_Id = "", STR_User_Socail_Email_ID = "", STR_User_Socail_User_first_Name = "",
            STR_User_Socail_User_Name = "", STR_User_Socail_User_last_Name = "", STR_User_Socail_User_image = "", Str_Lat = "", Str_Long = "";

    //To store longitude and latitude from map
    private double latitude;
    private double longitude;

    boolean iserror = false;
    String Str_login_email = "", Str_login_password = "", error = "";
    String STR_User_Server_Id = "", STR_Email_ID = "", STR_User_First_Name = "", STR_User_Last_Name = "", STR_Phone_Number = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
//                setFacebookData(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);

//        keyhash();
        loginButton = (LoginButton) findViewById(R.id.login_button);
         /*circular progress bar (Start)*/
        mCircularSignin_ProgressBarlogin = (CircularProgressBar) findViewById(R.id.login_Signin_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularSignin_ProgressBarlogin.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        Appconstant.sh = getSharedPreferences(Appconstant.MyPREFERENCES, Context.MODE_PRIVATE);

        /*get current lat-long (Start)*/
//        locationData();
        /*get current lat-long (End)*/

        CV_btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("facebook longin success :",
                        "User ID: " + "" + loginResult.getAccessToken().getUserId()
                                + "\n" + "Auth Token: " + "" + loginResult.getAccessToken().getToken());

                Str_Fb_Tocken = loginResult.getAccessToken().getToken();
                Str_Fb_ID = loginResult.getAccessToken().getUserId();
                Log.e(" Str_Fb_Tocken :", "" + Str_Fb_Tocken);
                Log.e(" Str_Fb_ID :", "" + Str_Fb_ID);


                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("LoginActivity", response.toString());
                                try {
                                    Log.e("Try First Block :", "OKK");

                                    Str_Fb_ID = object.getString("id");
                                    Log.e("Str_Fb_ID :", "" + Str_Fb_ID);

                                    try {
                                        URL profile_pic = new URL("http://graph.facebook.com/" + Str_Fb_ID + "/picture?type=large");
                                        Str_Fb_Profile_url = String.valueOf(profile_pic);
                                        Log.e(" Str_Fb_Profile_url :", "" + Str_Fb_Profile_url);
                                        Str_Fb_Name = object.getString("name");
                                        Log.e("Str_Fb_Name :", "" + Str_Fb_Name);
                                        Str_Fb_Gender = object.getString("gender");
                                        Log.e("Str_Fb_Gender :", "" + Str_Fb_Gender);
                                        Str_Fb_UserEmail = object.getString("email");
                                        Log.e("Str_Fb_UserEmail :", "" + Str_Fb_UserEmail);
                                        Log.e("profile_pic :", "" + profile_pic);

                                        /*LoginToDashBoardActivity task = new LoginToDashBoardActivity();
                                        task.execute();*/
                                        if (!Str_Fb_ID.equals("")) {
                                            /*Intent intentFbLogin = new Intent(getApplicationContext(), MainActivity.class);
                                            intentFbLogin.putExtra("Fb_ID", Str_Fb_ID);
                                            intentFbLogin.putExtra("Fb_Name", Str_Fb_Name);
                                            intentFbLogin.putExtra("Fb_Email", Str_Fb_UserEmail);
                                            intentFbLogin.putExtra("Fb_Profile_Image", Str_Fb_Profile_url);
                                            startActivity(intentFbLogin);*/

//                                            disconnectFromFacebook();
                                            Log.e("Str_Fb_ID :", "Not Null " + "Go to DashBoard Screen");
                                            if (Utils.isConnected(getApplicationContext())) {
                                                SocialLogintask task = new SocialLogintask();
                                                task.execute();

                                            } else {

                                                SnackbarManager.show(
                                                        Snackbar.with(SignInActivity.this)
                                                                .position(Snackbar.SnackbarPosition.TOP)
                                                                .margin(15, 15)
                                                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                                                .text("Please Your Internet Connectivity..!!"));

                                            }


                                        } else {
                                            Log.e("Str_Fb_ID :", "Null");
                                        }

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.e("Facebook login Cancel :", "Yes");
                Log.e("Facebook login Cancel :", "Yes");
                Log.e("Facebook login Cancel :", "Yes");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Facebook login Error :", "Yes");
                Log.e("Facebook login Error :", "Yes");
                Log.e("Facebook login Error :", "Yes");

                Log.e("LoginActivity Error :", "" + error);
            }

        });


        /*EDT_Login_user_password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Log.e("this done :", "YES");
                    handled = true;
                }
                Log.e(" Edittext id:", "" + actionId);
                return handled;
            }
        });*/


        EDT_Login_user_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.e("this done :", "YES");

                }
                Log.e(" Edittext id:", "" + actionId);
                CV_btn_signin.performClick();
                printLog();
                return true;
            }
        });




        CV_btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Str_login_email = EDT_Login_user_email.getText().toString().trim();
                Str_login_password = EDT_Login_user_password.getText().toString().trim();

                Log.e(" Login Fields data :", "\n"
                        + "Str_login_email :" + "" + Str_login_email + "\n"
                        + "Str_login_password :" + "" + Str_login_password + "\n");




                if (Str_login_email.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//                    v.playSoundEffect(SoundEffectConstants.CLICK);
                    /**************** Start Animation **************  **/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(EDT_Login_user_email);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignInActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Email Id"));

                } else if (!isValidEmail(Str_login_email)) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(EDT_Login_user_email);
                    /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignInActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter valid email address."));


                } else if (Str_login_password.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(EDT_Login_user_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignInActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please enter your Password"));

                } else if (Str_login_password.length() < 5) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(EDT_Login_user_password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignInActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Wrong password."));


                } else if (!iserror) {

                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(SignInActivity.this)
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Good All Value Correct"));

//                    v.playSoundEffect(android.view.SoundEffectConstants.CLICK);


                    if (Utils.isConnected(getApplicationContext())) {
                        LoginJsontask task = new LoginJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(SignInActivity.this)
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .text("Please Your Internet Connectivity..!!"));

                    }

                }





            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void printLog() {
        Log.e("Edit Text Password to login method this done :", "YES");
    }


    /*call this method for programatically logout from facebook (Start)*/
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {

            Log.e(" You are already loged out from facebook :", "ok");
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

            }
        }).executeAsync();
    }
    /*call this method for programatically logout from facebook (End)*/


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
        mCircularSignin_ProgressBarlogin.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularSignin_ProgressBarlogin.getWidth(),
                mCircularSignin_ProgressBarlogin.getHeight());
        mCircularSignin_ProgressBarlogin.setVisibility(View.INVISIBLE);
        mCircularSignin_ProgressBarlogin.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    public void keyhash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.dz.ritesh", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key :", "" + something);
                System.out.println("hash key === " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }




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




    @Override
    public void onLocationPermissionGranted() {
        showToast("Location permission granted");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationPermissionDenied() {
        showToast("Location permission denied");
        locationData();
    }

    @Override
    public void onLocationReceived(Location location) {
        showToast(location.getProvider() + "," + location.getLatitude() + "," + location.getLongitude());

        Toast.makeText(getApplicationContext(), "Latitude :" + "  " + location.getLatitude()
                + "Longitude " + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();


        Str_Lat = Double.toString(location.getLatitude());
        Str_Long = Double.toString(location.getLongitude());

        latitude = Double.parseDouble(Str_Lat);
        longitude = Double.parseDouble(Str_Long);

        Log.e("Lat :", "" + latitude + "\t" + "Long :" + "" + longitude);
        Log.e("Str_user_Latitude :", "" + Str_Lat);
        Log.e("Str_user_Longitude :", "" + Str_Long);

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


    public class SocialLogintask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");

            Log.e("Facebook Profile image :", "" + Str_Fb_Profile_url);
            Log.e("Facebook UserEmail :", "" + Str_Fb_UserEmail);
            Log.e("Facebook Profile ID :", "" + Str_Fb_ID);
            Log.e("Facebook Profile Name :", "" + Str_Fb_Name);

            Rl_login_SignIn_progressview.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPathSocial + "facebook?");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("email", Str_Fb_UserEmail));
                nameValuePairs.add(new BasicNameValuePair("face_id", Str_Fb_ID));
                nameValuePairs.add(new BasicNameValuePair("image", Str_Fb_Profile_url));
                nameValuePairs.add(new BasicNameValuePair("user_name", Str_Fb_Name));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******Socail object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successful")) {
                    STR_User_Socail_Server_Id = jobect.getString("id");
                    STR_User_Socail_Email_ID = jobect.getString("email");
                    STR_User_Socail_User_Name = jobect.getString("user_name");
                    STR_User_Socail_User_first_Name = jobect.getString("first_name");
                    STR_User_Socail_User_last_Name = jobect.getString("last_name");
                    STR_User_Socail_User_image = jobect.getString("image");

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
            Rl_login_SignIn_progressview.setVisibility(View.GONE);

            if (!iserror) {
                if (result.equalsIgnoreCase("successful")) {

                    Log.e("Socail_User_Name :" , "" + STR_User_Socail_User_Name);
                    Log.e("Socail_Server_Id :" , "" + STR_User_Socail_Server_Id);
                    Log.e("Socail_User_image :" , "" + STR_User_Socail_User_image);
                    Log.e("Socail_Email_Id :" , "" + STR_User_Socail_Email_ID);

                    Appconstant.editor.putString("F_id", STR_User_Socail_Server_Id);
                    Appconstant.editor.putString("F_email", STR_User_Socail_Email_ID);
                    Appconstant.editor.putString("F_username", STR_User_Socail_User_Name);
                    Appconstant.editor.putString("F_userimage", STR_User_Socail_User_image);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.commit();


                    Toast.makeText(SignInActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();

                    Intent intentFbLogin = new Intent(getApplicationContext(), MainActivity.class);
                    intentFbLogin.putExtra("EXIT", "0");
                    intentFbLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentFbLogin);
                    finish();

                    /*Intent in = new Intent(LoginSelectActivity.this, MainActivity.class);
                    in.putExtra("EXIT", "0");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginSelectActivity.this.startActivity(in);
                    finish();*/
                }
            } else if (result.equalsIgnoreCase("successful")) {
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");

                Toast.makeText(getApplicationContext(), "" + result,Toast.LENGTH_SHORT).show();

            }
        }

    }


    /*email address field validator (Start)*/
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }
    /*email address field validator (End)*/




    public class LoginJsontask extends AsyncTask<String, Void, String> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Log.e("******* NOW Login WEB SERVICE IS RUNNING *******", "YES");
            Rl_login_SignIn_progressview.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... params) {
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");
            Log.e("******* NOW Login TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.urlPathSocial + "login?");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
                nameValuePairs.add(new BasicNameValuePair("email", Str_login_email));
                nameValuePairs.add(new BasicNameValuePair("password", Str_login_password));

                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("*******object******** :", "" + object);

                //JSONArray js = new JSONArray(object);
                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successful")) {
                    STR_User_Server_Id = jobect.getString("id");
                    STR_Email_ID = jobect.getString("email");
                    STR_User_First_Name = jobect.getString("first_name");
                    STR_User_Last_Name = jobect.getString("last_name");
                    STR_Phone_Number = jobect.getString("mobile_no");

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
            Rl_login_SignIn_progressview.setVisibility(View.GONE);

            if (!iserror) {
                if (result.equalsIgnoreCase("successful")) {

                    Appconstant.editor.putString("id", STR_User_Server_Id);
                    Appconstant.editor.putString("email", STR_Email_ID);
                    Appconstant.editor.putString("first_name", STR_User_First_Name);
                    Appconstant.editor.putString("last_name", STR_User_Last_Name);
                    Appconstant.editor.putString("mobile_no", STR_Phone_Number);
                    Appconstant.editor.putString("loginTest", "true");
                    Appconstant.editor.commit();


                    Toast.makeText(SignInActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(SignInActivity.this, MainActivity.class);
                    in.putExtra("EXIT", "0");
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    SignInActivity.this.startActivity(in);
                    finish();
                } else if (result.equalsIgnoreCase("user not found")) {

                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(EDT_Login_user_email);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(EDT_Login_user_password);
                    /**************** End Animation ****************/

                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
                    Log.e("********* Login Detail SEND *********", "NOT Success USERNAME PASSWORD ERROR");
//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).stop();

//                    ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).progressiveStop();

                    Toast.makeText(getApplicationContext(), "Username or Password is wrong",
                            Toast.LENGTH_SHORT).show();
//									// TODO Auto-generated method stub
                }
            } else {
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");
                Log.e("********* Login Detail SEND *********", "NOT Success");

                Toast.makeText(getApplicationContext(), "Oops!! Please check server connection .",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }





  /*  @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
//                        disconnectFromFacebook();
                        SignInActivity.this.finish();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }*/


}
