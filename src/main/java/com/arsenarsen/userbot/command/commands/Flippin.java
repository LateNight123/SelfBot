package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * <br>
 * Created by Arsen on 23.9.16..
 */
public class Flippin implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        msg.editMessage("(╯°□°）╯︵ ┻━┻").queue();
    }

    @Override
    public String getName() {
        return "flippin";
    }
}
