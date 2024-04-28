package com.maayan.hangman;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.io.InputStream;
import com.google.firebase.analytics.FirebaseAnalytics;


public class OptionsPage extends AppCompatActivity {

    private ImageView backBTN;
    private LinearLayout shareUs;
    private LinearLayout rateUs;
    private LinearLayout privacyPol;
    
    private  LinearLayout termsUse;

    private FirebaseAnalytics firebaseAnalytics;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_menu);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        findViews();

    }

    private void showHtmlPopup() {
        // Inflate the layout for the popup
        View popupView = getLayoutInflater().inflate(R.layout.popup_page, null);

        // Create a WebView to load and display HTML content
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) WebView webView = popupView.findViewById(R.id.webView);

        // Load HTML content from the assets folder
        loadHtmlFromAssets(webView, "privacy_policy.html");

        // Create a PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
        );

        // Set WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if your HTML requires it

        // Close button
        ImageView btnClose = popupView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the popup
                popupWindow.dismiss();
            }
        });

        // Show the popup
        popupWindow.showAtLocation(popupView, 0, 0, 0);
    }

    private void showHtmlPopupForTerms() {
        // Inflate the layout for the popup
        View popupView = getLayoutInflater().inflate(R.layout.popup_page, null);

        // Create a WebView to load and display HTML content
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) WebView webView = popupView.findViewById(R.id.webView);

        // Load HTML content from the assets folder
        loadHtmlFromAssets(webView, "terms_of_use.html");

        // Create a PopupWindow
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
        );

        // Set WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if your HTML requires it

        // Close button
        ImageView btnClose = popupView.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the popup
                popupWindow.dismiss();
            }
        });

        // Show the popup
        popupWindow.showAtLocation(popupView, 0, 0, 0);
    }
    private void loadHtmlFromAssets(WebView webView, String htmlFileName) {
        try {
            // Load HTML content from the assets folder
            InputStream inputStream = getAssets().open(htmlFileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String htmlContent = new String(buffer);
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findViews() {

        backBTN = findViewById(R.id.BTN_back);
        shareUs = findViewById(R.id.shareUsBTN);
        rateUs = findViewById(R.id.rateUsBTN);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //log into analytics
                Bundle params = new Bundle();
                params.putString("button_clicked", "share_us_button_clicked");
                firebaseAnalytics.logEvent("share_us_button_clicked", params);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Check out this cool Hangman app! [URL to app on Google Play Store]";
                String shareSubject = "Hangman App";

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);

                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            }
        });

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //log into analytics
                Bundle params = new Bundle();
                params.putString("button_clicked", "rate_us_button_clicked");
                firebaseAnalytics.logEvent("rate_us_button_clicked", params);

                showRateAppDialog();
            }
        });

        privacyPol = findViewById(R.id.LBL_privacy_policy);
        termsUse = findViewById(R.id.termsOfUseBTN);

        privacyPol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHtmlPopup();
            }
        });
        
        termsUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHtmlPopupForTerms();
            }
        });
    }

    private void showRateAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate Us! ⭐");
        builder.setMessage("If you enjoy using the app, please take a moment to rate it. Thank you for your support!");

        builder.setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                openAppStoreForRating();
            }
        });

        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Never", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Here you might want to handle the user's decision of never wanting to rate the app
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openAppStoreForRating() {
        // Replace "com.example.yourapp" with your app's package name
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
