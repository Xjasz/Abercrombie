package com.jaszapps.abercrombie;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBlank extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreateFragment");
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "onDestroyFragment");
        super.onDestroy();
    }
}
