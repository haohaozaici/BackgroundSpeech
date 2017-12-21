package io.github.haohaozaici.backgroundservice;

import static io.github.haohaozaici.backgroundservice.App.speechSynthesis;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.umeng.message.PushAgent;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.Sound;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.SpeechSynthesis;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.String2Voice;
import io.github.haohaozaici.backgroundservice.umeng.MyPushIntentService;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @BindView(R.id.restartApp)
  Button restartApp;
  @BindView(R.id.play_sound)
  Button play_sound;
  @BindView(R.id.play_money)
  Button play_money;
  @BindView(R.id.sound_list)
  RecyclerView sound_list;
  @BindView(R.id.input_money) EditText input_money;
  @BindView(R.id.hint_text) TextView hint_text;

  private SpeechSynthesis speechSynthesis;

  String money;

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

    play_money.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (money != null && !money.equals("")) {

          if (!money.contains(".")) {
            Observable.create(new ObservableOnSubscribe<String>() {
              @Override
              public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                try {
                  String2Voice.Money2Voice(Integer.parseInt(money), speechSynthesis);
                } catch (IllegalArgumentException excep) {
                  Log.w(TAG, "IllegalArgumentException: " + excep.getMessage());
                  e.onNext(excep.getMessage());
                }
              }
            })
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                  @Override
                  public void onSubscribe(Disposable d) {

                  }

                  @Override
                  public void onNext(String s) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onError(Throwable e) {

                  }

                  @Override
                  public void onComplete() {

                  }
                });

          } else {
            Toast.makeText(MainActivity.this, "单位为分，不能包含小数点", Toast.LENGTH_SHORT).show();
          }

        } else {
          Toast.makeText(MainActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
      }
    });

    input_money.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        money = s.toString();
        if (money != null && !money.equals("")) {
          if (Long.parseLong(money) > 999999999) {
            Toast.makeText(MainActivity.this, "金额过大", Toast.LENGTH_SHORT).show();
          } else {
            hint_text.setText(String2Voice.formatMoney(Integer.parseInt(money)) + " 元");
          }
        } else {
          hint_text.setText("");
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });


  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    speechSynthesis.release();
  }
}
