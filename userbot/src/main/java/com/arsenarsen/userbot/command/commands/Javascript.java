package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.script.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Javascript implements Command {
    private ScriptEngineManager engineManager = new ScriptEngineManager();

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        engine.put("channel", channel);
        if(channel.getType() == ChannelType.TEXT)
            engine.put("guild", ((TextChannel)channel).getGuild());
        else engine.put("guild", null);
        engine.put("message", msg);
        String js = Arrays.stream(args).collect(Collectors.joining(" "));
        try {
            msg.editMessage("Code: ```js\n" + js + "```\nOutput: ```js\n" + engine.eval(js) + "\n```").queue();
        } catch (ScriptException e) {
            UserBot.LOGGER.error("Could not evaluate js! {}".replace("{}", js), e);
            msg.editMessage("Could not evaluate ```js\n" + js + "\n```\n```js" + e.getMessage() + "```").queue();
        }
    }

    @Override
    public String getName() {
        return "js";
    }

    @Override
    public String getUsage() {
        return "javascript eval";
    }
}
