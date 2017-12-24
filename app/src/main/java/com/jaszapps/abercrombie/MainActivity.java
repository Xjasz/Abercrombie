package com.jaszapps.abercrombie;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import core.Singleton;
import core.reflection.injectors.Inject;
import core.reflection.listeners.Listen;
import core.reflection.listeners.Listener;
import core.reflection.messages.ActionBarMessage;
import core.reflection.messages.BackRequestMessage;
import core.reflection.messages.BackResponseMessage;
import core.reflection.messages.FragmentChangeMessage;
import core.reflection.models.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private ViewGroup actionBar;
    private TextView tvTitle;
    private ImageView ivMenu;
    private ImageView ivRefresh;

    @Inject(id = R.id.statusBarBackground)
    private View statusBarBackground;

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Singleton.registerMainActivity(this);
        Singleton.checkConnection();
        initializeActionBar();
        initializeActivity();
    }


    @Listen
    public void onFragmentChangeMessageReceived(FragmentChangeMessage fcm) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(fcm.inAnimation, fcm.outAnimation);
        transaction.replace(fcm.containerID, fcm.fragment).commit();
    }

    @Override
    public int getStatusBarColor(View view) {
        int color = Color.BLACK;
        if (view != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if (view.getBackground() instanceof ColorDrawable) {
                    color = ((ColorDrawable) view.getBackground()).getColor();
                    Log.d(TAG, "color :" + String.valueOf(color));
                    float[] hsv = new float[3];
                    Color.colorToHSV(color, hsv);
                    hsv[2] *= 0.8f;
                    color = Color.HSVToColor(hsv);
                    return color;
                }
            }
        }
        Log.d(TAG, "invalid color");
        return color;
    }

    @Override
    public void onBackPressed() {
        Listener.send(new BackRequestMessage(0));
    }

    @Listen()
    public void onBackPressResponseMessage(BackResponseMessage<Integer> response) {
        Log.d(TAG, "onBackPressResponseMessage = " + String.valueOf(response.object));
        switch (response.object) {
            case 0:
                finish();
                break;
        }
    }

    private void initializeActionBar() {
        if (actionBar == null) {
            actionBar = (ViewGroup) findViewById(R.id.container_Actionbar);
            getLayoutInflater().inflate(R.layout.actionbar_main, actionBar);
            tvTitle = (TextView) actionBar.findViewById(R.id.tvTitle);
            ivMenu = (ImageView) actionBar.findViewById(R.id.ivMenu);
            ivRefresh = (ImageView) actionBar.findViewById(R.id.ivRefresh);
            ivMenu.setColorFilter(Color.WHITE);
            ivRefresh.setColorFilter(Color.WHITE);
            ivMenu.setOnClickListener(this);
            ivRefresh.setOnClickListener(this);
        }
    }


    private void initializeActivity() {
        getSupportFragmentManager().beginTransaction().add(R.id.container_MainFragment, new FragmentMain()).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container_SubFragment, new FragmentBlank()).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMenu:
                Log.d(TAG, "ivMenu pressed");
                Singleton.toastMessage("Menu not set");
                break;
            case R.id.ivRefresh:
                Log.d(TAG, "ivRefresh pressed");
                Listener.send(new ActionBarMessage(3));
                break;
        }
    }
}
