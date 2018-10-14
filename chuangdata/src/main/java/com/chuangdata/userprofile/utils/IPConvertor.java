package com.chuangdata.userprofile.utils;

/**
 * TODO should use a unified jar, not copy codes from Analyser
 *
 * @author User
 */
public class IPConvertor {
    /**
     * 将十进制IP转化为点分形式
     *
     * @param ip 待转换的IP
     * @return 转换后的IP
     */
    public static String ip2str(long ip) {
        return (ip >>> 24 & 0xFF) + "." + (ip >>> 16 & 0xFF) + "."
                + (ip >>> 8 & 0xFF) + "." + (ip & 0xFF);
    }

    /**
     * IP地址转long方法
     *
     * @param strIp 点分IP地址
     * @return
     */
    public static long ipToLong(String strIp) {
        if(strIp.equals("")){
            return 0;
        }
        long[] ip = new long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String LongToIp(long ip)
    {
        StringBuilder sb = new StringBuilder();
        //直接右移24位
        sb.append(ip >> 24);
        sb.append(".");
        //将高8位置0，然后右移16
        sb.append((ip & 0x00FFFFFF) >> 16);
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append((ip & 0x0000FFFF) >> 8);
        sb.append(".");
        //将高24位置0
        sb.append((ip & 0x000000FF));
        return sb.toString();
    }
}
