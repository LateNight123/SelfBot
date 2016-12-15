package com.arsenarsen.userbot.command.co;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Groovy implements Command {
    private ScriptEngineManager manager = new ScriptEngineManager();
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        ScriptEngine engine = manager.getEngineByName("groovy");
        engine.put("channel", channel);
        if(channel.getType() == ChannelType.TEXT)
            engine.put("guild", ((TextChannel)channel).getGuild());
        else engine.put("guild", null);
        engine.put("message", msg);
        String groovy = Arrays.stream(args).collect(Collectors.joining(" "));
        try {
            msg.editMessage("Code: ```groovy\n" + groovy + "```\nOutput: ```groovy\n" + engine.eval("import java.util.*;\n" +
                    "import java.util.concurrent.*;\n" +
                    "import java.util.stream.*;\n" +
                    "import java.time.*;\n" +
                    "import java.math.*;\n" +
                    "import net.dv8tion.jda.core.entities.*;\n" +
                    "import net.dv8tion.jda.core.entities.impl.*;\n" +
                    "import net.dv8tion.jda.core.managers.*;\n" +
                    "import net.dv8tion.jda.core.*;\n" +
                    "import com.arsenarsen.userbot.*;\n" +
                    "import com.arsenarsen.userbot.command.*;\n" + groovy) + "\n```").queue();
        } catch (ScriptException e) {
            UserBot.LOGGER.error("Could not evaluate js! {}".replace("{}", groovy), e);
            msg.editMessage("Could not evaluate ```groovy\n" + groovy + "\n```\n```groovy" + e.getMessage() + "```").queue();
        }
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public String getUsage() {
        return "Evaluates a groovy script.";
    }
}
