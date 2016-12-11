package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.sql.SQL;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class Quotes implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            SQL.executeSQL(conn -> conn.createStatement().execute("CREATE TABLE IF NOT EXISTS quotes(\n" +
                    "   id      INTEGER      PRIMARY KEY,\n" +
                    "   mid     VARCHAR(20)  UNIQUE      ON CONFLICT ROLLBACK,\n" +
                    "   quote   VARCHAR(200) NOT NULL,\n" +
                    "   author  VARCHAR(30)  NOT NULL,\n" +
                    "   avatar  VARCHAR(60)  NOT NULL,\n" +
                    "   channel VARCHAR(20)  NOT NULL\n," +
                    "   time    VARCHAR(30)  NOT NULL\n" +
                    ")"));
            if (args.length == 2 && args[1].matches("\\d+")) {
                if (args[0].equalsIgnoreCase("add")) {
                    channel.getHistoryAround(args[1], 100).queue(history -> {
                        Message message = history.getMessageById(args[1]);
                        String author = DiscordUtils.getTag(message.getAuthor());
                        String avatar = DiscordUtils.gerAvatar(message.getAuthor());
                        String channelTag = '#' + channel.getName();
                        if (channel.getType() == ChannelType.TEXT) {
                            channelTag += " in guild " + ((TextChannel) channel).getGuild().getName();
                        }
                        try {
                            String finalChannelTag = channelTag;
                            SQL.executeSQL(conn -> {
                                PreparedStatement statement = conn.prepareStatement(
                                        "INSERT INTO quotes (mid,quote,author,channel,avatar,time) VALUES (" +
                                                "?,?,?,?,?,?" +
                                                ")");
                                statement.setString(1, message.getId());
                                statement.setString(2, message.getRawContent());
                                statement.setString(3, author.substring(1));
                                statement.setString(4, finalChannelTag);
                                statement.setString(5, avatar);
                                statement.setString(6, message.getCreationTime()
                                        .format(DateTimeFormatter.RFC_1123_DATE_TIME));
                                statement.executeUpdate();
                                msg.editMessage("Added!").queue();
                            });
                        } catch (SQLException e) {
                            if (e.getMessage().contains("A UNIQUE constraint failed (UNIQUE constraint failed: quotes.mid)")) {
                                msg.editMessage("Already quoted!").queue();
                                return;
                            }
                            DiscordUtils.updateWithException("Could not SQL the quote!", e, msg);
                        }
                    });
                } else if (args[0].equalsIgnoreCase("remove") && args[1].matches("\\d+")) {
                    int i = Integer.parseInt(args[1]);
                    SQL.executeSQL(conn -> {
                        PreparedStatement statement = conn.prepareStatement(
                                "DELETE FROM quotes WHERE id = ?");
                        statement.setInt(1, i);
                        statement.executeUpdate();
                        msg.editMessage("Removed!").queue();
                    });
                }
                return;
            }
            SQL.executeSQL(conn -> {
                Statement s = conn.createStatement();
                ResultSet set = s.executeQuery("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1");
                if (set.next()) {
                    String author = set.getString("author");
                    String channelTag = set.getString("channel");
                    String avatar = set.getString("avatar");
                    String quote = set.getString("quote");
                    String time = set.getString("time");
                    int id = set.getInt("id");
                    EmbedBuilder builder = DiscordUtils.getEmbedBuilder()
                            .setAuthor(author, null, avatar)
                            .setDescription(quote)
                            .setFooter(time, null)
                            .addField("Channel: ", channelTag, false)
                            .addField("ID: ", String.valueOf(id), false);
                    msg.editMessage(new MessageBuilder().setEmbed(builder.build()).build()).queue();
                } else {
                    msg.editMessage("No quotes!").queue();
                }
            });
        } catch (SQLException e) {
            DiscordUtils.updateWithException("Database error!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "quotes";
    }

    @Override
    public String getUsage() {
        return "Quotes, not to be confused with quote, " +
                "is a way to keep track of stupid sh*t your friends say, just for fun :D\n" +
                "Usage: `quote add MESSAGE_ID` to add a quote, `quote remove QUOTE_ID` to remove quotes," +
                "`quote` to get a random quote, `quote ID` to get the quote under ID or a random one if there is none.";
    }
}
