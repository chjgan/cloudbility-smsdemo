package com.smsdemo.util;

/**
 * Base64 编码解码算法
 *
 * @author atlas
 */
public final class Base64Util {
    private Base64Util() {

    }

    public static String encode(byte[] raw) {
        return CodecBase64.encodeBase64String(raw);
    }

    public static byte[] decode(String base64) {
        return CodecBase64.decodeBase64(base64);
    }

    public static String encodeURLSafe(byte[] data) {
        return CodecBase64.encodeBase64URLSafeString(data);
    }
}