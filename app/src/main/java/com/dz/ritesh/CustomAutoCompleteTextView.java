package com.dz.ritesh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

/**
 * Created by ritesh on 22/2/17.
 */

public class CustomAutoCompleteTextView extends AutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("description");
    }

}


