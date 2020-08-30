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
        registerService("�����б�", this::printServices, true);
    }

    /**
     * ע�����
     * @param keyword �ؼ���
     * @param callback ��Ӧ����
     * @param fullMatch �Ƿ�Ϊȫ��ƥ��
     */
    public void registerService(String keyword, Consumer<GroupMessage> callback, boolean fullMatch){
        if (fullMatch){
            keywordEqualsList.put(keyword, callback);
        }else{
            keywordContainsList.put(keyword, callback);
        }
    }

    /**
     * �Ƴ�����
     * @param keyword �ؼ���
     * @param fullMatch �Ƿ�λ��ȫ��ƥ���б�
     */
    public void removeService(String keyword, boolean fullMatch){
        if (fullMatch){
            keywordEqualsList.remove(keyword);
        }else{
            keywordContainsList.remove(keyword);
        }
    }

    /**
     * ����ַ�
     * @param event Ⱥ��Ϣ�¼�
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
     * �����ǰ�ķ���
     * @param event Ⱥ��Ϣ�¼�
     */
    private void printServices(GroupMessage event){
        StringBuilder response = new StringBuilder("���������¡�");
        for (String keyword : keywordContainsList.keySet()){
            response.append("\n").append(keyword);
        }
        for (String keyword : keywordEqualsList.keySet()){
            response.append("\n").append(keyword);
        }
        if(keywordEqualsList.size() == 0 && keywordContainsList.size() == 0){
            response.append("\n").append("��");
        }
        event.getGroup().sendMessage(response.toString());
    }
}
