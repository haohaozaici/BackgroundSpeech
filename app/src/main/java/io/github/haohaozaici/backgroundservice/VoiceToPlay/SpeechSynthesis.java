package io.github.haohaozaici.backgroundservice.VoiceToPlay;

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
import java.util.List;

/**
 * Created by haohao on 2017/8/23.
 */

public class SpeechSynthesis {

  private static final String TAG = "SpeechSynthesis";
  public static final String SOUNDS_FOLDER = "tts";

  private static final float VOLUME = 1.0f;

  private AssetManager assetManager;
  private SoundPool soundPool;

  List<Sound> sounds = new ArrayList<>();

  public SpeechSynthesis(Context context) {
    assetManager = context.getAssets();

    createSoundPool();

    loadSounds();

  }

  protected void createSoundPool() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      createNewSoundPool();
    } else {
      createOldSoundPool();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  protected void createNewSoundPool(){
    AudioAttributes attributes = new AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
        //        .setFlags(FLAG_AUDIBILITY_ENFORCED)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build();
    soundPool = new SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(11)
        .build();
  }

  @SuppressWarnings("deprecation")
  protected void createOldSoundPool(){
    soundPool = new SoundPool(11,AudioManager.STREAM_NOTIFICATION,0);
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

  public List<Sound> getSounds() {
    return sounds;
  }

  public int play(Sound sound) {
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


}
