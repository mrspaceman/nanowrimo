package uk.co.droidinactu.nanowrimo.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by aspela on 27/06/17.
 */

public class DataManager {
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String MESSAGES_CHILD = "messages";
    public static final String EVENTS_CHILD = "events";
    public static final String WORDCOUNT_CHILD = "wordcounts/";
    public static final String ANONYMOUS = "anonymous";
    private static final String LOG_TAG = DataManager.class.getSimpleName() + ":";
    public final String NANO_API_GET_WORDCOUNT = "http://nanowrimo.org/wordcount_api/wc/"; // followed by username
    public final String NANO_API_GET_WORDCOUNT_HIST = "http://nanowrimo.org/wordcount_api/wchistory/"; // followed by username
    public final String NANO_API_GET_WORDCOUNT_REGION = "http://nanowrimo.org/wordcount_api/wcregion/"; // followed by region
    public final String NANO_API_GET_WORDCOUNT_REGION_HIST = "http://nanowrimo.org/wordcount_api/wcregionhist/"; // followed by region
    public final String NANO_API_GET_WORDCOUNT_SITE = "http://nanowrimo.org/wordcount_api/wcstatsummary";
    public final String NANO_API_GET_WORDCOUNT_SITE_HIST = "http://nanowrimo.org/wordcount_api/wcstats";
    public Map<String, DayWordCount> dayWrdCounts = new HashMap<>();
    private FirebaseDatabase mFbDatabase = null;
    private DatabaseReference mFbDatabaseRefWordCount;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Context context;


    public DataManager(Context ctx) {
        this.context = ctx;
    }

    public void initialise() {
        if (mFbDatabase == null) {
            FirebaseApp.initializeApp(context);
            mFbDatabase = FirebaseDatabase.getInstance();
            mFbDatabase.setPersistenceEnabled(true);
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
        }
    }

    public void initialiseListeners(ChildEventListener wordcountMsgListener) {
        mFbDatabaseRefWordCount = mFbDatabase.getReference(WORDCOUNT_CHILD + getUsername().replace(" ", "_"));
        mFbDatabaseRefWordCount.keepSynced(true);
        mFbDatabaseRefWordCount.addChildEventListener(wordcountMsgListener);
    }

    public String getUsername() {
        if (mFirebaseUser == null) {
            return ANONYMOUS;
        }
        return mFirebaseUser.getDisplayName();
    }

    public String getPhotoUrl() {
        if (mFirebaseUser != null && mFirebaseUser.getPhotoUrl() != null) {
            return mFirebaseUser.getPhotoUrl().toString();
        }
        return "";
    }


    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        if (mFirebaseUser==null && mFirebaseAuth!=null){
            mFirebaseUser = mFirebaseAuth.getCurrentUser();}
        return mFirebaseUser;
    }


    public String saveWordCount(DayWordCount obj) {
        if (obj.getId() == null) {
            obj.setId(UUID.randomUUID().toString());
            sendWordcountToNaNoWriMo(obj);
        }
        Task<Void> ret = mFbDatabaseRefWordCount.child(obj.getId()).setValue(obj);
        addWordCountToList(obj);
        return obj.getId();
    }

    private void sendWordcountToNaNoWriMo(final DayWordCount obj) {

        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final String nanoName = sp.getString("pref_nano_username", "");
        final String nanoKey = sp.getString("pref_nano_user_secret_key", "");

        // create the hashable string
        String hashStr = nanoKey + nanoName + obj.getWordcount();

        // hash the string
        final String hashed = DigestUtils.shaHex(hashStr);

        // create and send the request
        String url = "http://nanowrimo.org/api/wordcount";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        String s = error.getMessage();
                        if (s == null) {
                            s = "Unknown Volley Error sending wordcount to NaNoWriMo website";
                        }
                        Log.d("Error.Response", s);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hash", hashed);
                params.put("name", nanoName);
                params.put("wordcount", "" + obj.getWordcount());

                return params;
            }
        };

        queue.add(putRequest);
    }

    public void addWordCountToList(DayWordCount obj) {
        dayWrdCounts.put(obj.getDate(), obj);
    }

    public Map<String, DayWordCount> getWordCounts(final int year, final int month) {
        Map<String, DayWordCount> list = new HashMap<>();
        for (String key : dayWrdCounts.keySet()) {
            if (key.contains("/" + month + "/" + year)) {
                list.put(key, dayWrdCounts.get(key));
            }
        }
        return list;
    }

    public int getWordCount(final int year, final int month) {
        int cumWordCount = 0;
        Map<String, DayWordCount> list = new HashMap<>();
        for (String key : dayWrdCounts.keySet()) {
            if (key.contains("/" + month + "/" + year)) {
                cumWordCount += dayWrdCounts.get(key).getWordcount();
            }
        }
        return cumWordCount;
    }

    public int getWordCount(final int year, final int month, final int day) {
        int cumWordCount = 0;
        Map<String, DayWordCount> list = new HashMap<>();
        for (String key : dayWrdCounts.keySet()) {
            if (key.contains("/" + month + "/" + year)) {
                DayWordCount dwc = dayWrdCounts.get(key);
                if (dwc.getDayNumber() <= day) {
                    cumWordCount += dwc.getWordcount();
                }
            }
        }
        return cumWordCount;
    }


    public void firebaseSignOut() {
        mFirebaseAuth.signOut();
        mFirebaseUser = null;
    }

    public String getFirebaseUserUid() {
        return mFirebaseUser.getUid();
    }


    public int getCurrentTotal(final int year, final int month) {
        int total = 0;
        for (String key : dayWrdCounts.keySet()) {
            if (key.contains("/" + month + "/" + year)) {
                total += dayWrdCounts.get(key).getWordcount();
            }
        }
        return total;
    }

}
