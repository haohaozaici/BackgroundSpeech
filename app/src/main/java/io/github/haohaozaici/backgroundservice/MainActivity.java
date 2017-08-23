package io.github.haohaozaici.backgroundservice;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.umeng.message.PushAgent;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.Sound;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.SpeechSynthesis;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.String2Voice;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @BindView(R.id.restartApp)
  Button restartApp;
  @BindView(R.id.play_sound)
  Button play_sound;
  @BindView(R.id.sound_list)
  RecyclerView sound_list;

  private SpeechSynthesis speechSynthesis;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    /*permission check*/
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_DENIED) {
      ActivityCompat
          .requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    if (ContextCompat.checkSelfPermission(this, permission.READ_PHONE_STATE)
        == PackageManager.PERMISSION_DENIED) {
      ActivityCompat
          .requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
    }
    PushAgent.getInstance(this).onAppStart();

    restartApp.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        ProcessPhoenix.triggerRebirth(MainActivity.this);
      }
    });

    speechSynthesis = new SpeechSynthesis(this);
    sound_list.setLayoutManager(new LinearLayoutManager(this));
    sound_list.setAdapter(new SoundAdapter(this, speechSynthesis.getSounds(), speechSynthesis));

    play_sound.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        for (Sound sound : speechSynthesis.getSounds()) {
          speechSynthesis.play(sound);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ie) {
            ie.printStackTrace();
          }
        }
      }
    });

    Log.v(TAG, "convert(\"0.01\"): "+ String2Voice.convert("0.01"));
    Log.v(TAG, "convert(\"0.11\"): "+ String2Voice.convert("0.11"));
    Log.v(TAG, "convert(\"100.00\"): "+ String2Voice.convert("100.00"));
    Log.v(TAG, "convert(\"101.00\"): "+ String2Voice.convert("101.00"));
    Log.v(TAG, "convert(\"10101.00\"): "+ String2Voice.convert("10101.00"));
    Log.v(TAG, "convert(\"10101.00\"): "+ String2Voice.convert("12345.89"));
    Log.v(TAG, "convert(\"1234567.89\"): "+ String2Voice.convert("1234567.89"));
    Log.v(TAG, "convert(\"1234567.09\"): "+ String2Voice.convert("1234567.09"));

  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    speechSynthesis.release();
  }
}
