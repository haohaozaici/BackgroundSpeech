package io.github.haohaozaici.backgroundservice;

import static org.junit.Assert.assertEquals;

import io.github.haohaozaici.backgroundservice.voicetoplay.String2Voice;
import java.text.DecimalFormat;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  @Test
  public void addition_isCorrect() throws Exception {
    assertEquals(4, 2 + 2);
  }


  @Test
  public void money2Voice() {

    int[] money = {
        /*0,*/ 1, 6,
        10, 11, 16, 26, 36, 99,
        100, 101, 166, 999,
        1000, 1001, 1010, 1100, 1110, 1111,
        10000, 10001, 10011, 10111, 11111, 11101,
        110000000
    };

    for (int i : money) {

      System.out.println("输入: " + i + "->" + formatMoney(i) + "-->" + String2Voice.int2Money(i));

    }

  }

  public static String formatMoney(int money) {

    return formatTotalMoneyToShow(money + "");
  }


  public static String formatTotalMoneyToShow(String money) {
    Double d = Double.parseDouble(money);
    d /= 100;

    DecimalFormat df = new DecimalFormat("#########0.00");
    return df.format(d);
  }

}