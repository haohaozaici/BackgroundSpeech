package io.github.haohaozaici.backgroundservice.VoiceToPlay;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by haohao on 2017/8/24.
 */

public class String2Voice {

  private static final Pattern AMOUNT_PATTERN =
      Pattern.compile("^(0|[1-9]\\d{0,11})\\.(\\d\\d)$"); // 不考虑分隔符的正确性
  private static final char[] RMB_NUMS = "零壹贰叁肆伍陆柒捌玖".toCharArray();
  private static final String[] UNITS = {"元", "角", "分", "整"};
  private static final String[] U1 = {"", "拾", "佰", "仟"};
  private static final String[] U2 = {"", "万", "亿"};

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


  /**
   * 将金额（整数部分等于或少于12位，小数部分2位）转换为中文大写形式.
   *
   * @param amount 金额数字
   * @return 中文大写
   */
  public static String convert(String amount) throws IllegalArgumentException {
    // 去掉分隔符
//    amount = amount.replace(",", "");

    // 验证金额正确性
    if (amount.equals("0.00")) {
      throw new IllegalArgumentException("金额不能为零.");
    }
    Matcher matcher = AMOUNT_PATTERN.matcher(amount);
    if (!matcher.find()) {
      throw new IllegalArgumentException("输入金额有误.");
    }

    String integer = matcher.group(1); // 整数部分
    String fraction = matcher.group(2); // 小数部分

    String result_integer = "";
    String result_fraction = "";
    if (!integer.equals("0")) {
      result_integer += integer2rmb(integer) + UNITS[0]; // 整数部分
    } else {
      result_integer += RMB_NUMS[0];
    }
//    if (fraction.equals("00")) {
//      result += UNITS[3]; // 添加[整]
//    } else if (fraction.startsWith("0") && integer.equals("0")) {
//      result += fraction2rmb(fraction).substring(1); // 去掉分前面的[零]
//    } else {
//      result += fraction2rmb(fraction); // 小数部分
//    }
    if (fraction.equals("00")) {
    } else {
      result_integer = result_integer.replace("元", "");
      result_fraction += "点";
      result_fraction += fraction2rmb(fraction); // 小数部分
    }

    String result = result_integer + result_fraction;
    if (result.contains("点")) {
      result += "元";
    }

    return result;
  }

  // 将金额小数部分转换为中文大写
  private static String fraction2rmb(String fraction) {
    char jiao = fraction.charAt(0); // 角
    char fen = fraction.charAt(1); // 分
//    return (RMB_NUMS[jiao - '0'] + (jiao > '0' ? UNITS[1] : ""))
//        + (fen > '0' ? RMB_NUMS[fen - '0'] + UNITS[2] : "");
    return (RMB_NUMS[jiao - '0'])
        + (fen > '0' ? RMB_NUMS[fen - '0'] + "" : "");
  }

  // 将金额整数部分转换为中文大写
  private static String integer2rmb(String integer) {
    StringBuilder buffer = new StringBuilder();
    // 从个位数开始转换
    int i, j;
    for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
      char n = integer.charAt(i);
      if (n == '0') {
        // 当n是0且n的右边一位不是0时，插入[零]
        if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
          buffer.append(RMB_NUMS[0]);
        }
        // 插入[万]或者[亿]
        if (j % 4 == 0) {
          if (i > 0 && integer.charAt(i - 1) != '0'
              || i > 1 && integer.charAt(i - 2) != '0'
              || i > 2 && integer.charAt(i - 3) != '0') {
            buffer.append(U2[j / 4]);
          }
        }
      } else {
        if (j % 4 == 0) {
          buffer.append(U2[j / 4]);     // 插入[万]或者[亿]
        }
        buffer.append(U1[j % 4]);         // 插入[拾]、[佰]或[仟]
        buffer.append(RMB_NUMS[n - '0']); // 插入数字
      }
    }
    return buffer.reverse().toString();
  }


  public static void Money2Voice(Context context, String money, SpeechSynthesis speechSynthesis) {
    List<Sound> sounds = speechSynthesis.getSounds();
    String text = convert(money);

    Log.d("MainActivity", "Money2Voice: " + text);

    try {
      speechSynthesis.play(sounds.get(12));
      Thread.sleep(1200);
      char[] chars = text.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        switch (chars[i] + "") {
          case zero:
            speechSynthesis.play(sounds.get(0));
            break;
          case one:
            speechSynthesis.play(sounds.get(1));
            break;
          case two:
            speechSynthesis.play(sounds.get(2));
            break;
          case three:
            speechSynthesis.play(sounds.get(3));
            break;
          case four:
            speechSynthesis.play(sounds.get(4));
            break;
          case five:
            speechSynthesis.play(sounds.get(5));
            break;
          case six:
            speechSynthesis.play(sounds.get(6));
            break;
          case seven:
            speechSynthesis.play(sounds.get(7));
            break;
          case eight:
            speechSynthesis.play(sounds.get(8));
            break;
          case nine:
            speechSynthesis.play(sounds.get(9));
            break;
          case yuan:
            speechSynthesis.play(sounds.get(17));
            break;
          case shi:
            speechSynthesis.play(sounds.get(13));
            break;
          case bai:
            speechSynthesis.play(sounds.get(11));
            break;
          case qian:
            speechSynthesis.play(sounds.get(16));
            break;
          case wan:
            speechSynthesis.play(sounds.get(15));
            break;
          case yi:
            speechSynthesis.play(sounds.get(14));
            break;
          case dot:
            speechSynthesis.play(sounds.get(10));
            break;
          default:
            break;
        }
        Thread.sleep(550);
      }
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }


  }
}
