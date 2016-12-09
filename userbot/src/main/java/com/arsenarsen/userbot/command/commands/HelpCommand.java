package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Comparator;

public class HelpCommand implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        EmbedBuilder builder = DiscordUtils.getEmbedBuilder()
                .setAuthor("UserBot v" + UserBot.VERSION, "https://github.com/ArsenArsen/SelfBot", null);
        builder.setTitle("\tBy Arsen#3291");
        UserBot.getInstance().getDispatcher().getCommands().stream().sorted(Comparator.comparing(Command::getName))
                .forEach(command -> builder.addField(command.getName(), "```fix\n" + command.getUsage() + "```", true));
        msg.editMessage(new MessageBuilder().setEmbed(builder.build()).build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "Lists commands. Simple?";
    }
}
