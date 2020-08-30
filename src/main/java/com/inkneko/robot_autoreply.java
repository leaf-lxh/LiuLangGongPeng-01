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
        core.registerService("抽签", (GroupMessage event) -> {
            final At at = new At(event.getSender());
            event.getGroup().sendMessage(at.plus(lotGenerator.drawlot(event.getSender().getId())));
            }, true);
        core.registerService("解签", (GroupMessage event) -> {
            final At at = new At(event.getSender());
            event.getGroup().sendMessage(at.plus(lotGenerator.interpret(event.getSender().getId())));
            }, true);

         */
        //没做发送者限制
        core.registerService("开启董事长动态订阅", (GroupMessage event) -> {biliWatch.addGroup(event);}, true);

        this.getEventListener().subscribeAlways(GroupMessage.class, (GroupMessage event) -> {
            core.ServiceDispatch(event);
        });
        this.getEventListener().subscribeAlways(MemberJoinEvent.class, (MemberJoinEvent event)->{
            final At quote = new At(event.getMember());
            event.getGroup().sendMessage(quote.plus("请新员工仔细阅读员工守则和其他群公告哦"));
        });

        biliWatch.start();
    }
}



/* plugin demo
    public void onEnable() {
        getLogger().info("Plugin enabled!");

        this.getEventListener().subscribeAlways(GroupMessage.class, (GroupMessage event) -> {
            String content = event.getMessage().contentToString();
            getLogger().info("收到消息: " + content);
            if (content.contains("reply")) {
                // 引用回复
                final QuoteReply quote = MessageUtils.quote(event.getMessage());
                event.getGroup().sendMessage(quote.plus("引用回复"));

            } else if (content.contains("at")) {
                // at
                event.getGroup().sendMessage(new At(event.getSender()));

            } else if (content.contains("permission")) {
                // 成员权限
                event.getGroup().sendMessage(event.getPermission().toString());

            } else if (content.contains("mixed")) {
                // 复合消息, 通过 .plus 连接两个消息
                event.getGroup().sendMessage(
                        MessageUtils.newImage("{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.png") // 演示图片, 可能已过期
                                .plus("Hello") // 文本消息
                                .plus(new At(event.getSender())) // at 群成员
                                .plus(AtAll.INSTANCE) // at 全体成员
                );

            } else if (content.contains("recall1")) {
                event.getGroup().sendMessage("你看不到这条消息").recall();
                // 发送消息马上就撤回. 因速度太快, 客户端将看不到这个消息.

            } else if (content.contains("recall2")) {
                final Job job = event.getGroup().sendMessage("3秒后撤回").recall(3000);

                // job.cancel(new CancellationException()); // 可取消这个任务

            } else if (content.contains("上传图片")) {
                File file = new File("myImage.jpg");
                if (file.exists()) {
                    final Image image = event.getGroup().uploadImage(new File("myImage.jpg"));
                    // 上传一个图片并得到 Image 类型的 Message
                    event.getGroup().sendMessage(image); // 发送图片

                    final String imageId = image.getImageId(); // 可以拿到 ID
                    final Image fromId = MessageUtils.newImage(imageId); // ID 转换得到 Image

                    event.getGroup().sendMessage(fromId);
                }

            } else if (content.contains("friend")) {
                final Future<MessageReceipt<Contact>> future = event.getSender().sendMessageAsync("Async send"); // 异步发送
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

             */

