package com.bryancandi.linux.kernel.docs;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    private WebView webView;

    @Override
    public void onPause(){
        super.onPause();
        String currentUrl = webView.getUrl(); //get current URL on app close
        SharedPreferences prefs = requireActivity().getSharedPreferences("WEBVIEW", Context.MODE_PRIVATE);
        prefs.edit().putString("URL_TAG", currentUrl ).apply(); //save current URL to Shared Prefs
    }
    String localURL = "file:///android_asset/index.html";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = requireView().findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webView.setWebChromeClient( new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("WEBVIEW", Context.MODE_PRIVATE);
        String savedUrl = prefs.getString("URL_TAG", null); //load saved URL and apply to string 'savedURL'

        if(savedUrl != null) {
            webView.loadUrl(savedUrl);
            Log.d(TAG,"savedURL");
        }else{
            webView.loadUrl(localURL);
            Log.d(TAG, "localURL");
        }

        //webView.loadUrl(localURL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}