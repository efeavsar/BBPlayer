package com.lonict.android.bbplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.deezer.sdk.model.Album;
import com.deezer.sdk.model.Permissions;
import com.deezer.sdk.network.connect.DeezerConnect;
import com.deezer.sdk.network.connect.SessionStore;
import com.deezer.sdk.network.connect.event.DialogListener;
import com.deezer.sdk.network.request.DeezerRequest;
import com.deezer.sdk.network.request.DeezerRequestFactory;
import com.deezer.sdk.network.request.event.JsonRequestListener;
import com.deezer.sdk.network.request.event.RequestListener;
import com.deezer.sdk.player.AlbumPlayer;
import com.deezer.sdk.player.networkcheck.NetworkStateCheckerFactory;
import com.lonict.android.bbplayer.ui.MusicPlayerActivity;

import java.util.List;

/**
 * Created by Efe Avsar on 26/03/2016.
 */
public class LonictDeezer {

    private final String mDeezerapplicaitonID;
    private final Context mContext;
    private final DeezerConnect mDeezerConnect;

    public LonictDeezer(Context context)
    {
        this.mContext = context;
        this.mDeezerapplicaitonID= context.getResources().getString(R.string.deezer_application_id);
        DeezerConnect deezerConnect = DeezerConnect.forApp(mDeezerapplicaitonID).
                withContext(mContext).
                build();
        this.mDeezerConnect = deezerConnect;
    }

    public void authorize()
    {
        SessionStore sessionStore = new SessionStore();
        if (sessionStore.restore(mDeezerConnect, mContext)) {
            // The restored session is valid, navigate to the Home Activity
            //Go ahead
            //Intent intent = new Intent(context, HomeActivity.class);
            //startActivity(intent);
            return;
        }

        // The set of Deezer Permissions needed by the app
        String[] permissions = new String[] { // v1
                Permissions.BASIC_ACCESS,
                Permissions.MANAGE_LIBRARY,
                Permissions.LISTENING_HISTORY };
        // The listener for authentication events
        DialogListener listener = new DialogListener() {
            public void onComplete(Bundle values) {
                SessionStore sessionStore = new SessionStore();
                sessionStore.save(mDeezerConnect, mContext);
            }
            public void onCancel() {}
            public void onException(Exception e) {}
        };
        // Launches the authentication process
        mDeezerConnect.authorize((MusicPlayerActivity)mContext, permissions, listener);
    }

    public void requestPlaylist()
    {
        // the request listener
        RequestListener listener = new JsonRequestListener() {
            public void onResult(Object result, Object requestId) {
                List<Album> albums = (List<Album>) result; // v1
        // do something with the albums
            }
            public void onUnparsedResult(String requestResponse, Object requestId) {} // v2
            public void onException(Exception e, Object requestId) {} // v3
        }
        long artistId = 11472;
        DeezerRequest request = DeezerRequestFactory.requestArtistAlbums(artistID);
        mDeezerConnect.requestAsync(request, listener); //
    }
}
