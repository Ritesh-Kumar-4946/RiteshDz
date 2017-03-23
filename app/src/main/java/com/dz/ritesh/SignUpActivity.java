package com.dz.ritesh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dz.ritesh.SignupPhoneField.VerifyPhoneFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

/**
 * Created by ritesh on 20/2/17.
 */

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_flags);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new VerifyPhoneFragment())
                    .commit();
        }


        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
        /*signup data is on VeryfyPhoneFragment.class*/
    }

}
