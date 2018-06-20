package com.karaden.flathead;

import android.app.Presentation;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karaden.flathead.immersivemode.BasicImmersiveModeFragment;
import com.karaden.flathead.message.MessageBuilder;
import com.karaden.flathead.message.picker.MessagePickerFragment;
import com.karaden.flathead.message.types.ConversationMessage;
import com.karaden.flathead.message.types.MoodPromptMessage;
import com.karaden.flathead.message.types.ScreenMessage;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;

import com.serenegiant.usb.USBMonitor.UsbControlBlock;
import com.serenegiant.usb.USBMonitor.OnDeviceConnectListener;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usbcameracommon.UVCCameraHandler;
import com.serenegiant.usbcameracommon.UVCCameraHandlerMultiSurface;
import com.serenegiant.widget.CameraViewInterface;
import com.serenegiant.widget.UVCCameraTextureView;


public class MainActivity extends AppCompatActivity
        implements MessagePickerFragment.OnMessageSelectedListener,
        CameraDialog.CameraDialogParent {


    private MessageBuilder mb;
    private MessagePickerFragment mpf;

    private BasicImmersiveModeFragment immersiveModeFragment;


    private DisplayManager mDisplayManager;
    private DemoPresentation mDemoPresentation;
    private DemoPresentationContents mDemoPresentationContents;
    private Typeface moodPromptFont;
    private Typeface conversationFont;


    private static final boolean DEBUG = true;    // TODO set false on release

    public enum TypefaceName {
        FURMANITE,
        DATACONTROL

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentByTag("BasicImmersiveModeFragment") == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            immersiveModeFragment = new BasicImmersiveModeFragment();
            transaction.add(immersiveModeFragment, "BasicImmersiveModeFragment");
            transaction.commit();
        }

        setContentView(R.layout.activity_main);

        mb = new MessageBuilder(this);

        mpf = (MessagePickerFragment)
                getSupportFragmentManager().findFragmentById(R.id.messageListFragment);

        //==============================================
        // =========Camera setup below this line========
        //==============================================
        cameraCreate();

        secondScreenCreate();

    }

    // onCreate stuff for secondScreen
    private void secondScreenCreate() {

        moodPromptFont = Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf");
        conversationFont = Typeface.createFromAsset(getAssets(), "fonts/datacontrol.ttf");

        // Get the display manager service.
        mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
    }




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            immersiveModeFragment.setImmersiveMode();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);


        mpf.setMessages(mb.getScreenMessages());

    }


    @Override
    protected void onStart() {
        super.onStart();

        cameraStart();
    }

    @Override
    protected void onStop() {

        cameraStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {



        cameraDestroy();

        super.onDestroy();
    }




//region Message handling
    public void onMessageSelected(ScreenMessage sm) {


            //Assuming only getting one back...
            Display[] displays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            mDemoPresentationContents = new DemoPresentationContents(sm);
            showPresentation(displays[0], mDemoPresentationContents);


    }


//endregion Message handling

//region Camera
    //==============================================================
    //==============================================================
    //==============Camera specific stuff below here================
    //TODO: refactor this into its own class. USB Monitor is tricky.
    //==============================================================
    //==============================================================
    private static final String TAG = "MainActivity";

    private static final float[] BANDWIDTH_FACTORS = { 0.5f, 0.5f };


    /**
     * set true if you want to record movie using MediaSurfaceEncoder
     * (writing frame data into Surface camera from MediaCodec
     * by almost same way as USBCameratest2)
     * set false if you want to record movie using MediaVideoEncoder
     */
    private static final boolean USE_SURFACE_ENCODER = false;
    /**
     * preview resolution(width)
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     */
    private static final int PREVIEW_WIDTH = 640;
    /**
     * preview resolution(height)
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     */
    private static final int PREVIEW_HEIGHT = 480;
    /**
     * preview mode
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     * 0:YUYV, other:MJPEG
     */
    private static final int PREVIEW_MODE = 1;



    /**
     * for accessing USB
     */
    private USBMonitor mUSBMonitor;

    /**
     * Handler to execute camera related methods sequentially on private thread
     */
    private UVCCameraHandler mHandlerL;
    private UVCCameraHandler mHandlerR;

    /**
     * for camera preview display
     */
    private CameraViewInterface mUVCCameraViewL;
    private CameraViewInterface mUVCCameraViewR;

    /**
     * for open&start / stop&close camera preview
     */
    private ToggleButton mCaptureButtonL;
    private ToggleButton mCaptureButtonR;


    private Surface mLeftPreviewSurface;
    private Surface mRightPreviewSurface;


//Done...ish?
    private void cameraCreate()
    {
        if (DEBUG) Log.v(TAG, "onCreate:");

        mUVCCameraViewL = (CameraViewInterface)findViewById(R.id.camera_view_L);
        mUVCCameraViewL.setAspectRatio(UVCCamera.DEFAULT_PREVIEW_WIDTH / (float)UVCCamera.DEFAULT_PREVIEW_HEIGHT);
        ((UVCCameraTextureView)mUVCCameraViewL).setOnClickListener(mOnClickListener);
        mCaptureButtonL = (ToggleButton) findViewById(R.id.capture_button_L);
        mCaptureButtonL.setOnClickListener(mOnClickListener);
        mCaptureButtonL.setVisibility(View.INVISIBLE);
        mHandlerL = UVCCameraHandler.createHandler(this, mUVCCameraViewL, UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, BANDWIDTH_FACTORS[0]);

        mUVCCameraViewR = (CameraViewInterface)findViewById(R.id.camera_view_R);
        mUVCCameraViewR.setAspectRatio(UVCCamera.DEFAULT_PREVIEW_WIDTH / (float)UVCCamera.DEFAULT_PREVIEW_HEIGHT);
        ((UVCCameraTextureView)mUVCCameraViewR).setOnClickListener(mOnClickListener);
        mCaptureButtonR = (ToggleButton) findViewById(R.id.capture_button_R);
        mCaptureButtonR.setOnClickListener(mOnClickListener);
        mCaptureButtonR.setVisibility(View.INVISIBLE);
        mHandlerR = UVCCameraHandler.createHandler(this, mUVCCameraViewR, UVCCamera.DEFAULT_PREVIEW_WIDTH, UVCCamera.DEFAULT_PREVIEW_HEIGHT, BANDWIDTH_FACTORS[1]);

        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
    }


    private void cameraStart() {
        //Camera
        mUSBMonitor.register();
        if (mUVCCameraViewR != null)
            mUVCCameraViewR.onResume();
        if (mUVCCameraViewL != null)
            mUVCCameraViewL.onResume();
    }

    //Done
    private void cameraStop() {
        mHandlerR.close();
        if (mUVCCameraViewR != null)
            mUVCCameraViewR.onPause();
        mHandlerL.close();
        mCaptureButtonR.setVisibility(View.INVISIBLE);
        if (mUVCCameraViewL != null)
            mUVCCameraViewL.onPause();
        mCaptureButtonL.setVisibility(View.INVISIBLE);
        mUSBMonitor.unregister();
    }

    //Done
    private void cameraDestroy() {
        if (mHandlerR != null) {
            mHandlerR = null;
        }
        if (mHandlerL != null) {
            mHandlerL = null;
        }
        if (mUSBMonitor != null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
        mUVCCameraViewR = null;
        mCaptureButtonR = null;
        mUVCCameraViewL = null;
        mCaptureButtonL = null;
    }


    //    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener
//            = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(
//                final CompoundButton compoundButton, final boolean isChecked) {
//
//            switch (compoundButton.getId()) {
//                case R.id.camera_button:
//                    if (isChecked && !mCameraHandler.isOpened()) {
//                        CameraDialog.showDialog(MainActivity.this);
//                    } else {
//                        stopPreview();
//                    }
//                    break;
//            }
//        }
//    };




    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.camera_view_L:
                    if (mHandlerL != null) {
                        if (!mHandlerL.isOpened()) {
                            CameraDialog.showDialog(MainActivity.this);
                        } else {
                            mHandlerL.close();
                            setCameraButton();
                        }
                    }
                    break;
                case R.id.capture_button_L:
                    break;
                case R.id.camera_view_R:
                    if (mHandlerR != null) {
                        if (!mHandlerR.isOpened()) {
                            CameraDialog.showDialog(MainActivity.this);
                        } else {
                            mHandlerR.close();
                            setCameraButton();
                        }
                    }
                    break;
                case R.id.capture_button_R:
                    break;
            }
        }
    };


//
//    private void setCameraButton(final boolean isOn) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (mCameraButton != null) {
//                    try {
//                        mCameraButton.setOnCheckedChangeListener(null);
//                        mCameraButton.setChecked(isOn);
//                    } finally {
//                        mCameraButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
//                    }
//                }
//
//            }
//        });
//
//    }

//    private void startPreview() {
//        if (DEBUG) Log.v(TAG, "startPreview:");
//        mUVCCameraView.resetFps();
//        mCameraHandler.startPreview();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final SurfaceTexture st = mUVCCameraView.getSurfaceTexture();
//                    if (st != null) {
//                        final Surface surface = new Surface(st);
//                        mPreviewSurfaceId = surface.hashCode();
//                        mCameraHandler.addSurface(mPreviewSurfaceId, surface, false);
//                    }
//
//                } catch (final Exception e) {
//                    Log.w(TAG, e);
//                }
//            }
//        });
//    }

//    private void stopPreview() {
//        if (DEBUG) Log.v(TAG, "stopPreview:");
//
//        if (mPreviewSurfaceId != 0) {
//            mCameraHandler.removeSurface(mPreviewSurfaceId);
//            mPreviewSurfaceId = 0;
//        }
//        mCameraHandler.close();
//        setCameraButton(false);
//    }


//Done
private final OnDeviceConnectListener mOnDeviceConnectListener = new OnDeviceConnectListener() {

    @Override
    public void onAttach(final UsbDevice device) {
        Toast.makeText(MainActivity.this,
                "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnect(final UsbDevice device,
                          final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {

        if (DEBUG) Log.v(TAG, "onConnect:" + device);
        if (!mHandlerL.isOpened()) {
            mHandlerL.open(ctrlBlock);
            final SurfaceTexture st = mUVCCameraViewL.getSurfaceTexture();
            mHandlerL.startPreview(new Surface(st));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCaptureButtonL.setVisibility(View.VISIBLE);
                }
            });
        } else if (!mHandlerR.isOpened()) {
            mHandlerR.open(ctrlBlock);
            final SurfaceTexture st = mUVCCameraViewR.getSurfaceTexture();
            mHandlerR.startPreview(new Surface(st));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCaptureButtonR.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onDisconnect(final UsbDevice device,
                             final USBMonitor.UsbControlBlock ctrlBlock) {

        if (DEBUG) Log.v(TAG, "onDisconnect:");
        if ((mHandlerL != null) && !mHandlerL.isEqual(device)) {

            //Replace queueevent with a new threadcall
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    mHandlerL.close();
                    if (mLeftPreviewSurface != null) {
                        mLeftPreviewSurface.release();
                        mLeftPreviewSurface = null;
                    }
                    setCameraButton();
                }
            };

            r.run();
        }
        else if ((mHandlerR != null) && !mHandlerR.isEqual(device)) {
            //Replace queueevent with a new threadcall
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    mHandlerR.close();
                    if (mRightPreviewSurface != null) {
                        mRightPreviewSurface.release();
                        mRightPreviewSurface = null;
                    }
                    setCameraButton();
                }
            };

            r.run();

        }
    }

    @Override
    public void onDettach(final UsbDevice device) {
        Toast.makeText(MainActivity.this,
                "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(final UsbDevice device) {
        if (DEBUG) Log.v(TAG, "onCancel:");
    }
};

    /**
     * to access from CameraDialog
     */
    @Override //Done
    public USBMonitor getUSBMonitor() {
        return mUSBMonitor;
    }

    @Override //Done
    public void onDialogResult(boolean canceled) {
        if (DEBUG) Log.v(TAG, "onDialogResult:canceled=" + canceled);
        if (canceled) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCameraButton();
                }
            });
        }
    }

    //Straight copy
    private void setCameraButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((mHandlerL != null) && !mHandlerL.isOpened() && (mCaptureButtonL != null)) {
                    mCaptureButtonL.setVisibility(View.INVISIBLE);
                }
                if ((mHandlerR != null) && !mHandlerR.isOpened() && (mCaptureButtonR != null)) {
                    mCaptureButtonR.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
//endregion Camera

//region Second Screen

    private Typeface getTypeface(TypefaceName tfn){

        switch (tfn) {
            case FURMANITE:
                return moodPromptFont;
            case DATACONTROL:
                return conversationFont;
            default:
                //TODO: pick a Default
                return conversationFont;
        }

    }



    /**
     * Shows a {@link Presentation} on the specified display.
     */
    private void showPresentation(Display display, DemoPresentationContents contents) {

        mDemoPresentation = new DemoPresentation(this, display, contents);
        mDemoPresentation.show();

    }


    /**
     * The presentation to show on the secondary display.
     *
     * Note that the presentation display may have different metrics from the display on which
     * the main activity is showing so we must be careful to use the presentation's
     * own {@link Context} whenever we load resources.
     */
    private final class DemoPresentation extends Presentation {

        final DemoPresentationContents mContents;


        DemoPresentation(Context context, Display display,
                         DemoPresentationContents contents) {
            super(context, display);
            mContents = contents;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // Be sure to call the super class.
            super.onCreate(savedInstanceState);


            // Inflate the layout.
            setContentView(R.layout.messagedisplaylayout);

            TextView primaryLabel = findViewById(R.id.mainLabel);
            primaryLabel.setVisibility(mDemoPresentationContents.mPrimaryLabelVisible);
            primaryLabel.setText(mDemoPresentationContents.mPrimaryLabelText);
            primaryLabel.setTypeface(getTypeface(mDemoPresentationContents.mPrimaryLabelTypeface));
            primaryLabel.setTextColor(mDemoPresentationContents.mPrimaryLabelTextColor);

            TextView secondaryLabel = findViewById(R.id.subLabel);
            secondaryLabel.setVisibility(mDemoPresentationContents.mSecondaryLabelVisible);
            if (mDemoPresentationContents.mSecondaryLabelVisible == View.VISIBLE) {
                secondaryLabel.setText(mDemoPresentationContents.mSecondaryLabelText);
                secondaryLabel.setTypeface(getTypeface(mDemoPresentationContents.mSecondaryLabelTypeface));
                secondaryLabel.setTextColor(mDemoPresentationContents.mSecondaryLabelTextColor);
            }



            // Show a n image for visual interest.
            ImageView image = findViewById(R.id.messageImage);
            image.setVisibility(mDemoPresentationContents.mLogoVisible);
            if (mDemoPresentationContents.mLogoVisible == View.VISIBLE)
            {
                image.setImageResource(mDemoPresentationContents.mLogoImage);
            }
        }

    }

    /**
     * Information about the content we want to show in the presentation.
     */
    private final static class DemoPresentationContents implements Parcelable {

        int displayModeId;

        int mPrimaryLabelVisible;
        String mPrimaryLabelText;
        TypefaceName mPrimaryLabelTypeface;
        int mPrimaryLabelTextColor;

        int mSecondaryLabelVisible;
        String mSecondaryLabelText;
        TypefaceName mSecondaryLabelTypeface;
        int mSecondaryLabelTextColor;

        int mLogoVisible;
        int mLogoImage;



        public static final Creator<DemoPresentationContents> CREATOR =
                new Creator<DemoPresentationContents>() {
                    @Override
                    public DemoPresentationContents createFromParcel(Parcel in) {
                        return new DemoPresentationContents(in);
                    }

                    @Override
                    public DemoPresentationContents[] newArray(int size) {
                        return new DemoPresentationContents[size];
                    }
                };


        DemoPresentationContents(ScreenMessage sm) {
            if (sm instanceof MoodPromptMessage) {

            mPrimaryLabelVisible = View.VISIBLE;
            mPrimaryLabelText = sm.primaryText;
            mPrimaryLabelTypeface = TypefaceName.FURMANITE;
            mPrimaryLabelTextColor = sm.primaryColour;

            mSecondaryLabelVisible = View.VISIBLE;
            mSecondaryLabelText = sm.secondaryText;
            mSecondaryLabelTypeface = TypefaceName.FURMANITE;
            mSecondaryLabelTextColor = sm.secondaryColour;

            mLogoVisible = View.VISIBLE;
            mLogoImage = sm.logoResourceID;



            }
            else if (sm instanceof ConversationMessage) {

                mPrimaryLabelVisible = View.VISIBLE;
                mPrimaryLabelText = sm.primaryText;
                mPrimaryLabelTypeface = TypefaceName.DATACONTROL;
                mPrimaryLabelTextColor = sm.primaryColour;

                mSecondaryLabelVisible = View.GONE;

                mLogoVisible = View.GONE;


            }
        }

        private DemoPresentationContents(Parcel in)
        {
            displayModeId = in.readInt();
            mPrimaryLabelVisible = in.readInt();
            mPrimaryLabelText = in.readString();
            mPrimaryLabelTypeface = TypefaceName.values()[in.readInt()];
            mPrimaryLabelTextColor = in.readInt();

            mSecondaryLabelVisible = in.readInt();
            mSecondaryLabelText = in.readString();
            mSecondaryLabelTypeface = TypefaceName.values()[in.readInt()];
            mSecondaryLabelTextColor = in.readInt();

            mLogoVisible = in.readInt();
            mLogoImage = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            //TODO
            dest.writeInt(displayModeId);

            dest.writeInt(mPrimaryLabelVisible);
            dest.writeString(mPrimaryLabelText);
            dest.writeInt(mPrimaryLabelTypeface.ordinal());
            dest.writeInt(mPrimaryLabelTextColor);

            dest.writeInt(mSecondaryLabelVisible);
            dest.writeString(mSecondaryLabelText);
            dest.writeInt(mSecondaryLabelTypeface.ordinal());
            dest.writeInt(mSecondaryLabelTextColor);

            dest.writeInt(mLogoVisible);
            dest.writeInt(mLogoImage);
        }

    }

//endregion Second Screen


}









