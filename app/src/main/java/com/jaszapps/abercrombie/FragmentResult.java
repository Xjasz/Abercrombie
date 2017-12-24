package com.jaszapps.abercrombie;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import core.Singleton;
import core.reflection.injectors.Inject;
import core.reflection.listeners.Listen;
import core.reflection.listeners.Listener;
import core.reflection.messages.BackRequestMessage;
import core.reflection.messages.FragmentChangeMessage;
import core.reflection.models.BaseFragment;


public class FragmentResult extends BaseFragment {
    private final String TAG = getClass().getSimpleName();

    @Inject(id = R.id.webView)
    private WebView webView;

    @Listen(consumed = true)
    public void onMessageReceived(BackRequestMessage<Integer> request) {
        switch (request.object) {
            case 0:
                Listener.send(new FragmentChangeMessage(R.id.container_SubFragment,
                    R.anim.slidein_fromleft,
                    R.anim.slideout_to_right,
                    new FragmentBlank()));
                break;
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = super.onCreateView(inflater, container, savedInstanceState);
        initializeFragment();
        return viewGroup;
    }

    private void initializeFragment() {
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setSupportZoom(true);

        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(Singleton.ContentToShow);
    }

}