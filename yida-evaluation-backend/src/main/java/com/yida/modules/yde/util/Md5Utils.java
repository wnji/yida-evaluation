package com.yida.modules.yde.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Md5Utils {
    private final static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public synchronized static String getMd5Str(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return bufferToHex(md.digest());
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误，" + e.toString());
        }
    }

    private static String bufferToHex(byte[] bytes) throws Exception {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) throws Exception {
        int k = m + n;
        if (bytes.length < k) {
            throw new Exception("超过范围");
        }
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换,

        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Md5Utils.getMd5Str("000001"));
        System.out.println(Md5Utils.getMd5Str("yida-" + "AL1000001" + "4fc711301f3c784d66955d98d399afb"));
    }
}
