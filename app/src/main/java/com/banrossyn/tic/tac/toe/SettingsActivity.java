package com.banrossyn.tic.tac.toe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.banrossyn.tic.tac.toe.Service.Music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";

    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;

    private int revealX;
    private static final String PREFS_NAME = "vibration";
    private static final String PREF_VIBRATION = "TicVib";
    private int revealY;
    private boolean Vibration;
    private boolean isChecked;
    private String[] Randomfirst;
    Music.HomeWatcher mHomeWatcher;
    SharedPreferences sp;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Vibration = preferences.getBoolean(PREF_VIBRATION, true);

        setContentView(R.layout.activity_settings);
    sp = getSharedPreferences("music_settings", MODE_PRIVATE);
        if (!sp.getBoolean("mute_music", false)) {
            playMusic();
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        final Intent intent = getIntent();


        Switch swit = findViewById(R.id.swith2);
        swit.setChecked(Vibration);
        swit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playClick();
                if (Vibration) {
                    isChecked = false;
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_VIBRATION, isChecked);
                    editor.apply();

                } else {
                    isChecked = true;
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_VIBRATION, isChecked);
                    editor.apply();
                }
            }
        });






        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int savedValue = sharedPreferences.getInt("key", 0);


        Spinner spinner2 = (Spinner) findViewById(R.id.spinner);

        if (savedValue == 1)
            Randomfirst = new String[]{"Easy", "Random", "Medium", "Hard", "Impossible"};
        if (savedValue == 2)
            Randomfirst = new String[]{"Medium", "Random", "Easy", "Hard", "Impossible"};
        if (savedValue == 3)
            Randomfirst = new String[]{"Hard", "Random", "Easy", "Medium", "Impossible"};
        if (savedValue == 4)
            Randomfirst = new String[]{"Impossible", "Random", "Easy", "Medium", "Hard"};
        if (savedValue == 5)
            Randomfirst = new String[]{"Random", "Easy", "Medium", "Hard", "Impossible"};
        if (savedValue == 0)
            Randomfirst = new String[]{"Random", "Easy", "Medium", "Hard", "Impossible"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, Randomfirst);


        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);


        List<String> statusCheck = new ArrayList<String>();
        statusCheck = Arrays.asList("Easy");
        spinner2.setSelection(statusCheck.indexOf(1));


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (selectedItem.equals("Random")) {
                    editor.putInt("key", 5);
                }

                if (selectedItem.equals("Easy")) {
                    editor.putInt("key", 1);
                }
                if (selectedItem.equals("Medium")) {
                    editor.putInt("key", 2);
                }
                if (selectedItem.equals("Hard")) {
                    editor.putInt("key", 3);
                }
                if (selectedItem.equals("Impossible")) {
                    editor.putInt("key", 4);
                }
                editor.commit();
            }


            // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RelativeLayout r6 = findViewById(R.id.r6);
        r6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating();
                playClick();
            }
        });

        RelativeLayout r8 = findViewById(R.id.r8);
        r8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.moregamesurl))));
                playClick();
            }
        });

        RelativeLayout r7 = findViewById(R.id.r7);
        r7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedbacks();
                playClick();
            }
        });

        RelativeLayout r5 = findViewById(R.id.r5);
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDialog();
                playClick();
            }
        });

        rootLayout = findViewById(R.id.rootlay);

        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(
                        EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(
                        EXTRA_CIRCULAR_REVEAL_Y)) {

            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);

            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);
            ViewTreeObserver viewTreeObserver =
                    rootLayout.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override

                            public void onGlobalLayout() {
                                revealActivity(
                                        revealX, revealY);

                                rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });
            }
        } else {

            rootLayout.setVisibility(View.VISIBLE);
        }
    }

    protected void revealActivity(int x, int y) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

// create the animator for this view (the start radius is zero)

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(
                    new AccelerateInterpolator());

// make the view visible and start the animation

            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    protected void unRevealActivity() {


        float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
        Animator circularReveal = ViewAnimationUtils.
                createCircularReveal(

                        rootLayout, revealX, revealY, finalRadius, 0);
        circularReveal.setDuration(400);
        circularReveal.addListener(
                new AnimatorListenerAdapter() {
                    @Override

                    public void onAnimationEnd(Animator animation) {

                        rootLayout.setVisibility(View.INVISIBLE);

                        //finish Activity.
                        finish();

                    }
                });
        circularReveal.start();

    }

    private void rating() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }

    private void feedbacks() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {"banrossyn@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Classic Tic Tac Toe Feedback");
        intent.putExtra(Intent.EXTRA_CC, "banrossyn@gmail.com");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }

    private void settingsDialog() {


        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.setContentView(R.layout.setting);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        final RadioGroup rgMusic = dialog.findViewById(R.id.radiogroup_music_select);
        final RadioButton musicOn = dialog.findViewById(R.id.music_on);
        final RadioButton musicOff = dialog.findViewById(R.id.music_off);


        if (!sp.getBoolean("mute_music", false)) {
            rgMusic.check(musicOn.getId());
            musicOn.setTextColor(Color.rgb(90, 85, 83));
        } else {
            rgMusic.check(musicOff.getId());
            musicOff.setTextColor(Color.rgb(90, 85, 83));
        }


        rgMusic.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                playClick();
                if (checkedId == musicOn.getId()) {
                    musicOn.setTextColor(Color.rgb(90, 85, 83));
                    musicOff.setTextColor(Color.rgb(167, 168, 168));
                    if (mServ != null) {
                        mServ.startMusic();
                    } else {
                        playMusic();
                    }
                    sp.edit().putBoolean("mute_music", false).apply();

                } else {
                    musicOff.setTextColor(Color.rgb(90, 85, 83));
                    musicOn.setTextColor(Color.rgb(167, 168, 168));
                    if (mServ != null) {
                        mServ.stopMusic();
                    }
                    sp.edit().putBoolean("mute_music", true).apply();

                }
            }
        });


        RadioGroup rgSound = dialog.findViewById(R.id.radiogroup_sound_select);
        final RadioButton soundOn = dialog.findViewById(R.id.sound_on);
        final RadioButton soundOff = dialog.findViewById(R.id.sound_off);

        if (!sp.getBoolean("mute_sounds", false)) {
            rgSound.check(soundOn.getId());
            soundOn.setTextColor(Color.rgb(90, 85, 83));
        } else {
            rgSound.check(soundOff.getId());
            soundOff.setTextColor(Color.rgb(90, 85, 83));
        }


        rgSound.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == soundOn.getId()) {
                    sp = getSharedPreferences("music_settings", MODE_PRIVATE);
                    sp.edit().putBoolean("mute_sounds", false).apply();
                    playClick();
                    soundOn.setTextColor(Color.rgb(90, 85, 83));
                    soundOff.setTextColor(Color.rgb(167, 168, 168));

                } else {
                    sp.edit().putBoolean("mute_sounds", true).apply();
                    soundOff.setTextColor(Color.rgb(90, 85, 83));
                    soundOn.setTextColor(Color.rgb(167, 168, 168));
                }
            }
        });


        Button closeBtn = dialog.findViewById(R.id.close_button);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClick();
                dialog.dismiss();


            }
        });

    }
    @Override
    public void onBackPressed() {
        unRevealActivity();
    }

    private void playClick() {
        SharedPreferences sp = getSharedPreferences("music_settings", MODE_PRIVATE);
        final MediaPlayer click = MediaPlayer.create(SettingsActivity.this, R.raw.click);
        if (!sp.getBoolean("mute_sounds", false)) {
            click.start();
        }
    }

    private void playMusic() {
        doBindService();
        Intent music = new Intent();
        music.setClass(this, Music.class);
        startService(music);

        mHomeWatcher = new Music.HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new Music.HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();

    }

    private boolean mIsBound = false;
    private Music mServ;
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((Music.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, Music.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }

    }

    @Override
    public void onPause() {

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isInteractive();
        }
        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, Music.class);
        stopService(music);
        super.onDestroy();
    }

}
