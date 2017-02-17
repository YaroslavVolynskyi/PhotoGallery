package com.yarik.photogallery;

import android.support.annotation.Nullable;

/**
 * <br>
 * XYRALITY GmbH 2015, BkAndroidClient
 *
 * @author Yaroslav Volynskyi
 * @since 17/02/17.
 */

public class StringReverseUtils {

    @Nullable
    public static String reverseStringWithBuilder(@Nullable final String input) {
        final StringBuilder b = new StringBuilder();
        if (input != null && !input.isEmpty()) {
            for (int i = input.length() - 1; i >= 0; i--) {
                b.append(input.charAt(i));
            }
            return b.toString();
        }
        return null;
    }

    @Nullable
    public static String reverseStringWithCharArray(@Nullable final String input) {
        if (input != null && !input.isEmpty()) {
            char[] chars = input.toCharArray();
            final int length = chars.length;
            char additionalChar;
            for (int i = 0; i < length / 2; i++) {
                additionalChar = chars[i];
                chars[i] = chars[length - i - 1];
                chars[length - i - 1] = additionalChar;
            }
            return String.valueOf(chars);
        }
        return null;
    }
}
