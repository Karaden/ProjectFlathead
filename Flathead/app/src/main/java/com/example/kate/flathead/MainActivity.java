package com.example.kate.flathead;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.kate.flathead.immersivemode.BasicImmersiveModeFragment;
import com.example.kate.flathead.message.MessageFragmentsController;


public class MainActivity extends AppCompatActivity {


    public MessageFragmentsController mfc;

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

    }

}

