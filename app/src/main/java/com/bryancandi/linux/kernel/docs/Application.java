package com.bryancandi.linux.kernel.docs;

import com.google.android.material.color.DynamicColors;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // This is all you need for M3 dynamic color.
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
