package com.example.kate.flathead;

import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.kate.flathead.immersivemode.BasicImmersiveModeFragment;
import com.example.kate.flathead.message.MessageBuilder;
import com.example.kate.flathead.message.display.MessageDisplayFragment;
import com.example.kate.flathead.message.picker.MessagePickerFragment;
import com.example.kate.flathead.message.types.ScreenMessage;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usbcameracommon.UVCCameraHandlerMultiSurface;
import com.serenegiant.widget.UVCCameraTextureView;


public class MainActivity extends AppCompatActivity
        implements MessagePickerFragment.OnMessageSelectedListener,
        CameraDialog.CameraDialogParent {


    public MessageBuilder mb;
    public MessageDisplayFragment mdf;
    public MessagePickerFragment mpf;
    //  public UVCCameraFragment ucf;

    BasicImmersiveModeFragment immersiveModeFragment;


    private static final boolean DEBUG = true;    // TODO set false on release


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

        testDisplayFragment();
    }


    public void onMessageSelected(ScreenMessage sm) {
        if (mdf == null) {
            //something went wrong
        } else {
            mdf.updateDisplay(sm);

        }
    }


    private void testDisplayFragment() {

        mdf.updateDisplay("this is a test", Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf"), Color.RED);

    }


    //======================================================
    //======================================================
    //======================================================
    //======================================================
    //======================================================
    private static final String TAG = "MainActivity";
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
    private UVCCameraHandlerMultiSurface mCameraHandler;
    /**
     * for camera preview display
     */
    private UVCCameraTextureView mUVCCameraView;
    /**
     * for open&start / stop&close camera preview
     */
    private ToggleButton mCameraButton;
    private int mPreviewSurfaceId;
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(
                final CompoundButton compoundButton, final boolean isChecked) {

            switch (compoundButton.getId()) {
                case R.id.camera_button:
                    if (isChecked && !mCameraHandler.isOpened()) {
                        CameraDialog.showDialog(MainActivity.this);
                    } else {
                        stopPreview();
                    }
                    break;
            }
        }
    };
    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener
            = new USBMonitor.OnDeviceConnectListener() {

        @Override
        public void onAttach(final UsbDevice device) {
            Toast.makeText(MainActivity.this,
                    "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnect(final UsbDevice device,
                              final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {

            if (DEBUG) Log.v(TAG, "onConnect:");
            mCameraHandler.open(ctrlBlock);
            startPreview();

        }

        @Override
        public void onDisconnect(final UsbDevice device,
                                 final USBMonitor.UsbControlBlock ctrlBlock) {

            if (DEBUG) Log.v(TAG, "onDisconnect:");
            if (mCameraHandler != null) {

                //TODO: replace queueevent with a new threadcall
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        stopPreview();
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
            setCameraButton(false);
        }
    };

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

        mdf = (MessageDisplayFragment)
                getSupportFragmentManager().findFragmentById(R.id.displayFragment);

        mpf = (MessagePickerFragment)
                getSupportFragmentManager().findFragmentById(R.id.messageListFragment);

        //====================================================================================
        //====================================================================================
        //====================================================================================
        //====================================================================================


        if (DEBUG) Log.v(TAG, "onCreate:");

        mCameraButton = findViewById(R.id.camera_button);
        mCameraButton.setOnCheckedChangeListener(mOnCheckedChangeListener);

        mUVCCameraView = findViewById(R.id.camera_view);
        mUVCCameraView.setAspectRatio(PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);

        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
        mCameraHandler = UVCCameraHandlerMultiSurface.createHandler(this, mUVCCameraView,
                USE_SURFACE_ENCODER ? 0 : 1, PREVIEW_WIDTH, PREVIEW_HEIGHT, PREVIEW_MODE);


    }

    @Override
    protected void onStart() {
        super.onStart();

        mUSBMonitor.register();
    }

    @Override
    protected void onStop() {
        stopPreview();
        mCameraHandler.close();
        setCameraButton(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mCameraHandler != null) {
            mCameraHandler.release();
            mCameraHandler = null;
        }
        if (mUSBMonitor != null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
        mUVCCameraView = null;
        mCameraButton = null;
        super.onDestroy();
    }

    private void setCameraButton(final boolean isOn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCameraButton != null) {
                    try {
                        mCameraButton.setOnCheckedChangeListener(null);
                        mCameraButton.setChecked(isOn);
                    } finally {
                        mCameraButton.setOnCheckedChangeListener(mOnCheckedChangeListener);
                    }
                }

            }
        });

    }

    private void startPreview() {
        if (DEBUG) Log.v(TAG, "startPreview:");
        mUVCCameraView.resetFps();
        mCameraHandler.startPreview();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SurfaceTexture st = mUVCCameraView.getSurfaceTexture();
                    if (st != null) {
                        final Surface surface = new Surface(st);
                        mPreviewSurfaceId = surface.hashCode();
                        mCameraHandler.addSurface(mPreviewSurfaceId, surface, false);
                    }

                } catch (final Exception e) {
                    Log.w(TAG, e);
                }
            }
        });
    }

    private void stopPreview() {
        if (DEBUG) Log.v(TAG, "stopPreview:");

        if (mPreviewSurfaceId != 0) {
            mCameraHandler.removeSurface(mPreviewSurfaceId);
            mPreviewSurfaceId = 0;
        }
        mCameraHandler.close();
        setCameraButton(false);
    }

    /**
     * to access from CameraDialog
     *
     * @return
     */
    @Override
    public USBMonitor getUSBMonitor() {
        return mUSBMonitor;
    }

    @Override
    public void onDialogResult(boolean canceled) {
        if (DEBUG) Log.v(TAG, "onDialogResult:canceled=" + canceled);
        if (canceled) {
            setCameraButton(false);
        }
    }


}

