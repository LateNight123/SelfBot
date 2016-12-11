package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Javascript implements Command {
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
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
