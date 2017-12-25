package io.github.haohaozaici.backgroundservice.VoiceToPlay;

import static io.github.haohaozaici.backgroundservice.VoiceToPlay.String2Voice.int2Money;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haohao on 2017/8/23.
 */

public class SpeechSynthesis {

  private static SpeechSynthesis sSpeechSynthesis;

  private SpeechSynthesis(Context context) {
    assetManager = context.getAssets();
    createSoundPool();
    loadSounds();
  }

  public static synchronized SpeechSynthesis getInstance(Context context) {
    if (sSpeechSynthesis == null) {
      sSpeechSynthesis = new SpeechSynthesis(context);
    }
    return sSpeechSynthesis;
  }

  private static final String TAG = "SpeechSynthesis";
  private static final String SOUNDS_FOLDER = "tts";

  private static final float VOLUME = 1.0f;   //播放音量
  private static final int MAX_STREAMS = 1;   //同时播放音频数量
  private static final long PLAY_SPEED = 520; //millis 金额播放间隔

  private AssetManager assetManager;
  private SoundPool soundPool;

  private List<Sound> sounds = new ArrayList<>();
  private Map<String, Sound> mSoundMap = new HashMap<>();  //声音对象容器，key = 文件名

  private static final String zero = "零",
      one = "壹",
      two = "贰",
      three = "叁",
      four = "肆",
      five = "伍",
      six = "陆",
      seven = "柒",
      eight = "捌",
      nine = "玖";

  private static final String yuan = "元",
      zheng = "整",
      shi = "拾",
      bai = "佰",
      qian = "仟",
      wan = "万",
      yi = "亿",
      dot = "点";


  private void createSoundPool() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      createNewSoundPool();
    } else {
      createOldSoundPool();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void createNewSoundPool() {
    AudioAttributes attributes = new AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
        //        .setFlags(FLAG_AUDIBILITY_ENFORCED)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build();
    soundPool = new SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(MAX_STREAMS)
        .build();
  }

  @SuppressWarnings("deprecation")
  private void createOldSoundPool() {
    soundPool = new SoundPool(11, AudioManager.STREAM_NOTIFICATION, 0);
  }

  private void loadSounds() {
    String[] soundNames;
    try {
      soundNames = assetManager.list(SOUNDS_FOLDER);
      Log.i(TAG, "Found " + soundNames.length + " sounds");
    } catch (IOException ioe) {
      Log.e(TAG, "Could not list assets", ioe);
      return;
    }

    for (String filename : soundNames) {
      try {
        String assetPath = SOUNDS_FOLDER + "/" + filename;
        Sound sound = new Sound(assetPath);
        load(sound);
        sounds.add(sound);
        mSoundMap.put(sound.getmName(), sound);
      } catch (IOException ioe) {
        Log.e(TAG, "Could not load sound " + filename, ioe);
      }
    }

  }

  private void load(Sound sound) throws IOException {
    AssetFileDescriptor afd = assetManager.openFd(sound.getmAssetPath());
    int soundId = soundPool.load(afd, 1);
    sound.setmSoundId(soundId);
  }


  private int play(Sound sound) {
    Integer soundId = sound.getmSoundId();
    if (soundId == null) {
      Log.d(TAG, "play: error");
      return 0;
    }
    Log.d(TAG, "play: success");
    return soundPool.play(soundId, VOLUME, VOLUME, 1, 0, 1.0f);
  }

  public void release() {
    soundPool.release();
  }

  public void money2Voice(int money) throws IllegalArgumentException {
    String text = int2Money(money);
    Log.d("MainActivity", "Money2Voice: " + text);

    try {
      // TODO: 2017/12/25 播放首部
      play(mSoundMap.get("tts_success"));
      Thread.sleep(1350);

      //金额播报
      char[] chars = text.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        switch (chars[i] + "") {
          case zero:
            play(mSoundMap.get("tts_0"));
            break;
          case one:
            play(mSoundMap.get("tts_1"));
            break;
          case two:
            play(mSoundMap.get("tts_2"));
            break;
          case three:
            play(mSoundMap.get("tts_3"));
            break;
          case four:
            play(mSoundMap.get("tts_4"));
            break;
          case five:
            play(mSoundMap.get("tts_5"));
            break;
          case six:
            play(mSoundMap.get("tts_6"));
            break;
          case seven:
            play(mSoundMap.get("tts_7"));
            break;
          case eight:
            play(mSoundMap.get("tts_8"));
            break;
          case nine:
            play(mSoundMap.get("tts_9"));
            break;
          case yuan:
            play(mSoundMap.get("tts_yuan"));
            break;
          case shi:
            play(mSoundMap.get("tts_ten"));
            break;
          case bai:
            play(mSoundMap.get("tts_hundred"));
            break;
          case qian:
            play(mSoundMap.get("tts_thousand"));
            break;
          case wan:
            play(mSoundMap.get("tts_ten_thousand"));
            break;
          case yi:
            play(mSoundMap.get("tts_ten_million"));
            break;
          case dot:
            play(mSoundMap.get("tts_dot"));
            break;
          default:
            break;
        }
        Thread.sleep(PLAY_SPEED);
      }
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

}
