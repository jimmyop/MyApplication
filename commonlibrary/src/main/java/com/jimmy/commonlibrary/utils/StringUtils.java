package com.jimmy.commonlibrary.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @description 字符处理工具类
 */
public abstract class StringUtils {

    /**
     * 是否为空
     * 为空返回true,反着返回false;
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0 || value.equalsIgnoreCase("null")) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为数字
     *
     * @param obj
     * @return
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * true中文/false英文
     *
     * @param c
     * @return
     */
    public static boolean isCnorEn(char c) {
        boolean flag = false;
        if (c >= 0x0391 && c <= 0xFFE5) { // 中文
            flag = true;
        } else if (c >= 0x0000 && c <= 0x00FF) {// 英文
            flag = false;
        }
        return flag;
    }

    /**
     * 字符串长度，一个中文2个字节，英文1个字节
     *
     * @param str
     * @return
     */
    public static int[] strLength(String str) {
        int count[] = new int[2];

        for (int index = 0; index < str.length(); index++) {
            if (isCnorEn(str.charAt(index))) {
                count[0] = count[0] + 2;
                count[1]++;
            } else {
                count[0]++;
                count[1]++;
            }
        }
        return count;
    }

    /**
     * 数组是否为空
     *
     * @param values
     * @return true不为空，false为空
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * @param unicode
     * @return
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (!isEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }

    /**
     * @param input
     * @return
     */
    public static String stripNonValidXMLCharacters(String input) {
        if (input == null || ("".equals(input)))
            return "";
        StringBuilder out = new StringBuilder();
        char current;
        for (int i = 0; i < input.length(); i++) {
            current = input.charAt(i);
            if ((current == 0x9) || (current == 0xA) || (current == 0xD)
                    || ((current >= 0x20) && (current <= 0xD7FF))
                    || ((current >= 0xE000) && (current <= 0xFFFD))
                    || ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

    /**
     * 字符串按字节截取
     *
     * @param str 原字符
     * @param len 截取长度
     * @return
     */
    public static String splitString(String str, int len) {
        return splitString(str, len, "...");
    }

    /**
     * 字符串按字节截取
     *
     * @param str   原字符
     * @param len   截取长度
     * @param elide 省略符
     * @return String
     */
    public static String splitString(String str, int len, String elide) {
        if (str == null)
            return "";
        int strlen = str.length();
        if (strlen - len > 0) {
            str = str.substring(0, len) + elide.trim();
        }
        return str;
    }

    /**
     * 将json中的首字母大写处理成小写
     *
     * @param jsonInput
     * @return
     */
    public static String dealWithFirstChar(String jsonInput) {
        String originalInput = jsonInput;
        StringBuilder inputStr = new StringBuilder(jsonInput);
        String regex = "\"(\\w+)\":";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inputStr);
        List<String> result = new ArrayList<String>();
        while (m.find()) {
            String valueName = m.group(1);
            String newValueName = null;
            char[] words = valueName.toCharArray();
            if (Character.isUpperCase(words[0])) {// 首字母大写,不符合变量命名规范

                words[0] = Character.toLowerCase(words[0]);
                newValueName = new String(words);
                String regx1 = "\"" + valueName + "\":";
                String replace = "\"" + newValueName + "\":";
                originalInput = originalInput.replaceAll(regx1, replace);
            }
            result.add(valueName);
            inputStr.delete(0, m.end(0));
            m = p.matcher(inputStr);
        }
        return originalInput;

    }

    /**
     * 判断是否是以1开头的11位手机号码
     *
     * @param string
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isPhoneNum(String string) {

        String patternString = "(^13\\d{9}$)|(^14)[5,7]\\d{8}$|(^15[0,1,2,3,5,6,7,8,9]\\d{8}$)|(^17)[6,7,8]\\d{8}$|(^18\\d{9}$)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 判断是否是3-16位字母或数字
     *
     * @param string
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isPassword(String string) {
        String reg = "[a-zA-Z0-9]{3,16}";
        return string.matches(reg);
    }

    /**
     * 过滤掉非法字符"!", "@","-",　"‘", "/", "\"以及空格
     *
     * @param string
     * @return true 如果匹配，false 不匹配
     */
    public static String filter(String string) {

        return string.trim().replace("\\", "").replace("/", "")
                .replace("'", "").replace("-", "").replace("\"", "'");
    }

    /**
     * 匹配中文和字母和数字
     *
     * @param words
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isName(String words) {
        String pattern = "[[a-zA-Z0-9]|^[\\u4e00-\\u9fa5]$]+";
        return words.matches(pattern);
    }

    /**
     * 匹配信用卡(银行卡)号 16位数字用连字符或者空格或者分割
     *
     * @param cardnum
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isCreditCard(String cardnum) {
        String pattern = "^[{|\"(]?[0-9a-fA-F]{8}[-]?([0-9a-fA-F]{4}[-]?){3}[0-9a-fA-F]{12}[\")|}]?$";
        return cardnum.matches(pattern);
    }

    /**
     * 匹配Money
     *
     * @param money
     * @return true 如果匹配，false 不匹配
     */
    public static boolean isMoney(String money) {
        String pattern = "^[1-9][0-9]*(\\.[0-9]+)?";
        return money.matches(pattern);
    }

    /**
     * 计算签名
     *
     * @param baseString
     * @param keyString
     * @return
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static byte[] computeSignature(String baseString, String keyString)
            throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKey key = null;
        byte[] keyBytes = keyString.getBytes("UTF-8");
        key = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(key);
        byte[] text = baseString.getBytes("UTF-8");
        return mac.doFinal(text);
    }

    /**
     * 把b单位的大小转成xxM
     *
     * @param fileSize
     * @return
     */
    public static String formatFileSize(float fileSize) {
        String fileSizeString = "B";
        if (fileSize > 1024) {
            fileSize = fileSize / 1024;
            fileSizeString = "K";
            if (fileSize > 1024) {
                fileSize = fileSize / 1024;
                fileSizeString = "M";
            }
        }
        DecimalFormat df = new DecimalFormat("#0.00 ");
        fileSizeString = df.format(fileSize) + fileSizeString;
        return fileSizeString;
    }

    /**
     * 截取字符串，并处理中午乱码等问题
     *
     * @param name
     * @param len
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String spileChName(String name, int len) {
        byte[] bytes;
        try {
            bytes = name.getBytes("Unicode");
            int n = 0; // 表示当前的字节数
            int i = 2; // 要截取的字节数，从第3个字节开始
            for (; i < bytes.length && n < len; i++) {
                // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
                if (i % 2 == 1) {
                    n++; // 在UCS2第二个字节时n加1
                } else {
                    // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }
            // 如果i为奇数时，处理成偶数
            if (i % 2 == 1)

            {
                // 该UCS2字符是汉字时，去掉这个截一半的汉字
                if (bytes[i - 1] != 0)
                    i = i - 1;
                    // 该UCS2字符是字母或数字，则保留该字符
                else
                    i = i + 1;
            }
            return new String(bytes, 0, i, "Unicode");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5 加密
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /***
     *
     * @param string
     * @return 出错时返回-1
     */
    public static int paserStr2Int(String string) {
        try {
            int parseInt = Integer.parseInt(string);
            return parseInt;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }


    /***
     *
     * @param string
     * @return 出错时返回0
     */
    public static double paserStr2Double(String string) {
        try {
            double parseDouble = Double.parseDouble(string);
            BigDecimal result = new BigDecimal(String.valueOf(parseDouble));
            return result.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /***
     *
     * @return 出错时返回0.0
     */
    public static double paserStr2Double(Serializable serializable) {
        if (serializable instanceof String) {

            try {
                double parseDouble = Double.parseDouble((String) serializable);
                return parseDouble;
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
            }
        } else if (serializable instanceof Double) {
            return (Double) serializable;
        }
        return 0.0;
    }


    /***
     *
     * @return 出错时返回0
     */
    public static int paserStr2Int(Serializable serializable) {
        if (serializable instanceof String) {

            try {
                int parseInt = Integer.parseInt((String) serializable);
                return parseInt;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else if (serializable instanceof Integer) {
            return (Integer) serializable;
        }
        return 0;
    }

    /***
     *
     * @return 出错时返回0
     */
    public static Long paserStr2Long(Serializable serializable) {
        if (serializable instanceof String) {

            try {
                Long parseInt = Long.parseLong((String) serializable);
                return parseInt;
            } catch (Exception e) {
                e.printStackTrace();
                return 0l;
            }
        } else if (serializable instanceof Long || serializable instanceof Integer) {
            return (Long) serializable;
        }
        return 0l;
    }
}
