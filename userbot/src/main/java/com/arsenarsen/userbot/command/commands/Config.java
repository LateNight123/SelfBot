package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.Configuration;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Config implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length > 1) {
            switch (args[0].toLowerCase()) {
                case "get":
                    Configuration configuration = Configuration.get(args[1]);
                    if (configuration != null)
                        msg.editMessage(new MessageBuilder().append(msg.getRawContent())
                                .setEmbed(DiscordUtils.getEmbedBuilder()
                                        .addField(configuration.name(), configuration.get(), true)
                                        .build()).build()).queue();
                    break;
                case "set":
                    if (args.length > 2) {
                        String value = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
                        String key = args[1];

                        Configuration configuration2 = Configuration.get(args[1]);
                        if (configuration2 != null)
                            try {
                                configuration2.set(value);
                                EmbedBuilder builder = DiscordUtils.getEmbedBuilder();
                                builder.addField(configuration2.name(), configuration2.get(), true);
                                msg.editMessage(new MessageBuilder().append(msg.getRawContent())
                                        .setEmbed(builder.build()).build()).queue();
                            } catch (IllegalArgumentException e) {
                                msg.editMessage(new MessageBuilder().append(msg.getRawContent())
                                        .setEmbed(DiscordUtils.getEmbedBuilder().setDescription(e.getMessage()).build())
                                        .build()).queue();
                            }
                    }
                    break;
            }
        } else {
            EmbedBuilder builder = DiscordUtils.getEmbedBuilder();
            for (Configuration c : Configuration.values()) {
                try {
                    builder.addField(c.name(), c.get(), true);
                } catch (Exception ignored) {
                }
            }
            msg.editMessage(new MessageBuilder().append(msg.getRawContent()).setEmbed(builder.build()).build()).queue();
        }
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getUsage() {
        return "`config SET KEY VALUE | GET KEY` to manipulate values, just `config` to list them";
    }
}
