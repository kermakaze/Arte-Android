package com.teamarte.arte.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;


    private static final String USER_SIGNED_IN_KEY = "user_signed_in";
    private static final String USER_ID_KEY = "user_id";


    private static final String PREFERENCES_KEY = "ARTE_PREFERENCES";



    private static PrefManager mInstance;

    private PrefManager(Context context){

        this.mSharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        this.mEditor = mSharedPreferences.edit();
    }








    public static PrefManager getInstance(Context c) {
        if(mInstance ==  null)
            mInstance = new PrefManager(c);
        return mInstance;
    }
    public boolean isUserSignedIn() {
        return  mSharedPreferences.getBoolean(USER_SIGNED_IN_KEY, false);
    }

    public void setUserSignedIn(boolean signedIn) {
        mEditor.putBoolean(USER_SIGNED_IN_KEY, signedIn);
        mEditor.commit();
    }


    public String getUserId() {
        return   mSharedPreferences.getString(USER_ID_KEY,"");
    }


    public void  saveUserId( String userId) {
        mEditor.putString(USER_ID_KEY, userId);

        mEditor.commit();
        setUserSignedIn(true);
    }


    public void clearUserDetails(){
        mEditor.clear();
        setUserSignedIn(false);
    }




}
