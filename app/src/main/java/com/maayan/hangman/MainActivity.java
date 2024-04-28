package com.maayan.hangman;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.gms.ads.rewarded.RewardedAd;

public class MainActivity extends AppCompatActivity {
    private ImageView[] hangmanImages = new ImageView[6];
    private AdView adView;
    private int[] faces = new int[6];
    private TextView[] letters = new TextView[26];
    private TextView word; //this is what is being shown to the user (---- at the start)
    private Words words;

    private FirebaseAnalytics firebaseAnalytics;
    private String currentWord; //this is the random-generated word
    private int counter;
    private int coins;
    private int currentCharIndex;
    private TextView coinsShow;
    private Button newWord;
    private Button hint;

    private ImageView options;
    private int bolChanged;

    RewardedAd rewardedAd;

    private FrameLayout bannerFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //bannerFrame = findViewById(R.id.bannerFrame);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
               Log.d("Crashlytics", "here??");

            }
        });


        //loadBanner();
        /*
           Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        // Add the crash button to the layout
        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
         */

        startNewGame();
    }



    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBanner() {

            // Create a new ad view.
        AdView mAdView = new AdView(this);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView.setAdSize(AdSize.FULL_BANNER);
        bannerFrame.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        }


    private void setButtonsClickListeners() {

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assuming currentWord is a String variable
                //  Toast.makeText(MainActivity.this, "we are here", Toast.LENGTH_SHORT).show();

                Bundle params = new Bundle();
                params.putString("button_clicked", "hint_button_clicked");
                firebaseAnalytics.logEvent("hint_button_clicked", params);

                if(coins>=3)
                {
                    coins-=3;//hint costs 3 tokens
                    saveCoins();
                    retrieveCoinNumber();


                    if (currentCharIndex < currentWord.length()) {
                        if(word.getText().toString().indexOf('_', currentCharIndex) != -1)
                        {
                            revealLetter(Character.toUpperCase(currentWord.charAt(currentCharIndex)), currentCharIndex++);

                        }
                        else
                        {
                            if(word.getText().toString().charAt(currentCharIndex) == ' ')
                            {
                                currentCharIndex += 2;

                            }
                            else{
                                currentCharIndex++;
                            }
                            //onClick(view);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "All letters are already revealed!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    outof();
                }





            }


        });


        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle params = new Bundle();
                params.putString("button_clicked", "new_word_button_clicked");
                firebaseAnalytics.logEvent("new_word_button_clicked", params);

                if(coins>=5) {
                    coins -= 5;//new word costs 3 tokens
                    saveCoins();
                    retrieveCoinNumber();
                    startNewGame();
                    //save coin number to shared prefrences:
                    // In your activity or wherever appropriate
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("coinNumber", coins);
                    editor.apply();
                }
                else{
                    outof();
                }


            }
        });
    }

    private void outof() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Out of Tokens?");
        builder.setMessage("Watch an ad to earn 4 more tokens!");

        builder.setPositiveButton("Watch Ad", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //log into analytics
                Bundle params = new Bundle();
                params.putString("button_clicked", "watched_ad");
                firebaseAnalytics.logEvent("watched_ad", params);

                showRewardedVideoAdvertisment();
                loadRewardedVideoAdvertisment();
                // Load rewarded ad
                AdRequest adRequest = new AdRequest.Builder().build();
                RewardedAd.load(MainActivity.this, "ca-app-pub-3940256099942544/5224354917", adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        // Handle ad loading failure
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        super.onAdLoaded(rewardedAd);
                        rewardedAd = ad;
                        Log.d("MAAYA1212", "Ad was loaded.");

                        coins+=4;
                        saveCoins();
                        retrieveCoinNumber();
                        dialog.dismiss();
                        //give tokens
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startNewGame() {

        initialization();
        retrieveCoinNumber();
        currentWord = words.getRandomWord();
        if (currentWord != null) {
            updateWordDisplay(); // Initial display with underscores
        }

        setLetterClickListeners();
        setButtonsClickListeners();
        setOptionClickListener();

    }

    private void setOptionClickListener() {

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OptionsPage.class);
                startActivity(intent);


            }
        });
    }

    private void retrieveCoinNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve the existing coin number, or default to 0 if not found
        coins = sharedPreferences.getInt("coinNumber", coins);

        // Update the UI to display the retrieved coin number
        coinsShow.setText(""+coins);
    }


    private void setLetterClickListeners() {
        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            letters[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLetterClicked(index);
                    letters[index].setVisibility(View.INVISIBLE); // Hide the clicked letter
                }
            });
        }
    }

    private void onLetterClicked(int letterIndex) {
        char clickedLetter = (char) ('A' + letterIndex);

        if (currentWord != null && containsLetter(currentWord, clickedLetter)) {
            // Letter is in the word, handle visibility or other actions
            Toast.makeText(this, "Letter " + clickedLetter + " is in the word!", Toast.LENGTH_SHORT).show();
            revealLetter(clickedLetter, -1);
        } else {
            // Letter is not in the word, handle incorrect guess
            Toast.makeText(this, "Letter " + clickedLetter + " is not in the word!", Toast.LENGTH_SHORT).show();
            // Handle hangman image visibility or other actions
           if(counter < hangmanImages.length)
           {
               if(hangmanImages[0].getVisibility() == View.VISIBLE)
               {
                 hangmanImages[counter].setVisibility(View.VISIBLE);
               }
               hangmanImages[0].setImageResource(faces[counter]);
               hangmanImages[0].setVisibility(View.VISIBLE);
               counter++;

           }
           else{
               //LOSER
               Toast.makeText(this, "YOU LOST THE GAME", Toast.LENGTH_SHORT).show();
               saveCoins();

           }

        }

        Log.d("MSG3", "Letter " + clickedLetter + " clicked!");
    }

    private void saveCoins() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("coinNumber", coins);
        editor.apply();
    }

    private void revealLetter(char clickedLetter, int index) {
        // Convert the original string to a StringBuilder
        StringBuilder stringBuilder = new StringBuilder(word.getText());

        if (index == -1) {
            // Update the displayed word with the correctly guessed letter
            for (int i = 0; i < currentWord.length(); i++) {
                char currentChar = Character.toUpperCase(currentWord.charAt(i));
                if (currentChar == clickedLetter) {
                    stringBuilder.setCharAt(i * 2, clickedLetter);
                }
            }

            // Check if the word is completely revealed
            if (!containsLetter(stringBuilder.toString(), '_')) {
                Toast.makeText(this, "YOU WON!!!", Toast.LENGTH_SHORT).show();
                coins+=10;
                saveCoins();
            }
        } else if (index >= 0 && index < stringBuilder.length()) {
            // If index is within valid bounds, reveal the character at the specified position
            stringBuilder.setCharAt(index * 2, clickedLetter);
        }

        word.setText(stringBuilder.toString());
    }


    private void updateWordDisplay() {
        StringBuilder displayBuilder = new StringBuilder();

        for (int i = 0; i < currentWord.length(); i++) {
            char currentChar = currentWord.charAt(i);
            if (Character.isLetter(currentChar)) {
                displayBuilder.append("_ ");
            } else {
                displayBuilder.append(currentChar);
            }
        }

        word.setText(displayBuilder.toString().trim());
    }

    private boolean containsLetter(String word, char letter) {
        for (char c : word.toCharArray()) {
            if (Character.toUpperCase(c) == letter) {
                return true;
            }
        }
        return false;
    }

    private void initialization() {
        counter = 0;
        currentCharIndex = 0;
        bannerFrame = findViewById(R.id.bannerFrame);

        word = findViewById(R.id.WORD);
        words = new Words();

        newWord = findViewById(R.id.BTN_NEW_WORD);
        hint = findViewById(R.id.BTN_HINT);
        coinsShow = findViewById(R.id.TXT_COINS);
        options = findViewById(R.id.LBL_options);







        initializeHangmanImages();
        initializeFaces();
        initializeLetters();
        loadBanner();

    }

    private void initializeFaces() {
        faces[0] = R.drawable.happy_face_one;
        faces[1] = R.drawable.happy_face_two;
        faces[2] = R.drawable.happy_face_three;
        faces[3] = R.drawable.happy_face_four;
        faces[4] = R.drawable.happy_face_five;
        faces[5] = R.drawable.happy_face_six;
    }

    private void initializeHangmanImages() {
        hangmanImages[0] = findViewById(R.id.IMG_head);
        hangmanImages[1] = findViewById(R.id.IMG_body);
        hangmanImages[2] = findViewById(R.id.IMG_left_hand);
        hangmanImages[3] = findViewById(R.id.IMG_left_leg);
        hangmanImages[4] = findViewById(R.id.IMG_right_hand);
        hangmanImages[5] = findViewById(R.id.IMG_right_leg);

        for (ImageView image : hangmanImages) {
            image.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeLetters() {
        for (int i = 0; i < letters.length; i++) {
            int resId = getResources().getIdentifier("LET_" + (char) ('A' + i), "id", getPackageName());
            letters[i] = findViewById(resId);
            letters[i].setVisibility(View.VISIBLE);
        }
    }

    private void showRewardedVideoAdvertisment() {
        if (rewardedAd != null) {
            Activity activityContext = MainActivity.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
        }
    }
    private void loadRewardedVideoAdvertisment() {
        RewardedAd.load(MainActivity.this, "ca-app-pub-3940256099942544/5354046379",
                new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd _rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        rewardedAd = _rewardedAd;
                    }
                });
    }
}
