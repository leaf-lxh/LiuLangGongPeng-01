package com.inkneko;

import kotlinx.coroutines.Job;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.GroupMessage;
import net.mamoe.mirai.message.data.*;

class robot_autoreply extends PluginBase {
    private robot_core core = new robot_core();
    private robot_draw_lot lotGenerator = new robot_draw_lot();
    private robot_bilibili_watch biliWatch = new robot_bilibili_watch(12649642);
    public void onLoad() {
        getLogger().info("Plugin loaded!");
    }

    public void onEnable(){
        /*
        core.registerService("��ǩ", (GroupMessage event) -> {
            final At at = new At(event.getSender());
            event.getGroup().sendMessage(at.plus(lotGenerator.drawlot(event.getSender().getId())));
            }, true);
        core.registerService("��ǩ", (GroupMessage event) -> {
            final At at = new At(event.getSender());
            event.getGroup().sendMessage(at.plus(lotGenerator.interpret(event.getSender().getId())));
            }, true);

         */
        //û������������
        core.registerService("�������³���̬����", (GroupMessage event) -> {biliWatch.addGroup(event);}, true);

        this.getEventListener().subscribeAlways(GroupMessage.class, (GroupMessage event) -> {
            core.ServiceDispatch(event);
        });
        this.getEventListener().subscribeAlways(MemberJoinEvent.class, (MemberJoinEvent event)->{
            final At quote = new At(event.getMember());
            event.getGroup().sendMessage(quote.plus("����Ա����ϸ�Ķ�Ա�����������Ⱥ����Ŷ"));
        });

        biliWatch.start();
    }
}



/* plugin demo
    public void onEnable() {
        getLogger().info("Plugin enabled!");

        this.getEventListener().subscribeAlways(GroupMessage.class, (GroupMessage event) -> {
            String content = event.getMessage().contentToString();
            getLogger().info("�յ���Ϣ: " + content);
            if (content.contains("reply")) {
                // ���ûظ�
                final QuoteReply quote = MessageUtils.quote(event.getMessage());
                event.getGroup().sendMessage(quote.plus("���ûظ�"));

            } else if (content.contains("at")) {
                // at
                event.getGroup().sendMessage(new At(event.getSender()));

            } else if (content.contains("permission")) {
                // ��ԱȨ��
                event.getGroup().sendMessage(event.getPermission().toString());

            } else if (content.contains("mixed")) {
                // ������Ϣ, ͨ�� .plus ����������Ϣ
                event.getGroup().sendMessage(
                        MessageUtils.newImage("{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.png") // ��ʾͼƬ, �����ѹ���
                                .plus("Hello") // �ı���Ϣ
                                .plus(new At(event.getSender())) // at Ⱥ��Ա
                                .plus(AtAll.INSTANCE) // at ȫ���Ա
                );

            } else if (content.contains("recall1")) {
                event.getGroup().sendMessage("�㿴����������Ϣ").recall();
                // ������Ϣ���Ͼͳ���. ���ٶ�̫��, �ͻ��˽������������Ϣ.

            } else if (content.contains("recall2")) {
                final Job job = event.getGroup().sendMessage("3��󳷻�").recall(3000);

                // job.cancel(new CancellationException()); // ��ȡ���������

            } else if (content.contains("�ϴ�ͼƬ")) {
                File file = new File("myImage.jpg");
                if (file.exists()) {
                    final Image image = event.getGroup().uploadImage(new File("myImage.jpg"));
                    // �ϴ�һ��ͼƬ���õ� Image ���͵� Message
                    event.getGroup().sendMessage(image); // ����ͼƬ

                    final String imageId = image.getImageId(); // �����õ� ID
                    final Image fromId = MessageUtils.newImage(imageId); // ID ת���õ� Image

                    event.getGroup().sendMessage(fromId);
                }

            } else if (content.contains("friend")) {
                final Future<MessageReceipt<Contact>> future = event.getSender().sendMessageAsync("Async send"); // �첽����
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

             */

