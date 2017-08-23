package io.github.haohaozaici.backgroundservice.VoiceToPlay;

/**
 * Created by haohao on 2017/8/23.
 */

public class Sound {

  private String mAssetPath;
  private String mName;
  private Integer mSoundId;

  public Sound(String assetPath) {
    mAssetPath = assetPath;
    String[] components = assetPath.split("/");
    String filename = components[components.length - 1];
    mName = filename.replace(".mp3", "");
  }

  public String getmAssetPath() {
    return mAssetPath;
  }

  public void setmAssetPath(String mAssetPath) {
    this.mAssetPath = mAssetPath;
  }

  public String getmName() {
    return mName;
  }

  public void setmName(String mName) {
    this.mName = mName;
  }

  public Integer getmSoundId() {
    return mSoundId;
  }

  public void setmSoundId(Integer mSoundId) {
    this.mSoundId = mSoundId;
  }
}
