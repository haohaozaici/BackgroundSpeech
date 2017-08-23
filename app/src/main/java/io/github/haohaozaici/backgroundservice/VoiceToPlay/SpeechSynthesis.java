package io.github.haohaozaici.backgroundservice.VoiceToPlay;

import static android.media.AudioAttributes.CONTENT_TYPE_SPEECH;
import static android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED;
import static android.media.AudioAttributes.USAGE_NOTIFICATION_EVENT;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
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

  private AssetManager assetManager;
  private SoundPool soundPool;

  List<Sound> sounds = new ArrayList<>();

  public SpeechSynthesis(Context context) {
    assetManager = context.getAssets();

    SoundPool.Builder builder = new Builder();
    AudioAttributes.Builder audioBuilder = new AudioAttributes.Builder();
    audioBuilder.setContentType(CONTENT_TYPE_SPEECH)
        .setFlags(FLAG_AUDIBILITY_ENFORCED)
        .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
        .setUsage(USAGE_NOTIFICATION_EVENT);

    soundPool = builder.setAudioAttributes(audioBuilder.build())
        .setMaxStreams(10)
        .build();

    loadSounds();

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
    return soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
  }

  public void release() {
    soundPool.release();
  }


}
