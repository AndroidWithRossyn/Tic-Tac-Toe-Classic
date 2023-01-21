package com.banrossyn.tic.tac.toe;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.banrossyn.tic.tac.toe.Service.Music;

import java.util.Timer;
import java.util.TimerTask;

public class ChooseActivity extends AppCompatActivity {

    CharSequence player1 = "Player 1";
    CharSequence player2 = "Player 2";
    public boolean selectedsingleplayer;
    Music.HomeWatcher mHomeWatcher;

    boolean player1ax;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        SharedPreferences sp = getSharedPreferences("music_settings", MODE_PRIVATE);
        if (!sp.getBoolean("mute_music", false)) {
            playMusic();
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }







        CharSequence[] players = getIntent().getCharSequenceArrayExtra("playersnames");
        player1 = players[0];
        player2 = players[1];

        TextView textView = findViewById(R.id.text3);

        selectedsingleplayer = getIntent().getBooleanExtra("selectedsingleplayer", true);
        if (!selectedsingleplayer){
            textView.setText(player1 +" pick your side");



        }


        final ImageView imageView = findViewById(R.id.imageView);
        final ImageView imageView2 = findViewById(R.id.imageView2);
        imageView.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        imageView2.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
        final RadioButton r1 = findViewById(R.id.player1o);
        final RadioButton r2 = findViewById(R.id.player1x);
        final int textColor = Color.parseColor("#e5e9ea");
        final int textColorBlue = Color.parseColor("#3b7df8");
        r1.post(new Runnable() {
            @Override
            public void run() {
                if (r1.isChecked()) {
                    r1.setButtonTintList(ColorStateList.valueOf(textColorBlue));


                } else {
                    r1.setButtonTintList(ColorStateList.valueOf(textColor));
                }
                r1.postDelayed(this, 10);
            }
        });

        r2.post(new Runnable() {
            @Override
            public void run() {
                if (r2.isChecked()) {
                    r2.setButtonTintList(ColorStateList.valueOf(textColorBlue));
                } else {
                    r2.setButtonTintList(ColorStateList.valueOf(textColor));

                }
                r2.postDelayed(this, 10);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2.setChecked(false);
                r1.setChecked(true);

                imageView2.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
                player1ax = false;
                imageView2.setImageResource(R.drawable.oo);
                imageView.setImageResource(R.drawable.xxsh);
                imageView.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));

                playClick();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                r2.setChecked(true);
                player1ax = true;
                imageView.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
                imageView2.setImageResource(R.drawable.ooh);
                imageView.setImageResource(R.drawable.xxs);
                imageView2.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
                playClick();
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2.setChecked(false);
                player1ax = false;
                imageView2.setImageResource(R.drawable.oo);
                imageView.setImageResource(R.drawable.xxsh);
                imageView2.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
                imageView.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));

                playClick();
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(false);
                player1ax = true;
                imageView2.setImageResource(R.drawable.ooh);
                imageView.setImageResource(R.drawable.xxs);
                imageView.setColorFilter(getApplicationContext().getResources().getColor(R.color.transparent));
                imageView2.setColorFilter(getApplicationContext().getResources().getColor(R.color.tint2));
                playClick();
            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                if (r1.isChecked() || r2.isChecked()) {

                    Button ds = findViewById(R.id.button);
                    ds.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence[] players1 = getIntent().getCharSequenceArrayExtra("playersnames");
                            player1 = players1[0];
                            player2 = players1[1];


                            Intent i = new Intent(ChooseActivity.this, SceneActivity.class);
                            CharSequence[] players = {player1, player2};
                            i.putExtra("playersnames", players);
                            i.putExtra("player1ax", player1ax);
                            i.putExtra("selectedsingleplayer", selectedsingleplayer);
                            startActivity(i);
                        }
                    });
                }
            }


        }, 0, 20);//put here time 1000 milliseconds = 1 second


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChooseActivity.this, NameActivity.class);
        startActivity(intent);
    }



    private void playClick() {
        SharedPreferences sp = getSharedPreferences("music_settings", MODE_PRIVATE);
        final MediaPlayer click = MediaPlayer.create(ChooseActivity.this, R.raw.click);
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
