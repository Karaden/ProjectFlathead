package com.example.kate.flathead.cameraUVC;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.kate.flathead.R;
import com.serenegiant.usb.CameraDialog;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usbcameracommon.UVCCameraHandlerMultiSurface;
import com.serenegiant.widget.UVCCameraTextureView;

import static com.serenegiant.utils.UIThreadHelper.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 *
 * Use the {@link UVCCameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UVCCameraFragment extends Fragment
        implements CameraDialog.CameraDialogParent {

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
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(
                final CompoundButton compoundButton, final boolean isChecked) {

//            switch (compoundButton.getId()) {
//                case R.id.camera_button:
//                    if (isChecked && !mCameraHandler.isOpened()) {
//                        CameraDialog.showDialog(mActivity);
//                    } else {
//                        stopPreview();
//                    }
//                    break;
//            }
        }
    };

    /**
     * for open&start / stop&close camera preview
     */
    private ToggleButton mCameraButton;
    Activity mActivity;
    Context mContext;


    public UVCCameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UVCCameraFragment.
     */
    public static UVCCameraFragment newInstance() {

        return new UVCCameraFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uvccamera, container, false);
    }

    /**
     * for camera preview display
     */
    private UVCCameraTextureView uvcCameraTextureView;


    //==============================
    private int mPreviewSurfaceId;
    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener
            = new USBMonitor.OnDeviceConnectListener() {

        @Override
        public void onAttach(final UsbDevice device) {
            Toast.makeText(mActivity,
                    "USB_DEVICE_ATTACHED", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnect(final UsbDevice device,
                              final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {


            mCameraHandler.open(ctrlBlock);
            startPreview();

        }

        @Override
        public void onDisconnect(final UsbDevice device,
                                 final USBMonitor.UsbControlBlock ctrlBlock) {


            if (mCameraHandler != null) {

                //TODO: replace queueevent with a new threadcall
                new Runnable() {
                    @Override
                    public void run() {
                        stopPreview();
                    }
                };
            }
        }

        @Override
        public void onDettach(final UsbDevice device) {
            Toast.makeText(mActivity,
                    "USB_DEVICE_DETACHED", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(final UsbDevice device) {
            setCameraButton(false);
        }
    };

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        mActivity = getActivity();
        mContext = getContext();

        mCameraButton = view.findViewById(R.id.camera_button);
        mCameraButton.setOnCheckedChangeListener(mOnCheckedChangeListener);

        uvcCameraTextureView = view.findViewById(R.id.camera_view);
        uvcCameraTextureView.setAspectRatio(PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);

        mUSBMonitor = new USBMonitor(mContext, mOnDeviceConnectListener);
        mCameraHandler = UVCCameraHandlerMultiSurface.createHandler(mActivity, uvcCameraTextureView,
                USE_SURFACE_ENCODER ? 0 : 1, PREVIEW_WIDTH, PREVIEW_HEIGHT, PREVIEW_MODE);


    }

    @Override
    public void onStart() {
        super.onStart();

        mUSBMonitor.register();
    }

    @Override
    public void onStop() {
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
        uvcCameraTextureView = null;
        mCameraButton = null;
        super.onDestroy();
    }

    private void setCameraButton(final boolean isOn) {

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
//        }, 0);
    }

    private void startPreview() {

        uvcCameraTextureView.resetFps();
        mCameraHandler.startPreview();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SurfaceTexture st = uvcCameraTextureView.getSurfaceTexture();
                    if (st != null) {
                        final Surface surface = new Surface(st);
                        mPreviewSurfaceId = surface.hashCode();
                        mCameraHandler.addSurface(mPreviewSurfaceId, surface, false);
                    }

                } catch (final Exception e) {
                    //TODO
                }
            }
        });
    }

    private void stopPreview() {

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

        if (canceled) {
            setCameraButton(false);
        }
    }

}
