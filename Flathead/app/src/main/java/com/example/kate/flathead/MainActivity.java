package com.example.kate.flathead;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.kate.flathead.cameraUVC.UVCCameraFragment;
import com.example.kate.flathead.immersivemode.BasicImmersiveModeFragment;
import com.example.kate.flathead.message.MessageBuilder;
import com.example.kate.flathead.message.display.MessageDisplayFragment;
import com.example.kate.flathead.message.picker.MessagePickerFragment;
import com.example.kate.flathead.message.types.ScreenMessage;


public class MainActivity extends AppCompatActivity
        implements MessagePickerFragment.OnMessageSelectedListener {


    public MessageBuilder mb;
    public MessageDisplayFragment mdf;
    public MessagePickerFragment mpf;
    public UVCCameraFragment ucf;

    BasicImmersiveModeFragment immersiveModeFragment;


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

        ucf = (UVCCameraFragment)
                getSupportFragmentManager().findFragmentById((R.id.vid_fragment));


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
//        mdf.updateDisplay("this is a test", Typeface.createFromFile("fonts/furmanite.otf"), Color.YELLOW); //this method does not work


    }

}

