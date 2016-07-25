package com.carpediem.homer.perfectitemscontainer;

/**
 * Created by homer on 16-7-22.
 */
public interface DropDownView {
    int PULL_DOWN = 1;
    int PENDING_REFRESH = 2;
    int REFRESH = 3;
    int DONE = 4;
    DropDownStateListener getDropDownListener();
    interface DropDownStateListener {
        void onStateChanged(int state);
    }
}
