package com.dz.ritesh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 17/2/17.
 */

public class SignInSignUpSelectActivity extends AppCompatActivity {

    @BindView(R.id.cv_signup_page)
    CardView Cv_SignUp_Page;

    @BindView(R.id.iv_signin_page)
    TextView Tv_SignIn_Page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup_select);
        ButterKnife.bind(this);


        Tv_SignIn_Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent intentSignup = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intentSignup);
            }
        });


        Cv_SignUp_Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent intentSignup = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intentSignup);
            }
        });

    }
}
