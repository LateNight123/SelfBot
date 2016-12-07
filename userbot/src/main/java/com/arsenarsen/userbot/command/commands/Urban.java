package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import com.arsenarsen.userbot.util.IOUtils;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Urban implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length > 0)
            try {
                String term = Arrays.stream(args).collect(Collectors.joining(" "));
                term = URLEncoder.encode(term, "UTF-8");
                UrbanResponse resp = UserBot.GSON.fromJson(Unirest
                        .get("http://api.urbandictionary.com/v0/define?term=" + term)
                        .header("User-Agent", "Mozilla/5.0 " + System.currentTimeMillis())
                        .asString()
                        .getBody(), UrbanResponse.class);
                if (!resp.result_type.equals("exact")) {
                    msg.editMessage("Could not find a definiton! Urban Dictionary said: " + IOUtils.capital(resp.result_type)).queue();
                    return;
                }
                UrbanResponse.Definiton d = resp.list[0];
                msg.editMessage(new MessageBuilder().setEmbed(new EmbedBuilder()
                        .setTitle("Definiton of " + URLDecoder.decode(term, "UTF-8") + " on UrbanDictionary")
                        .setAuthor(d.author + " on UrbanDictionary", d.permalink, IOUtils.getIcon("http://www.urbandictionary.com/"))
                        .setFooter(d.example, "http://www.urbandictionary.com/")
                        .setColor(new Color((int)(0x1000000*Math.random())))
                        .setDescription(d.definition)
                        .build()).build()).queue();
            } catch (UnirestException | IOException | URISyntaxException e) {
                DiscordUtils.updateWithException("I could not define that!", e, msg);
            }
    }

    @Override
    public String getName() {
        return "urban";
    }

    @Override
    public String getUsage() {
        return "Defines stuff :o; Usage: urban blah blah";
    }

    private static class UrbanResponse { // urban af
        public String result_type;
        public Definiton[] list;

        static class Definiton {
            public String definition;
            public String permalink;
            public String example;
            public String author;
        }
    }
}
