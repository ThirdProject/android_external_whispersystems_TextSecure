/**
 * Copyright (C) 2011 Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thoughtcrime.securesms.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import org.thoughtcrime.securesms.mms.MmsRadio;
import org.whispersystems.textsecure.util.InvalidNumberException;
import org.whispersystems.textsecure.util.PhoneNumberFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ws.com.google.android.mms.pdu.CharacterSets;
import ws.com.google.android.mms.pdu.EncodedStringValue;

public class Util {

  public static String[] splitString(String string, int maxLength) {
    int count = string.length() / maxLength;

    if (string.length() % maxLength > 0)
      count++;

    String[] splitString = new String[count];

    for (int i=0;i<count-1;i++)
      splitString[i] = string.substring(i*maxLength, (i*maxLength) + maxLength);

    splitString[count-1] = string.substring((count-1) * maxLength);

    return splitString;
  }

  public static ExecutorService newSingleThreadedLifoExecutor() {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingLifoQueue<Runnable>());

    executor.execute(new Runnable() {
      @Override
      public void run() {
//        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
      }
    });

    return executor;
  }

  public static boolean isEmpty(EncodedStringValue[] value) {
    return value == null || value.length == 0;
  }

  public static CharSequence getBoldedString(String value) {
    SpannableString spanned = new SpannableString(value);
    spanned.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    spanned.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    return spanned;
  }

  public static CharSequence getItalicizedString(String value) {
    SpannableString spanned = new SpannableString(value);
    spanned.setSpan(new StyleSpan(Typeface.ITALIC), 0,
                    spanned.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    return spanned;
  }

  public static String toIsoString(byte[] bytes) {
    try {
      return new String(bytes, CharacterSets.MIMENAME_ISO_8859_1);
    } catch (UnsupportedEncodingException e) {
      // Impossible to reach here!
      Log.e("MmsDatabase", "ISO_8859_1 must be supported!", e);
      return "";
    }
  }

  public static byte[] toIsoBytes(String isoString) {
    try {
      return isoString.getBytes(CharacterSets.MIMENAME_ISO_8859_1);
    } catch (UnsupportedEncodingException e) {
      Log.w("Util", "ISO_8859_1 must be supported!", e);
      return new byte[0];
    }
  }

  public static void wait(Object lock, int timeout) {
    try {
      lock.wait(timeout);
    } catch (InterruptedException ie) {
      throw new AssertionError(ie);
    }
  }

  public static String canonicalizeNumber(Context context, String number)
      throws InvalidNumberException
  {
    String localNumber = TextSecurePreferences.getLocalNumber(context);
    return PhoneNumberFormatter.formatNumber(number, localNumber);
  }

  public static byte[] readFully(InputStream in) throws IOException {
    ByteArrayOutputStream baos   = new ByteArrayOutputStream();
    byte[]                buffer = new byte[4069];

    int read;

    while ((read = in.read(buffer)) != -1) {
      baos.write(buffer, 0, read);
    }

    in.close();
    return baos.toByteArray();
  }

  //  public static Bitmap loadScaledBitmap(InputStream src, int targetWidth, int targetHeight) {
  //    return BitmapFactory.decodeStream(src);
  ////  BitmapFactory.Options options = new BitmapFactory.Options();
  ////  options.inJustDecodeBounds    = true;
  ////  BitmapFactory.decodeStream(src, null, options);
  ////
  ////  Log.w("Util", "Bitmap Origin Width: " + options.outWidth);
  ////  Log.w("Util", "Bitmap Origin Height: " + options.outHeight);
  ////
  ////  boolean scaleByHeight =
  ////    Math.abs(options.outHeight - targetHeight) >=
  ////    Math.abs(options.outWidth - targetWidth);
  ////
  ////  if (options.outHeight * options.outWidth >= targetWidth * targetHeight * 2) {
  ////    double sampleSize = scaleByHeight ? (double)options.outHeight / (double)targetHeight : (double)options.outWidth / (double)targetWidth;
  //////    options.inSampleSize = (int)Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
  ////    Log.w("Util", "Sampling by: " + options.inSampleSize);
  ////  }
  ////
  ////  options.inJustDecodeBounds = false;
  ////
  ////  return BitmapFactory.decodeStream(src, null, options);
  //  }

}
