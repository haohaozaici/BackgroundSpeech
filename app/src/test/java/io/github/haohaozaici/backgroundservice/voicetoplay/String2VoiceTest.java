package io.github.haohaozaici.backgroundservice.voicetoplay;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by haohao on 2017/12/25.
 */
public class String2VoiceTest {

  @Test
  public void int2Money() throws Exception {

    int[] money = {
        /*0,*/ 1, 6,
        10, 11, 16, 26, 36, 99,
        100, 101, 166, 999,
        1000, 1001, 1010, 1100, 1110, 1111,
        10000, 10001, 10011, 10111, 11111, 11101,
        110000000
    };

    for (int i : money) {

      System.out.println(
          "输入: " + i + "->" + String2Voice.formatMoney(i) + "-->" + String2Voice.int2Money(i));

    }
  }


}