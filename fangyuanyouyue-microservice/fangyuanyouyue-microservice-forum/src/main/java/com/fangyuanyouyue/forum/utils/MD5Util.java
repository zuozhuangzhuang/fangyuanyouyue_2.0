package com.fangyuanyouyue.forum.utils;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5Util
{
  private static Logger logger = Logger.getLogger(MD5Util.class);
  protected static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', 
    '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  protected static MessageDigest messagedigest = null;

  static {
    try { messagedigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      logger.error("MD5FileUtil messagedigest初始化失败", e);
    }
  }

  public static String getFileMD5String(File file) throws IOException {
    FileInputStream in = new FileInputStream(file);
    FileChannel ch = in.getChannel();
    MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0L,
      file.length());
    messagedigest.update(byteBuffer);
    in.close();
    return bufferToHex(messagedigest.digest());
  }

  public static String getMD5String(String s) {
    return getMD5String(s.getBytes());
  }

  public static String getMD5String(byte[] bytes) {
    messagedigest.update(bytes);
    return bufferToHex(messagedigest.digest());
  }

  private static String bufferToHex(byte[] bytes) {
    return bufferToHex(bytes, 0, bytes.length);
  }

  private static String bufferToHex(byte[] bytes, int m, int n) {
    StringBuffer stringbuffer = new StringBuffer(2 * n);
    int k = m + n;
    for (int l = m; l < k; l++) {
      appendHexPair(bytes[l], stringbuffer);
    }
    return stringbuffer.toString();
  }

  private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
    char c0 = hexDigits[((bt & 0xF0) >> 4)];
    char c1 = hexDigits[(bt & 0xF)];
    stringbuffer.append(c0);
    stringbuffer.append(c1);
  }

  public static boolean checkPassword(String password, String md5PwdStr) {
    String s = getMD5String(password);
    return s.equals(md5PwdStr);
  }

  public static void main(String[] args) throws IOException {
//    long begin = System.currentTimeMillis();
//
//    String md5 = getMD5String("e10adc3949ba59abbe56e057f20f883e");
////    String md5 = getMD5String("123456");
//
//    long end = System.currentTimeMillis();
//    System.out.println("md5:" + md5);
//    System.out.println("time:" + (end - begin) / 1000L + "s");

    // 原文
//    String plaintext = "DingSai";
    String plaintext = "123456";
//    String plaintext = "6aef10831583974a9fa639925bb3cc21";
    System.out.println("原始：" + plaintext);
    System.out.println("普通MD5后：" + MD5Util.MD5(plaintext));

    // 获取加盐后的MD5值
    String ciphertext = MD5Util.generate(MD5Util.MD5(plaintext));
    System.out.println("加盐后MD5：" + ciphertext);
    System.out.println("是否是同一字符串:" + MD5Util.verify(plaintext, ciphertext));
//    System.out.println("是否是同一字符串:" + MD5Util.verify("123456", "d5520ad2ad0a249d7ff9044407e20233fb78325293d65f6b"));
//    /**
//     * 其中某次DingSai字符串的MD5值
//     */
//    String[] tempSalt = { "c4d980d6905a646d27c0c437b1f046d4207aa2396df6af86", "66db82d9da2e35c95416471a147d12e46925d38e1185c043", "61a718e4c15d914504a41d95230087a51816632183732b5a" };
//
//    for (String temp : tempSalt) {
//      System.out.println("是否是同一字符串:" + MD5Util.verify(plaintext, temp));
//    }
  }

  /**
   * 普通MD5
   * @param input
   * @return
   */
  public static String MD5(String input) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      return "check jdk";
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
    char[] charArray = input.toCharArray();
    byte[] byteArray = new byte[charArray.length];

    for (int i = 0; i < charArray.length; i++)
      byteArray[i] = (byte) charArray[i];
    byte[] md5Bytes = md5.digest(byteArray);
    StringBuffer hexValue = new StringBuffer();
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16)
        hexValue.append("0");
      hexValue.append(Integer.toHexString(val));
    }
    return hexValue.toString();
  }


  /**
   * 加盐MD5
   * @param password
   * @return
   */
  public static String generate(String password) {
    Random r = new Random();
    StringBuilder sb = new StringBuilder(16);
    sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
    int len = sb.length();
    if (len < 16) {
      for (int i = 0; i < 16 - len; i++) {
        sb.append("0");
      }
    }
    String salt = sb.toString();
    System.out.println("salt:"+salt);
    password = md5Hex(password + salt);
    char[] cs = new char[48];
    for (int i = 0; i < 48; i += 3) {
      cs[i] = password.charAt(i / 3 * 2);
      char c = salt.charAt(i / 3);
      cs[i + 1] = c;
      cs[i + 2] = password.charAt(i / 3 * 2 + 1);
    }
    return new String(cs);
  }

  /**
   * 校验加盐后是否和原文一致
   * @param password
   * @param md5
   * @return
   */
  public static boolean verify(String password, String md5) {
    char[] cs1 = new char[32];
    char[] cs2 = new char[16];
    for (int i = 0; i < 48; i += 3) {
      cs1[i / 3 * 2] = md5.charAt(i);
      cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
      cs2[i / 3] = md5.charAt(i + 1);
    }
    String salt = new String(cs2);
    return md5Hex(password + salt).equals(new String(cs1));
  }
  /**
   * 获取十六进制字符串形式的MD5摘要
   */
  private static String md5Hex(String src) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] bs = md5.digest(src.getBytes());
      return new String(new Hex().encode(bs));
    } catch (Exception e) {
      return null;
    }
  }


}
