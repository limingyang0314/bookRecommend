package com.example.springwebserver.util;

public class NumberUtil {
    /**
     * 生成订单流水号
     *
     * @return
     */
    public static String getOrderNo() {
        StringBuffer buffer = new StringBuffer(String.valueOf(System.currentTimeMillis()));
        int num = getRandomNum(4);
        buffer.append(num);
        return buffer.toString();
    }
    public static int getRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }
}
