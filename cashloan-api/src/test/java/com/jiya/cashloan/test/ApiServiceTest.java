package com.jiya.cashloan.test;

import com.alibaba.fastjson.JSON;
import com.xiji.cashloan.core.common.util.HttpsUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: king
 * @Date: 2018/12/3 23:38
 * @Description:
 */
public class ApiServiceTest {

    public static void main(String[] args) {
        testfindProgress();
    }


    public static void testfindProgress() {
        String url = "http://47.110.61.233:8080/api/act/mine/borrow/findProgress.htm";
        Map<String,String> param = new HashMap<String, String>();
        param.put("web","debug");
        param.put("borrowId","5");
        System.out.println(JSON.toJSON(param));
        String resp = HttpsUtil.postClient(url, param);
        System.out.println();
        System.out.println(resp);
    }

    public static void testfindIndex() {
        String url = "http://47.110.61.233:8080/api/borrow/findIndex.htm";
        Map<String,String> param = new HashMap<String, String>();
        param.put("web","debug");
        param.put("borrowId","5");
        System.out.println(JSON.toJSON(param));
        String resp = HttpsUtil.postClient(url, param);
        System.out.println();
        System.out.println(resp);
    }
}