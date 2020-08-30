package com.inkneko;

import net.mamoe.mirai.message.GroupMessage;

import java.util.HashMap;
import java.util.function.Consumer;

public class robot_core {
    private HashMap<String, Consumer<GroupMessage>> keywordContainsList;
    private HashMap<String, Consumer<GroupMessage>> keywordEqualsList;

    public robot_core(){
        keywordEqualsList = new HashMap<>();
        keywordContainsList = new HashMap<>();
        registerService("功能列表", this::printServices, true);
    }

    /**
     * 注册服务
     * @param keyword 关键字
     * @param callback 响应函数
     * @param fullMatch 是否为全字匹配
     */
    public void registerService(String keyword, Consumer<GroupMessage> callback, boolean fullMatch){
        if (fullMatch){
            keywordEqualsList.put(keyword, callback);
        }else{
            keywordContainsList.put(keyword, callback);
        }
    }

    /**
     * 移除服务
     * @param keyword 关键字
     * @param fullMatch 是否位于全字匹配列表
     */
    public void removeService(String keyword, boolean fullMatch){
        if (fullMatch){
            keywordEqualsList.remove(keyword);
        }else{
            keywordContainsList.remove(keyword);
        }
    }

    /**
     * 服务分发
     * @param event 群消息事件
     */
    public void ServiceDispatch(GroupMessage event){
        String message = event.getMessage().contentToString();
        for (String keyword : keywordContainsList.keySet()){
            if(message.contains(keyword)){
                keywordContainsList.get(keyword).accept(event);
            }
        }
        for (String keyword : keywordEqualsList.keySet()){
            if(message.equals(keyword)){
                keywordEqualsList.get(keyword).accept(event);
            }
        }
    }

    /**
     * 输出当前的服务
     * @param event 群消息事件
     */
    private void printServices(GroupMessage event){
        StringBuilder response = new StringBuilder("【功能如下】");
        for (String keyword : keywordContainsList.keySet()){
            response.append("\n").append(keyword);
        }
        for (String keyword : keywordEqualsList.keySet()){
            response.append("\n").append(keyword);
        }
        if(keywordEqualsList.size() == 0 && keywordContainsList.size() == 0){
            response.append("\n").append("无");
        }
        event.getGroup().sendMessage(response.toString());
    }
}
