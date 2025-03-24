package com.bryancandi.linux.kernel.docs;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bryancandi.linux.kernel.docs.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    String webURL = "https://www.kernel.org/";
    String localURL = "https://appassets.androidplatform.net/assets/linux/index.html";
    String changesURL = "https://appassets.androidplatform.net/assets/changelog/changelog.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this); // enables edge-to-edge on old android versions
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "onMenuOpened", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return super.onCreateOptionsMenu(menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_kernel_archives) {
            Intent kernelLink = new Intent(android.content.Intent.ACTION_VIEW);
            kernelLink.setData(Uri.parse(webURL));
            startActivity(kernelLink);
            return true;
        }

        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.about_title));
            builder.setMessage(getString(R.string.kernel_tag) + "\n" +
                    (getString(R.string.kernel_version)) + "\n" + "\n" +
                    getString(R.string.app_version) + "\n" +
                    BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");
            builder.setIcon(R.drawable.ic_about);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog kernelDialog = builder.create();
            kernelDialog.setCancelable(true);
            kernelDialog.show();
            return true;
        }

        if (id == R.id.action_changelog) {
            WebView webView = findViewById(R.id.webView);
            webView.loadUrl(changesURL);
        }

        if (id == R.id.action_forward) {
            WebView webView = findViewById(R.id.webView);
            if (webView.canGoForward()) {
                webView.goForward();
            }
        }

        if (id == R.id.action_back) {
            WebView webView = findViewById(R.id.webView);
            if (webView.canGoBack()) {
                webView.goBack();
            }
        }

        if (id == R.id.action_exit) {
            ActivityCompat.finishAffinity(this);
        }

        if (id == R.id.action_home) {
            WebView webView = findViewById(R.id.webView);
            SharedPreferences prefs = getSharedPreferences("WEBVIEW", Context.MODE_PRIVATE);
            prefs.edit().clear().commit(); //clear shared prefs for fresh start.
            webView.loadUrl(localURL);
            Snackbar.make(webView, getString(R.string.action_home_snackbar), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

