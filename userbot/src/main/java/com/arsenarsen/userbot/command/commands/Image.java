package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Image implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length != 1)
            return;
        String url = args[0];
        if (EmbedBuilder.URL_PATTERN.matcher(url).matches() && url.length() < EmbedBuilder.URL_MAX_LENGTH)
            msg.editMessage(new MessageBuilder().setEmbed(DiscordUtils.getEmbedBuilder()
                    .setImage(url).build()).build()).queue();
    }

    @Override
    public String getName() {
        return "img";
    }

    @Override
    public String getUsage() {
        return "img url - Embeds an image :o";
    }
}
