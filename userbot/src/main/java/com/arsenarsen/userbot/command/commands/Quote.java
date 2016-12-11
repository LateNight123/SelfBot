package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.time.format.DateTimeFormatter;

public class Quote implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length >= 2) {
            if (args[0].matches("\\d+")) {
                MessageChannel finalChannel = channel;
                channel.getHistoryAround(args[0], 100)
                        .queue(messageHistory -> process(messageHistory, finalChannel, msg, args, args[0]));
            } else if (args[0].matches("\\d+#\\d+")) {
                String[] split = args[0].split("#");
                String channelId = split[0];
                String messageId = split[1];
                channel = UserBot.getInstance().getJda().getTextChannelById(channelId);
                if (channel != null) {
                    MessageChannel finalChannel1 = channel;
                    channel.getHistoryAround(messageId, 100)
                            .queue(history -> process(history, finalChannel1, msg, args, messageId));
                }
            }
        }
    }

    private void process(MessageHistory history, MessageChannel finalChannel, Message msg, String[] args, String messageId) {
        Message msg2 = history.getMessageById(messageId);
        if (msg2 != null) {
            User auth = msg2.getAuthor();
            String cnt = msg.getRawContent();
            cnt = cnt.substring(UserBot.getInstance().getConfig().getProperty("prefix").length() + getName().length() + 1);
            cnt = cnt.substring(args[0].length());
            EmbedBuilder builder = DiscordUtils.getEmbedBuilder()
                    .setAuthor(auth.getName() + '#' + auth.getDiscriminator(), null, DiscordUtils.gerAvatar(auth))
                    .setDescription(msg2.getRawContent())
                    .setFooter(msg2.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), null)
                    .addField("Channel: ", "<#" + finalChannel.getId() + ">", true);
            for (Message.Attachment attachment : msg2.getAttachments()) {
                builder.setImage(attachment.getUrl());
            }
            msg.editMessage(new MessageBuilder()
                    .setEmbed(builder.build())
                    .append(cnt)
                    .build()).queue();
        }
    }

    @Override
    public String getName() {
        return "quote";
    }

    @Override
    public String getUsage() {
        return "Quotes someones message. Usage: quote CHANNEL_ID#MESSAGE_ID Your comment;" +
                " To get the message ID, enable developers mode and right click the message -> Copy ID.\n" +
                "Channel ID is used only for cross channel quoting. Remove `CHANNEL_ID#` for the same channel.";
    }
}
