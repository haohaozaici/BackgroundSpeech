package io.github.haohaozaici.backgroundservice.umeng;

import android.content.Context;
import android.content.Intent;
import com.umeng.message.UmengMessageService;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;
import io.github.haohaozaici.backgroundservice.VoiceToPlay.SpeechSynthesis;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

/**
 * Developer defined push intent service.
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 *
 * @author lucas
 */
//完全自定义处理类
public class MyPushIntentService extends UmengMessageService {

  private static final String TAG = MyPushIntentService.class.getName();
  private SpeechSynthesis speechSynthesis;

  @Override
  public void onMessage(Context context, Intent intent) {
    if (speechSynthesis == null) {
      speechSynthesis = SpeechSynthesis.getInstance(this);
    }
    try {
      //可以通过MESSAGE_BODY取得消息体
      String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
      UMessage msg = new UMessage(new JSONObject(message));
      UmLog.d(TAG, "message=" + message);    //消息体
      UmLog.d(TAG, "custom=" + msg.custom);    //自定义消息的内容
      UmLog.d(TAG, "title=" + msg.title);    //通知标题
      UmLog.d(TAG, "text=" + msg.text);    //通知内容
      // code  to handle message here
      // ...

      final String text = msg.text;
//      new Handler().post(new Runnable() {
//        @Override
//        public void run() {
//          String2Voice.Money2Voice(MyPushIntentService.this, text, App.speechSynthesis);
//
//        }
//      });

      Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
          speechSynthesis.money2Voice(Integer.parseInt(text));
        }
      }).subscribeOn(Schedulers.single())
          .subscribe();

//      new Thread(new Runnable() {
//        @Override
//        public void run() {
//          String2Voice.Money2Voice(MyPushIntentService.this, text, speechSynthesis);
//
//        }
//      }).run();

//      String2Voice.Money2Voice(this, msg.text, App.speechSynthesis);

    } catch (Exception e) {
      UmLog.e(TAG, e.getMessage());
    }
  }
}
