package com.carpediem.homer.perfectitemscontainer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by homer on 16-7-22.
 */
public class NormalDropDownView extends TextView implements DropDownView {

    public NormalDropDownView(Context context) {
        super(context);
    }

    public NormalDropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalDropDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {

    }

    @Override
    public DropDownStateListener getDropDownListener() {
        return null;
    }
}
