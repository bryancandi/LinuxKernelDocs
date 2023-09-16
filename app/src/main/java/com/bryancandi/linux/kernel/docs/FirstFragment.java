package com.bryancandi.linux.kernel.docs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.webkit.WebViewAssetLoader;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.webkit.WebViewClientCompat;

public class FirstFragment extends Fragment {

    private WebView mWebView;

    private static class LocalContentWebViewClient extends WebViewClientCompat {
        private final WebViewAssetLoader mAssetLoader;
        LocalContentWebViewClient(WebViewAssetLoader assetLoader) {
            mAssetLoader = assetLoader;
        }
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          WebResourceRequest request) {
            return mAssetLoader.shouldInterceptRequest(request.getUrl());
        }
    }
    //String localURL = "file:///android_asset/index.html";
    String localURL = "https://appassets.androidplatform.net/assets/index.html";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = requireView().findViewById(R.id.webView);

        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(requireActivity()))
                //.addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(requireActivity()))
                //.setDomain("kernel.org")
                .build();
        mWebView.setWebViewClient(new LocalContentWebViewClient(assetLoader));

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAllowFileAccessFromFileURLs(false); // Setting this off for security.
        webSettings.setAllowUniversalAccessFromFileURLs(false); // Setting this off for security.
        webSettings.setAllowFileAccess(false); // Setting this off for security.
        webSettings.setAllowContentAccess(false); // Setting this off for security.

        SharedPreferences prefs = requireActivity().getSharedPreferences("WEBVIEW", Context.MODE_PRIVATE);
        String savedUrl = prefs.getString("URL_TAG", null); //load saved URL and apply to string 'savedURL'

        if(savedUrl != null) {
            mWebView.loadUrl(savedUrl);
            //Log.d(TAG,"savedURL");
        }else{
            mWebView.loadUrl(localURL);
            //Log.d(TAG, "localURL");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        String currentUrl = mWebView.getUrl(); //get current URL on app close
        SharedPreferences prefs = requireActivity().getSharedPreferences("WEBVIEW", Context.MODE_PRIVATE);
        prefs.edit().putString("URL_TAG", currentUrl ).apply(); //save current URL to Shared Prefs
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}