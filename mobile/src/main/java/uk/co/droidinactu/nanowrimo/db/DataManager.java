package uk.co.droidinactu.nanowrimo.db;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import uk.co.droidinactu.nanowrimo.NaNoApplication;

/**
 * Created by aspela on 27/06/17.
 */

public class DataManager {
    private static final String LOG_TAG = DataManager.class.getSimpleName() + ":";

    public static int NANOWRIMO_MONTH_TARGET = 50000;

    public static final String MESSAGES_CHILD = "messages";
    public static final String EVENTS_CHILD = "events";
    public static final String WORDCOUNT_CHILD = "wordcounts/";

    public static final String ANONYMOUS = "anonymous";

    private FirebaseDatabase mFbDatabase = null;
    private DatabaseReference mFbDatabaseRefWordCount;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private Context context;

    public Map<String, DayWordCount> dayWrdCounts = new HashMap<>();

    public void createDemoData() {
        DayWordCount wrdCnt = new DayWordCount("2017-11-01_18:00", 1345);
        String wrdCntUuid = saveWordCount(wrdCnt);

        wrdCnt = new DayWordCount("2017-11-02_18:00", 1045);
        wrdCntUuid = saveWordCount(wrdCnt);
    }

    public DataManager(Context ctx) {
        this.context = ctx;
    }

    public void initialise() {
        FirebaseApp.initializeApp(context);
        mFbDatabase = FirebaseDatabase.getInstance();
        mFbDatabase.setPersistenceEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
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
        return mFirebaseUser;
    }


    public String saveWordCount(DayWordCount obj) {
        if (obj.getId() == null) {
            obj.setId(UUID.randomUUID().toString());
        }
        Task<Void> ret = mFbDatabaseRefWordCount.child(obj.getId()).setValue(obj);
        addWordCountToList(obj);
        return obj.getId();
    }

    public void addWordCountToList(DayWordCount obj) {
        dayWrdCounts.put(obj.getDate(), obj);
    }

    public Map<String, DayWordCount> getWordCounts(int year, int month) {
        Map<String, DayWordCount> list = new HashMap<>();
        for (String key : dayWrdCounts.keySet()) {
            if (key.contains("/" + month + "/" + year)) {
                list.put(key, dayWrdCounts.get(key));
            }
        }
        return list;
    }


    public void firebaseSignOut() {
        mFirebaseAuth.signOut();
        mFirebaseUser = null;
    }

    public String getFirebaseUserUid() {
        return mFirebaseUser.getUid();
    }


}
