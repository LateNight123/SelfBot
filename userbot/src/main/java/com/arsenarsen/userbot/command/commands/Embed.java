package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.Configuration;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Embed implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if(args.length == 0)
            return;
        String cnt = DiscordUtils.substringCommand(this, msg, true);
        EmbedBuilder eb = DiscordUtils.getEmbedBuilder().setDescription(cnt);
        if(Configuration.EMBED_NAMED.get().equals("true"))
            eb.setAuthor(msg.getAuthor().getName() + '#' + msg.getAuthor().getDiscriminator(),
                    null, DiscordUtils.gerAvatar(msg.getAuthor()));
        msg.editMessage(new MessageBuilder().setEmbed(eb.build()).build()).queue();
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getUsage() {
        return "Makes a fancy embed instead of a normal message :o";
    }
}
