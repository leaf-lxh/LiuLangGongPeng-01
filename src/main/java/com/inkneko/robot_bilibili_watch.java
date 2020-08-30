package com.inkneko;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.AtAll;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class robot_bilibili_watch extends Thread{
    private int targetBiliUid = 12649642;
    private HashMap<Long, Group> targetGroups = new HashMap<>();
    private String requestURL = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=%d&offset_dynamic_id=0&need_top=1";

    private long lastVideoUploadTime = 0;
    private long lastDailyUpdateTime = 0;

    public robot_bilibili_watch(int targetBiliUid){
        this.targetBiliUid = targetBiliUid;
        long currentTimeStamp = new Date().getTime()/1000;
        lastVideoUploadTime = lastDailyUpdateTime = currentTimeStamp;
    }
    public void addGroup(GroupMessage event){
        if(targetGroups.containsKey(event.getGroup().getId())){
            event.getGroup().sendMessage("已添加订阅，请勿重复添加");
        }else{
            targetGroups.put(event.getGroup().getId(), event.getGroup());
            event.getGroup().sendMessage("已添加至订阅者列表");
        }
    }

    @Override
    public void run(){
        while(true){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(String.format(requestURL, targetBiliUid))
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String responseString = Objects.requireNonNull(response.body()).string();
                JsonObject responseJson = JsonParser.parseString(responseString).getAsJsonObject();
                JsonArray cards = responseJson.getAsJsonObject("data").getAsJsonArray("cards");
                for (JsonElement infoUnion : cards){
                    JsonObject card = infoUnion.getAsJsonObject();
                    card = JsonParser.parseString(card.get("card").getAsString()).getAsJsonObject();
                    if(card.keySet().contains("ctime")){
                        //为视频类动态
                        long ctime = card.get("ctime").getAsLong();
                        if (ctime > lastVideoUploadTime){
                            noticeVideoUpdate(
                                    card.get("aid").getAsLong(),
                                    card.get("title").getAsString(),
                                    card.get("desc").getAsString(),
                                    card.get("dynamic").getAsString());
                            lastVideoUploadTime = ctime;
                        }
                    }else if (card.keySet().contains("item")) {
                        JsonObject item = card.get("item").getAsJsonObject();
                        if (item.keySet().contains("upload_time")) {
                            //为普通动态
                            long upload_time = item.get("upload_time").getAsLong();
                            if (upload_time > lastDailyUpdateTime) {
                                noticeDailyUpdate(item.get("description").getAsString());
                                lastDailyUpdateTime = upload_time;
                            }
                        } else if (item.keySet().contains("timestamp")) {
                            long timestamp = item.get("timestamp").getAsLong();
                            if (timestamp > lastDailyUpdateTime) {
                                noticeDailyUpdate(item.get("content").getAsString());
                                lastDailyUpdateTime = timestamp;
                            }
                        }
                    }
                }
                sleep(1000 * 60 * 5); //5分钟
            } catch (IOException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                System.out.println("获取服务器返回结果为空");
            } catch (InterruptedException e) {
                System.out.println("监听线程被中断");
            }
        }

    }

    private void noticeVideoUpdate(long aid, String title, String description, String tags){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("董事长更新了视频\uD83D\uDCE2\uD83D\uDCE2").append("\n");
        stringBuilder.append("[标题]").append(title).append("\n");
        stringBuilder.append("[简介]").append(description).append("\n");
        stringBuilder.append("[标签]").append(tags).append("\n");
        stringBuilder.append("https://www.bilibili.com/video/av").append(aid);
        for (Long listener: targetGroups.keySet()){
            targetGroups.get(listener).sendMessage(stringBuilder.toString());
        }
    }

    private void noticeDailyUpdate(String content){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("董事长更新了一条动态\uD83D\uDCE2\uD83D\uDCE2").append("\n");
        stringBuilder.append("[内容]: ").append(content);
        for (Long listener: targetGroups.keySet()){
            targetGroups.get(listener).sendMessage(stringBuilder.toString());
        }
    }

    private void noticeError(String content){
        for (Long listener: targetGroups.keySet()){
            targetGroups.get(listener).sendMessage(content);
        }
    }
}
