package com.smsdemo.util;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * 参见：https://help.aliyun.com/document_detail/25492.html?spm=5176.doc25490.2.4.a1CU9t
 */
public class SignUtil {
    private static final Object LOCK = new Object();

    private static Mac macInstance;

    private static final String ALGORITHM = "HmacSHA1";

    public static String sign(String reqURl, Map<String, Object> parameterMap, String method, String accessKeySecret) {
        if (reqURl == null) {
            reqURl = "/";
        }
        if (!reqURl.startsWith("/")) {
            reqURl = "/" + reqURl;
        }
        String canonicalizedQueryString = canonicalize(parameterMap);
        String StringToSign =
                method + "&" +
                        percentEncode(reqURl) + "&" +
                        percentEncode(canonicalizedQueryString);
        return computeSignature(accessKeySecret, StringToSign);
    }

    public static String sign(Map<String, Object> parameterMap, String method, String accessKeySecret) {
        return sign("/", parameterMap, method, accessKeySecret);
    }

    private static String canonicalize(Map<String, Object> parameterMap) {
        StringBuilder sb = new StringBuilder();
        String[] parameterNames = parameterMap.keySet().toArray(
                new String[parameterMap.size()]);
        Arrays.sort(parameterNames);
        int count = 0;
        for (String paramName : parameterNames) {
            count++;
            sb.append(percentEncode(paramName));
            Object paramValue = parameterMap.get(paramName);
            if (paramValue != null) {
                sb.append("=").append(percentEncode(String.valueOf(paramValue)));
            }
            if (count < parameterNames.length) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static String percentEncode(String value) {
        try {
            return value != null ? URLEncoder.encode(value, DEFAULT_ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String computeSignature(String key, String data) {
        try {
            byte[] signData = sign(key.getBytes(DEFAULT_ENCODING), data.getBytes(DEFAULT_ENCODING));
            return Base64Util.encode(signData);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Unsupported algorithm: " + DEFAULT_ENCODING, ex);
        }
    }

    private static byte[] sign(byte[] key, byte[] data) {
        try {
            if (macInstance == null) {
                synchronized (LOCK) {
                    if (macInstance == null) {
                        macInstance = Mac.getInstance(ALGORITHM);
                    }
                }
            }
            Mac mac = null;
            try {
                mac = (Mac) macInstance.clone();
            } catch (CloneNotSupportedException e) {
                mac = Mac.getInstance(ALGORITHM);
            }
            mac.init(new SecretKeySpec(key, ALGORITHM));
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unsupported algorithm: " + ALGORITHM, ex);
        } catch (InvalidKeyException ex) {
            throw new RuntimeException("Invalid key: " + key, ex);
        }
    }


    private static String buildCanonicalizedResource(String resourcePath, Map<String, String> parameters) {
        if (!resourcePath.startsWith("/")) {
            throw new RuntimeException("Resource path should start with '/'");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(resourcePath);
        if (parameters != null) {
            String[] parameterNames = parameters.keySet().toArray(
                    new String[parameters.size()]);
            Arrays.sort(parameterNames);
            char separater = '?';
            for (String paramName : parameterNames) {
                builder.append(separater);
                builder.append(paramName);
                String paramValue = parameters.get(paramName);
                if (paramValue != null) {
                    builder.append("=").append(paramValue);
                }
                separater = '&';
            }
        }
        return builder.toString();
    }
}
