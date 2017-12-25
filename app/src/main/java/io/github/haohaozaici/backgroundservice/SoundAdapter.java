package io.github.haohaozaici.backgroundservice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.haohaozaici.backgroundservice.SoundAdapter.SoundViewHolder;
import io.github.haohaozaici.backgroundservice.voicetoplay.Sound;
import io.github.haohaozaici.backgroundservice.voicetoplay.SpeechSynthesis;
import java.util.List;

/**
 * Created by haohao on 2017/8/24.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundViewHolder> {

  private Context context;
  private List<Sound> mSounds;

  private SpeechSynthesis speechSynthesis;


  public SoundAdapter(Context context, List<Sound> sounds, SpeechSynthesis speechSynthesis) {
    this.context = context;
    this.mSounds = sounds;
    this.speechSynthesis = speechSynthesis;

  }

  @Override
  public SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.sound_item_layout, parent, false);
    return new SoundViewHolder(view);
  }

  @Override
  public void onBindViewHolder(SoundViewHolder holder, int position) {
    holder.bindSound(mSounds.get(position));
  }

  @Override
  public int getItemCount() {
    return mSounds.size();
  }

  public class SoundViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.sound_name)
    TextView sound_name;
    private Sound mSound;

    public SoundViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

    }

    public void bindSound(Sound sound) {
      mSound = sound;
      sound_name.setText(mSound.getmName());
      sound_name.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
//          speechSynthesis.play(mSound);
        }
      });
    }
  }

}
