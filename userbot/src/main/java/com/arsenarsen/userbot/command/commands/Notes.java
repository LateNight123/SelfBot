package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.sql.SQL;
import com.arsenarsen.userbot.util.DiscordUtils;
import com.arsenarsen.userbot.util.VerticalAligner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class Notes implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) { // the T​odo.java copy paste doe
        try {
            SQL.executeSQL(connection -> connection.createStatement().execute("CREATE TABLE IF NOT EXISTS notes(\n" +
                    "   id INTEGER PRIMARY KEY,\n" +
                    "   note VARCHAR(80) NOT NULL\n" +
                    ")"));
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    SQL.executeSQL(connection -> {
                        connection.createStatement().execute("DELETE FROM notes");
                        msg.editMessage("Cleared!").queue();
                    });
                }
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
                SQL.executeSQL(connection -> {
                    PreparedStatement st = connection.prepareStatement("INSERT INTO notes (note) VALUES (" +
                            "   ?" +
                            ")");
                    String note = DiscordUtils.substringCommand(this, msg, false);
                    st.setString(1, note);
                    st.executeUpdate();
                    msg.editMessage("Success!").queue();
                });
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove") && args[1].matches("\\d*")) {
                    SQL.executeSQL(connection -> {
                        int id = Integer.parseInt(args[1]);
                        PreparedStatement st = connection.prepareStatement("DELETE FROM notes WHERE id = ?");
                        st.setInt(1, id);
                        st.execute();
                        msg.editMessage("Success!").queue();
                    });
                }
            } else {
                SQL.executeSQL(connection -> {
                    Statement s = connection.createStatement();
                    s.execute("SELECT * FROM notes");
                    ResultSet resultSet = s.getResultSet();
                    StringBuilder todo = new StringBuilder().append("Your notes: ```fix\n");
                    Map<String, String> todos = new LinkedHashMap<>();
                    while (resultSet.next()) {
                        todos.put(Integer.toString(resultSet.getInt("id")), resultSet.getString("task"));
                    }
                    todo.append(VerticalAligner.align(todos, "|"));
                    todo.append("\n```");
                    msg.editMessage(todo.toString()).queue();
                });
            }
        } catch (SQLException e) {
            DiscordUtils.updateWithException("Could not get notes :(", e, msg);
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
