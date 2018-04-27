/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.fast.framework.view.videolist.widget;

import java.io.IOException;

import com.fast.framework.view.videolist.visibility.utils.Config;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

/**
 * This is player implementation based on {@link TextureView}
 * It encapsulates {@link MediaPlayer}.
 */
public class TextureVideoView extends ScalableTextureView
        implements TextureView.SurfaceTextureListener, Handler.Callback, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = "TextureVideoView";
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    private static final int MSG_START = 0x0001;
    private static final int MSG_PAUSE = 0x0004;
    private static final int MSG_STOP = 0x0006;

    private Uri mUri;
    private Context mContext;
    private Surface mSurface;
    private MediaPlayer mMediaPlayer;

    private MediaPlayerCallback mMediaPlayerCallback;
    private Handler mHandler;
    private Handler mVideoHandler;

    private static final HandlerThread sThread = new HandlerThread("VideoPlayThread");

    static {
        sThread.start();
    }

    public interface MediaPlayerCallback {
        void onPrepared(MediaPlayer mp);

        void onCompletion(MediaPlayer mp);

        void onBufferingUpdate(MediaPlayer mp, int percent);

        void onVideoSizeChanged(MediaPlayer mp, int width, int height);

        boolean onInfo(MediaPlayer mp, int what, int extra);

        boolean onError(MediaPlayer mp, int what, int extra);
    }

    public TextureVideoView(Context context) {
        super(context);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMediaPlayerCallback(MediaPlayerCallback mediaPlayerCallback) {
        mMediaPlayerCallback = mediaPlayerCallback;
    }

    @Override
    public boolean handleMessage(Message msg) {
        synchronized (TextureVideoView.class) {
            switch (msg.what) {

                case MSG_START:
                    if (SHOW_LOGS) {
                        Log.i(TAG, "<< handleMessage init");
                    }
                    openVideo();
                    if (SHOW_LOGS) {
                        Log.i(TAG, ">> handleMessage init");
                    }
                    break;

                case MSG_PAUSE:
                    if (SHOW_LOGS) {
                        Log.i(TAG, "<< handleMessage pause");
                    }
                    if (mMediaPlayer != null) {
                        mMediaPlayer.pause();
                    }
                    mCurrentState = STATE_PAUSED;
                    if (SHOW_LOGS) {
                        Log.i(TAG, ">> handleMessage pause");
                    }
                    break;

                case MSG_STOP:
                    if (SHOW_LOGS) {
                        Log.i(TAG, "<< handleMessage stop");
                    }
                    release(true);
                    if (SHOW_LOGS) {
                        Log.i(TAG, ">> handleMessage stop");
                    }
                    break;

                default:
                    break;

            }
        }
        return true;
    }

    private void init() {
        mContext = getContext();
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;
        mHandler = new Handler();
        mVideoHandler = new Handler(sThread.getLooper(), this);
        setSurfaceTextureListener(this);
    }

    // release the media player in any state
    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            //            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            //            am.abandonAudioFocus(null);
        }
    }

    private void openVideo() {
        if (mUri == null || mSurface == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnInfoListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setDataSource(mContext, mUri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
            mTargetState = STATE_PREPARING;
        } catch (IOException ex) {
            if (SHOW_LOGS) {
                Log.w(TAG, "Unable to open content: " + mUri, ex);
            }
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            if (mMediaPlayerCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMediaPlayerCallback.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
                    }
                });
            }
        } catch (IllegalArgumentException ex) {
            if (SHOW_LOGS) {
                Log.w(TAG, "Unable to open content: " + mUri, ex);
            }
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            if (mMediaPlayerCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMediaPlayerCallback.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
                    }
                });
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        if (mTargetState == STATE_PLAYING) {
            if (SHOW_LOGS) {
                Log.i(TAG, "onSurfaceTextureAvailable start");
            }
            start();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        stop();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        if (SHOW_LOGS) {
            Log.i(TAG, "setVideoURI " + uri.toString());
        }
        mUri = uri;
    }

    public void start() {
        mTargetState = STATE_PLAYING;

        if (isInPlaybackState()) {
            mVideoHandler.obtainMessage(MSG_STOP).sendToTarget();
        }

        if (mUri != null && mSurface != null) {
            mVideoHandler.obtainMessage(MSG_START).sendToTarget();
        }
    }

    public void pause() {
        if (isPlaying()) {
            mVideoHandler.obtainMessage(MSG_PAUSE).sendToTarget();
        }
        mTargetState = STATE_PAUSED;
    }

    public void resume() {
        if (!isPlaying()) {
            mVideoHandler.obtainMessage(MSG_START).sendToTarget();
        }
        mTargetState = STATE_PLAYING;
    }

    public void stop() {
        if (isInPlaybackState()) {
            mVideoHandler.obtainMessage(MSG_STOP).sendToTarget();
        }
        mTargetState = STATE_PLAYBACK_COMPLETED;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && mMediaPlayer.isPlaying();
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null && mCurrentState != STATE_ERROR && mCurrentState != STATE_IDLE
                        && mCurrentState != STATE_PREPARING);
    }

    @Override
    public void onCompletion(final MediaPlayer mp) {
        mCurrentState = STATE_PLAYBACK_COMPLETED;
        mTargetState = STATE_PLAYBACK_COMPLETED;
        if (mMediaPlayerCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerCallback.onCompletion(mp);
                }
            });
        }
    }

    @Override
    public boolean onError(final MediaPlayer mp, final int what, final int extra) {
        if (SHOW_LOGS) {
            Log.e(TAG, "onError() called with " + "mp = [" + mp + "], what = [" + what + "], extra = [" + extra + "]");
        }
        mCurrentState = STATE_ERROR;
        mTargetState = STATE_ERROR;
        if (mMediaPlayerCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerCallback.onError(mp, what, extra);
                }
            });
        }
        return true;
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        if (SHOW_LOGS) {
            Log.i(TAG, "onPrepared " + mUri.toString());
        }
        mCurrentState = STATE_PREPARED;

        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
            mTargetState = STATE_PLAYING;
        }

        if (mMediaPlayerCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerCallback.onPrepared(mp);
                }
            });
        }
    }

    @Override
    public void onVideoSizeChanged(final MediaPlayer mp, final int width, final int height) {
        if (mMediaPlayerCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerCallback.onVideoSizeChanged(mp, width, height);
                }
            });
        }
    }

    @Override
    public void onBufferingUpdate(final MediaPlayer mp, final int percent) {
        if (mMediaPlayerCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerCallback.onBufferingUpdate(mp, percent);
                }
            });
        }
    }

    @Override
    public boolean onInfo(final MediaPlayer mp, final int what, final int extra) {
        if (mMediaPlayerCallback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMediaPlayerCallback.onInfo(mp, what, extra);
                }
            });
        }
        return true;
    }
}
