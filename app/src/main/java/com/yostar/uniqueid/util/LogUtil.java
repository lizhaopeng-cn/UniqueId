package com.yostar.uniqueid.util;

import android.util.Log;

public class LogUtil {
  public static boolean OPEN_LOG = true;
  public static boolean DEBUG = true;
  private static String tag = "UniqueID";
  private static LogUtil log;
  private String mClassName;

  private LogUtil(String name) {
    this.mClassName = name;
  }

  public static void i(Object str) {
    print(4, str);
  }

  public static void d(Object str) {
    print(3, str);
  }

  public static void v(Object str) {
    print(2, str);
  }

  public static void w(Object str) {
    print(5, str);
  }

  public static void e(Object str) {
    print(6, str);
  }

  public static void printLog(Object str) {
    print(6, "=====================================");
    print(6, str);
    print(6, "=====================================");
  }

  private static void print(int index, Object str) {
    if (!OPEN_LOG) {
      return;
    }
    if (log == null) {
      log = new LogUtil("[UniqueID]");
    }
    String name = log.getFunctionName();
    if (name != null) {
      str = name + "  -  | " + str;
    }
    if ((!DEBUG) && (index <= 3)) {
      return;
    }
    switch (index) {
      case 2:
        Log.v(tag, str.toString());
        break;
      case 3:
        Log.d(tag, str.toString());
        break;
      case 4:
        Log.i(tag, str.toString());
        break;
      case 5:
        Log.w(tag, str.toString());
        break;
      case 6:
        Log.e(tag, str.toString());
    }
  }

  private String getFunctionName() {
    StackTraceElement[] sts = Thread.currentThread().getStackTrace();
    if (sts == null) {
      return null;
    }
    StackTraceElement[] arrayOfStackTraceElement1;
    int j = (arrayOfStackTraceElement1 = sts).length;
    for (int i = 0; i < j; i++) {
      StackTraceElement st = arrayOfStackTraceElement1[i];
      if ((!st.isNativeMethod())
          && (!st.getClassName().equals(Thread.class.getName()))
          && (!st.getClassName().equals(getClass().getName()))) {
        return this.mClassName
            + "[ "
            + Thread.currentThread().getName()
            + ": "
            + st.getFileName()
            + ":"
            + st.getLineNumber()
            + " "
            + st.getMethodName()
            + " ]";
      }
    }
    return null;
  }
}
