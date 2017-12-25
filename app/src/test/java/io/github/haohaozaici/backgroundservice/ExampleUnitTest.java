package io.github.haohaozaici.backgroundservice;

import static org.junit.Assert.assertEquals;

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