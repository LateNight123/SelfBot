package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Status implements Command {
    private static final Pattern GET_STREAM_URL_PATTERN = Pattern.compile("STREAM=(.+)", Pattern.CASE_INSENSITIVE);

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length > 1 && args[0].toLowerCase().matches("(game)|(stream=.+)")) {
            if (args[0].equalsIgnoreCase("game")) {
                UserBot.getInstance().getJda().getPresence()
                        .setGame(Game.of(Arrays.stream(args).skip(1).collect(Collectors.joining(" "))));
                msg.deleteMessage();
            } else {
                Matcher m = GET_STREAM_URL_PATTERN.matcher(args[0]);
                if (m.matches()) {
                    if (Game.isValidStreamingUrl(m.group(1))) {
                        UserBot.getInstance().getJda().getPresence()
                                .setGame(Game.of(Arrays.stream(args).skip(1).collect(Collectors.joining(" ")), m.group(1)));
                        msg.deleteMessage();
                    } else {
                        UserBot.getInstance().getJda().getPresence()
                                .setGame(Game.of(Arrays.stream(args).skip(1).collect(Collectors.joining(" "))));
                        msg.deleteMessage();
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public String getUsage() {
        return "`status GAME|STREAM=URL something here`";
    }
}
