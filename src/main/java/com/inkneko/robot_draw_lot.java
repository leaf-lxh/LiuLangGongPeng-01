package com.inkneko;

import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class robot_draw_lot {
    /*时间格式化器*/
    private SimpleDateFormat timeFormater = new SimpleDateFormat("YYYYMMdd");
    /*签数据*/
    private JsonArray lotDatabase;
    /*签数量*/
    private int maxLotRange = 384;

    /*今日已抽签的用户*/
    private static HashMap<Long, Boolean> todayDrawedUsers = new HashMap<>();
    /*今日日期*/
    private static String lastDay = "";

    /*robot_draw_lot构造函数*/
    public robot_draw_lot(){

        InputStream stream = robot_draw_lot.class.getResourceAsStream("/drawlot.json");
        String content = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
        lotDatabase = JsonParser.parseString(content).getAsJsonArray();
    }

    /**
     * 抽签
     * @param uin 用户的qq号码
     * @return 抽签响应数据
     */
    public String drawlot(long uin){
        int order = getOrder(uin);
        Boolean drawed = todayDrawedUsers.get(uin);
        if (drawed != null && drawed){
            return "今日你已经抽过签了，解签请发送【解签】";
        }
        else{
            todayDrawedUsers.put(uin, true);
        }

        JsonObject specificLotInfo = lotDatabase.get(order).getAsJsonObject();
        return "签位：" + specificLotInfo.get("order").getAsString() + "\n" + specificLotInfo.get("lot").getAsString() + "\n解签请发送【解签】";
    }

    /**
     * 解签
     * @param uin 用户的qq号码
     * @return 解签响应数据
     */
    public String interpret(long uin){
        int order = getOrder(uin);
        Boolean drawed = todayDrawedUsers.get(uin);
        if (drawed == null || !drawed){
            return "请先抽签，再进行解签";
        }
        JsonObject specificLotInfo = lotDatabase.get(order).getAsJsonObject();
        return "解签：" + specificLotInfo.get("order").getAsString() + "\n" + specificLotInfo.get("interpret").getAsString();
    }

    /**
     * 根据日期和用户qq生成当日的签位
     * @param uin 用户的qq
     * @return 签位
     */
    private int getOrder(long uin){
        Date today = new Date();
        String todayDate = timeFormater.format(today);
        if (!lastDay.equals(todayDate)){
            todayDrawedUsers.clear();
            lastDay = todayDate;
        }

        long todayNumeric = Integer.parseInt(todayDate);
        Random random = new Random(todayNumeric + uin);
        return random.nextInt(maxLotRange);
    }
}
