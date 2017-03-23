package com.dz.ritesh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ritesh on 20/2/17.
 */

public class Your_Product extends AppCompatActivity {


    @BindView(R.id.fl_banner_slid_yu)
    RelativeLayout rl_banner_yu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_product);
        ButterKnife.bind(this);




    }
}
