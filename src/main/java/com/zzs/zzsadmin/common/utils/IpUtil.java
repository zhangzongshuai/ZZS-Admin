package com.zzs.zzsadmin.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {


    /**
     * 获取客户端ip,如果前端使用nginx部署,需要在配置文件中加上以下配置:
     * <code>
     * <p>location /api{
     * <p>proxy_pass  http://ip:port;
     * <p> proxy_set_header Host $host;
     * <p>  proxy_set_header X-Real-IP $remote_addr;
     * <p> proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     * <p>}
     * </code>
     *
     * @param
     * @return String IP地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
