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
    /*ʱ���ʽ����*/
    private SimpleDateFormat timeFormater = new SimpleDateFormat("YYYYMMdd");
    /*ǩ����*/
    private JsonArray lotDatabase;
    /*ǩ����*/
    private int maxLotRange = 384;

    /*�����ѳ�ǩ���û�*/
    private static HashMap<Long, Boolean> todayDrawedUsers = new HashMap<>();
    /*��������*/
    private static String lastDay = "";

    /*robot_draw_lot���캯��*/
    public robot_draw_lot(){

        InputStream stream = robot_draw_lot.class.getResourceAsStream("/drawlot.json");
        String content = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
        lotDatabase = JsonParser.parseString(content).getAsJsonArray();
    }

    /**
     * ��ǩ
     * @param uin �û���qq����
     * @return ��ǩ��Ӧ����
     */
    public String drawlot(long uin){
        int order = getOrder(uin);
        Boolean drawed = todayDrawedUsers.get(uin);
        if (drawed != null && drawed){
            return "�������Ѿ����ǩ�ˣ���ǩ�뷢�͡���ǩ��";
        }
        else{
            todayDrawedUsers.put(uin, true);
        }

        JsonObject specificLotInfo = lotDatabase.get(order).getAsJsonObject();
        return "ǩλ��" + specificLotInfo.get("order").getAsString() + "\n" + specificLotInfo.get("lot").getAsString() + "\n��ǩ�뷢�͡���ǩ��";
    }

    /**
     * ��ǩ
     * @param uin �û���qq����
     * @return ��ǩ��Ӧ����
     */
    public String interpret(long uin){
        int order = getOrder(uin);
        Boolean drawed = todayDrawedUsers.get(uin);
        if (drawed == null || !drawed){
            return "���ȳ�ǩ���ٽ��н�ǩ";
        }
        JsonObject specificLotInfo = lotDatabase.get(order).getAsJsonObject();
        return "��ǩ��" + specificLotInfo.get("order").getAsString() + "\n" + specificLotInfo.get("interpret").getAsString();
    }

    /**
     * �������ں��û�qq���ɵ��յ�ǩλ
     * @param uin �û���qq
     * @return ǩλ
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
