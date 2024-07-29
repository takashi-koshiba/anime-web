package com.example.web.classes;

import jakarta.servlet.http.HttpServletRequest;

public class main01 {

	public static String getIp(HttpServletRequest request) {
        String ip =  request.getHeader("X-Forwarded-For");
        //ELB等を経由していたらxForwardedForを返す
        if (ip != null) {
            return ip;
        }
        return request.getRemoteAddr();      //(2)
    }
}
