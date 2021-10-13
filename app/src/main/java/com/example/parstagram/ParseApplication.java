package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register Parse models
        ParseObject.registerSubclass(Post.class);
        // Initialize Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("") // TODO: Add applicationID found parse dashboard
                .clientKey("") // TODO: Add client key
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
