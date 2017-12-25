package io.github.haohaozaici.backgroundservice;

import android.app.Application;
import android.util.Log;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.SpeechSynthesis;
import io.github.haohaozaici.backgroundservice.umeng.MyPushIntentService;

/**
 * Created by haohao on 2017/8/23.
 */

public class App extends Application {

  private static final String TAG = "MyApplication";
  public SpeechSynthesis speechSynthesis;

  private PushAgent mPushAgent;

  @Override
  public void onCreate() {
    super.onCreate();

    mPushAgent = PushAgent.getInstance(this);
    registerPush();
    speechSynthesis = SpeechSynthesis.getInstance(this);

  }

  private void registerPush() {

    //注册推送服务，每次调用register方法都会回调该接口
    mPushAgent.register(new IUmengRegisterCallback() {

      @Override
      public void onSuccess(String deviceToken) {
        //注册成功会返回device token
        Log.d(TAG, "onSuccess: deviceToken = " + deviceToken);
      }

      @Override
      public void onFailure(String s, String s1) {
        registerPush();
        Log.d(TAG, "onFailure: registerPush()");
      }
    });

    //完全自定义处理设置
    mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
  }
}
