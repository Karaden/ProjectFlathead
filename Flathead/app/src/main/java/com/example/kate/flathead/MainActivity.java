package com.example.kate.flathead;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.kate.flathead.immersivemode.BasicImmersiveModeFragment;
import com.example.kate.flathead.message.display.MessageDisplayFragment;
import com.example.kate.flathead.message.picker.MessagePickerFragment;


public class MainActivity extends AppCompatActivity
        implements MessagePickerFragment.OnMessageSelectedListener {


    public MessageDisplayFragment mdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentByTag("BasicImmersiveModeFragment") == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BasicImmersiveModeFragment fragment = new BasicImmersiveModeFragment();
            transaction.add(fragment, "BasicImmersiveModeFragment");
            transaction.commit();
        }

        setContentView(R.layout.activity_main);
        mdf = (MessageDisplayFragment)
                getSupportFragmentManager().findFragmentById(R.id.displayFragment);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);


        testDisplayFragment();
    }


    public void onMessageSelected(int position) {

        //TODO


        if (mdf == null) {
            //something went wrong
        } else {
            // call sm.display(mdf)

        }

    }


    private void testDisplayFragment() {

        mdf.updateDisplay("this is a test", Typeface.createFromAsset(getAssets(), "fonts/furmanite.otf"), Color.RED);
    }

}

