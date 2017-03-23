/*
 * Copyright (c) 2014-2015 Amberfog.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dz.ritesh.SignupPhoneField;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dz.ritesh.Constant.HttpUrlPath;
import com.dz.ritesh.Constant.Utils;
import com.dz.ritesh.R;
import com.dz.ritesh.SignInActivity;
import com.jaredrummler.materialspinner.MaterialSpinner;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import in.championswimmer.libsocialbuttons.fabs.FABFacebook;
import in.championswimmer.libsocialbuttons.fabs.FABInstagram;
import in.championswimmer.libsocialbuttons.fabs.FABTwitter;

@SuppressWarnings("deprecation")
public class VerifyPhoneFragment extends BaseFlagFragment {


    @BindView(R.id.edt_et_signup_user_first_name)
    EditText ET_UserFirstName;

    @BindView(R.id.edt_et_signup_user_last_name)
    EditText ET_UserLastName;

    @BindView(R.id.edt_et_signup_email)
    EditText ET_UserEmail;

    @BindView(R.id.phone)
    EditText ET_phone;

    @BindView(R.id.edt_et_sign_up_password)
    EditText ET_Password;

    @BindView(R.id.edt_et_sign_up_re_password)
    EditText ET_Confirm_Password;

    @BindView(R.id.cv_et_sign_up_btn_signup)
    CardView CV_btn_signup;

    @BindView(R.id.cv_signup_spineer_user_typee)
    CardView CV_signup_user_type;

    @BindView(R.id.tv_terms)
    TextView TV_terms_condition;

    @BindView(R.id.fb_signup)
    FABFacebook FaceBook_signup;

    @BindView(R.id.tt_signup)
    FABTwitter Twitter_signup;

    @BindView(R.id.ig_signup)
    FABInstagram Instagram_signup;


    boolean iserror = false;

    public VerifyPhoneFragment() {
    }


    @BindView(R.id.rl_signup_progress)
    RelativeLayout signupProgress;

    CircularProgressBar mCircularProgressBar;


    String str_user_first_name = "", str_user_last_name = "", str_emailid = "", str_password = "", str_confirm_password = "",
            str_phone_number = "", result = "", error = "", Str_User_Type = "", Str_User_Type_default = "", Str_User_TypeSelectedValue = "";

    String STR_User_ID = "", STR_Phone_Number = "", STR_User_Type = "", STR_User_First_Name = "", STR_User_Last_Name = "", STR_User_Email = "",
            STR_Password = "", STR_error_Message = "", STR_Result = "";

    @BindView(R.id.spinner_user_typee)
    MaterialSpinner spinner;
    ArrayList<String> USER_TYPE_LIST = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_signup, container, false);
        ButterKnife.bind(this, rootView);




         /*spinner click method and not clicked methos (Start)*/
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spinner.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Toast.makeText(getActivity(), "Position :" + "  " + position + "  Clicked " + "" + item,
                        Toast.LENGTH_SHORT).show();

                android.support.design.widget.Snackbar.make(view, "Clicked " + item,
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();
                Str_User_TypeSelectedValue = item;

            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Toast.makeText(getActivity(), "Please select User Type",
                        Toast.LENGTH_SHORT).show();

                android.support.design.widget.Snackbar.make(spinner, "Nothing selected",
                        android.support.design.widget.Snackbar.LENGTH_SHORT).show();


            }
        });
        /*spinner click method and not clicked methos (End)*/


        if (Utils.isConnected(getActivity())) {
            UserTypeListJsontask task = new UserTypeListJsontask();
            task.execute();
        } else {

            SnackbarManager.show(
                    Snackbar.with(getActivity())
                            .position(Snackbar.SnackbarPosition.TOP)
                            .margin(15, 15)
                            .backgroundDrawable(R.drawable.snackbar_custom_layout)
                            .text("Please Your Internet Connectivity..!!"));

        }


        /*circular progress bar (Start)*/
        mCircularProgressBar = (CircularProgressBar) rootView.findViewById(R.id.signup_progressbar_circular);
//        signupProgress.setVisibility(View.GONE);
        ((CircularProgressDrawable) mCircularProgressBar.getIndeterminateDrawable()).start();
        updateValues();
        /*circular progress bar (End)*/

        /*spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                android.support.design.widget.Snackbar.make(view, "Clicked " + item, android.support.design.widget.Snackbar.LENGTH_LONG).show();
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                android.support.design.widget.Snackbar.make(spinner, "Nothing selected", android.support.design.widget.Snackbar.LENGTH_LONG).show();
            }
        });*/


        CV_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_user_first_name = ET_UserFirstName.getText().toString().trim();
                str_user_last_name = ET_UserLastName.getText().toString().trim();
                str_emailid = ET_UserEmail.getText().toString().trim();
                str_password = ET_Password.getText().toString().trim();
                str_confirm_password = ET_Confirm_Password.getText().toString().trim();
                str_phone_number = ET_phone.getText().toString();
                str_phone_number = str_phone_number.replace(" ", "");

                Log.e(" Sign Up Fields data :", "\n"
                        + "Str_User_TypeSelectedValue :" + "" + Str_User_TypeSelectedValue + "\n"
                        + "str_user_first_name :" + "" + str_user_first_name + "\n"
                        + "str_user_last_name :" + "" + str_user_last_name + "\n"
                        + "str_emailid :" + "" + str_emailid + "\n"
                        + "str_phone_number :" + "" + str_phone_number + "\n"
                        + "str_password :" + "" + str_password + "\n"
                        + "str_confirm_password :" + "" + str_confirm_password);


                if (spinner.getSelectedIndex() == 0) {
                    Log.e("error", "ok");
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(CV_signup_user_type);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .text("Please Select User Type"));

                } else if (str_user_first_name.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                /*https://github.com/daimajia/AndroidViewAnimations/blob/master/README.md*/

                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_UserFirstName);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter your Name",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Please enter your First Name"));


                } else if (str_emailid.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_UserEmail);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Please enter your Email Id"));

                } else if (!isValidEmail(str_emailid)) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    //	emailedit.requestFocus();
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(ET_UserEmail);
                    /**************** End Animation ****************/
                    /*Toast.makeText(getApplicationContext(),
                            "Please enter valid email address.", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Please enter valid email address."));


                } else if (str_phone_number.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_phone);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Email Id", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Please enter your Phone Number"));

                } else if (str_password.equals("")) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Tada)
                            .duration(700)
                            .playOn(ET_Password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "Please enter your Password", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Please enter your Password"));

                } else if (str_password.length() < 5) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .playOn(ET_Password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(), "Please enter more than 5 character in password.",
                            Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Please enter more than 5 character \n in password."));


                } else if (!str_confirm_password.equals(str_password)) {
                    iserror = true;
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /**************** Start Animation ****************/
                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(ET_Password);

                    YoYo.with(Techniques.Swing)
                            .duration(700)
                            .playOn(ET_Confirm_Password);
                    /**************** End Animation ****************/

                    /*Toast.makeText(getApplicationContext(),
                            "oopsss....\n Password not Match Please try again", Toast.LENGTH_SHORT).show();*/


                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("oopsss....\\n Password not Match Please try again"));

                } else if (!iserror) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    /*Toast.makeText(getApplicationContext(),
                            "Good", Toast.LENGTH_SHORT).show();*/

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("Good All Value Correct"));

                    if (Utils.isConnected(getActivity())) {
                        SignUpJsontask task = new SignUpJsontask();
                        task.execute();
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(getActivity())
                                        .position(Snackbar.SnackbarPosition.TOP)
                                        .margin(15, 15)
                                        .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                        .textColor(R.color.black_clr)
                                        .text("Please Your Internet Connectivity..!!"));

                    }

                }


            }
        });


        FaceBook_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "FaceBook Signup ", Toast.LENGTH_SHORT).show();
            }
        });

        Twitter_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Twitter Signup ", Toast.LENGTH_SHORT).show();
            }
        });

        Instagram_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Instagram Signup ", Toast.LENGTH_SHORT).show();
            }
        });

        TV_terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Terms & condition ", Toast.LENGTH_SHORT).show();
            }
        });


        initUI(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCodes(getActivity());
    }

    @Override
    protected void send() {
        hideKeyboard(mPhoneEdit);
        mPhoneEdit.setError(null);
        String phone = validate();
        if (phone == null) {
            mPhoneEdit.requestFocus();
            mPhoneEdit.setError(getString(R.string.label_error_incorrect_phone));
            return;
        }
//        Toast.makeText(getActivity(), "send to " + phone, Toast.LENGTH_SHORT).show();
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


    /*progressbar data (Start)*/
    private void updateValues() {
        CircularProgressDrawable circularProgressDrawable;
        CircularProgressDrawable.Builder b = new CircularProgressDrawable.Builder(getActivity())
                .colors(getResources().getIntArray(R.array.gplus_colors))
                /*.sweepSpeed(mSpeed)
                .rotationSpeed(mSpeed)
                .strokeWidth(dpToPx(mStrokeWidth))*/
                .style(CircularProgressDrawable.STYLE_ROUNDED);
       /* if (mCurrentInterpolator != null) {
            b.sweepInterpolator(mCurrentInterpolator);
        }*/
        mCircularProgressBar.setIndeterminateDrawable(circularProgressDrawable = b.build());

        // /!\ Terrible hack, do not do this at home!
        circularProgressDrawable.setBounds(0,
                0,
                mCircularProgressBar.getWidth(),
                mCircularProgressBar.getHeight());
        mCircularProgressBar.setVisibility(View.INVISIBLE);
        mCircularProgressBar.setVisibility(View.VISIBLE);
    }
    /*progressbar data (End)*/


    public class UserTypeListJsontask extends AsyncTask<String, Void, ArrayList<String>> {

        boolean iserror = false;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //  loginprogressbar.setVisibility(View.VISIBLE);
            Log.e("******* UserTypeListJsontask IS RUNNING *******", "YES");
            Log.e("******* UserTypeListJsontask IS RUNNING *******", "YES");
            signupProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");
            Log.e("******* NOW BACKGROUND TASK IS RUNNING *******", "YES");

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://technorizen.co.in/WORKSPACE1/DZApp/webservice/all_type");

            try {
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("doinBackgrouns Main List Responce :", "" + object);

                USER_TYPE_LIST.add("Register As");
                JSONArray jsonArray = new JSONArray(object);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Str_User_Type = jsonObject.getString("type");
                    USER_TYPE_LIST.add(Str_User_Type);
                    Log.e(" doinBackgrouns Str_User_Type   name   :", "" + Str_User_Type);
                    Log.e(" doinBackgrouns Str_User_Type   name   :", "" + Str_User_Type);
                    Log.e(" doinBackgrouns Str_User_Type   name   :", "" + Str_User_Type);
                }

                return USER_TYPE_LIST;

            } catch (Exception e) {
                Log.v("22", "22" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);
            signupProgress.setVisibility(View.GONE);
            if (!iserror) {

                if (result1 == null) {
                    Log.e("result1 :", "Null");
                } else if (result1.isEmpty()) {

                    Log.e("result1 :", "empty");
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_dropdown_item, result1);
                    spinner.setItems(result1);
                    Str_User_Type_default = result1.get(0);
                    Log.e("Str_User_Type_default ", "" + Str_User_Type_default);

                    Log.e(" hair list result :", "" + result1.size());
                    Log.e(" hair list result :", "" + result1
                            + "\n" + "item 0 :" + "" + result1.get(0)
                            + "\n" + "item 1 :" + "" + result1.get(1));
                }

            }

        }

    }


    public class SignUpJsontask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            signupProgress.setVisibility(View.VISIBLE);


            Log.e(" Sign Up Fields data in Signup JSON TASK :", "\n"
                    + "str_user_first_name :" + "" + str_user_first_name + "\n"
                    + "str_user_last_name :" + "" + str_user_last_name + "\n"
                    + "str_emailid :" + "" + str_emailid + "\n"
                    + "str_phone_number :" + "" + str_phone_number + "\n"
                    + "str_password :" + "" + str_password + "\n"
                    + "str_confirm_password :" + "" + str_confirm_password);

        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(HttpUrlPath.UrlPath + "signup?");
            Log.e("SignUp Parameter \n\n:", HttpUrlPath.urlPathSocial + "signup?"
                    + "email =" + str_emailid
                    + "&first_name=" + str_user_first_name
                    + "&last_name=" + str_user_last_name
                    + "&password=" + str_password
                    + "&type=" + Str_User_TypeSelectedValue
                    + "&mobile_no = " + str_phone_number);


            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        7);
                nameValuePairs.add(new BasicNameValuePair("first_name",
                        str_user_first_name));
                nameValuePairs.add(new BasicNameValuePair("last_name",
                        str_user_last_name));
                nameValuePairs.add(new BasicNameValuePair("email",
                        str_emailid));
                nameValuePairs.add(new BasicNameValuePair("mobile_no",
                        str_phone_number));
                nameValuePairs.add(new BasicNameValuePair("password",
                        str_password));
                nameValuePairs.add(new BasicNameValuePair("type",
                        Str_User_TypeSelectedValue));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
                String object = EntityUtils.toString(response.getEntity());
                Log.e("************ object holds our data ************ :", "" + object);
                //JSONArray js = new JSONArray(object);


                JSONObject jobect = new JSONObject(object);
                result = jobect.getString("result");
                if (result.equalsIgnoreCase("successful")) {
                    STR_User_ID = jobect.getString("id");
                    STR_User_First_Name = jobect.getString("first_name");
                    STR_User_Last_Name = jobect.getString("last_name");
                    STR_User_Email = jobect.getString("email");
                    STR_Phone_Number = jobect.getString("mobile_no");
                    STR_User_Type = jobect.getString("type");

                } else {
                    if (result.equalsIgnoreCase("email already exist")) {
                        STR_error_Message = jobect.getString("result");


                    }
                }
            } catch (Exception e) {
                Log.e(" ******** Exception **********", "************ Exception ************" + e.getMessage());
                e.printStackTrace();
                iserror = true;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result1) {
            // TODO Auto-generated method stub
            super.onPostExecute(result1);

            //if (!iserror == false)
            if (!iserror) {
                if (result.equalsIgnoreCase("successful")) {
                    signupProgress.setVisibility(View.GONE);

                    Log.e("result :", "" + result);
                    Log.e("STR_userID :", "" + STR_User_ID);
                    Log.e("STR_User_First_Name :", "" + STR_User_First_Name);
                    Log.e("STR_User_Last_Name :", "" + STR_User_Last_Name);
                    Log.e("STR_user_email :", "" + STR_User_Email);
                    Log.e("STR_Phone_Number :", "" + STR_Phone_Number);
                    Log.e("STR_Password :", "" + STR_Password);
                    Log.e("STR_User_Type :", "" + STR_User_Type);

                    Toast.makeText(getActivity(), "Register Success", Toast.LENGTH_SHORT).show();



                    Intent GoLoginScreen = new Intent(getActivity(), SignInActivity.class);
                    startActivity(GoLoginScreen);

                } else if (result.equalsIgnoreCase("email already exist")) {
                    signupProgress.setVisibility(View.GONE);

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text(STR_error_Message));


                } else {
                    signupProgress.setVisibility(View.GONE);

                    SnackbarManager.show(
                            Snackbar.with(getActivity())
                                    .position(Snackbar.SnackbarPosition.TOP)
                                    .margin(15, 15)
                                    .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                    .textColor(R.color.black_clr)
                                    .text("oopsss....\\n Server Error"));
                }
            } else {
                signupProgress.setVisibility(View.GONE);
                SnackbarManager.show(
                        Snackbar.with(getActivity())
                                .position(Snackbar.SnackbarPosition.TOP)
                                .margin(15, 15)
                                .backgroundDrawable(R.drawable.snackbar_custom_layout)
                                .textColor(R.color.black_clr)
                                .text("oopsss....\\n Oops!! Please check server connection"));
//
            }
        }

    }


}
