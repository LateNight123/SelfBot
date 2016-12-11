package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.Configuration;
import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        EmbedBuilder builder = DiscordUtils.getEmbedBuilder()
                .setAuthor("UserBot v" + UserBot.VERSION, "https://github.com/ArsenArsen/SelfBot", null);
        builder.setTitle("\tBy Arsen#3291");
        if (args.length != 1) {
            builder.setDescription(UserBot.getInstance().getDispatcher().getCommands().stream().sorted(Comparator.comparing(Command::getName))
                    .map(Command::getName)
                    .collect(Collectors.joining(" ", "```fix\n", "\n```")));
            builder.setFooter(String.format("To get info on any command do %shelp COMMAND", Configuration.PREFIX.get()), null);
        } else {
            Optional<Command> command = UserBot.getInstance().getDispatcher().getCommands().stream()
                    .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0])).findFirst();
            if (command.isPresent()) {
                builder.addField(command.get().getName(), command.get().getUsage(), false);
            } else {
                builder.addField("Error!", "No such command!", false);
            }
        }
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
