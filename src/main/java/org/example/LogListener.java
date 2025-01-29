package org.example;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.nio.channels.Channel;

public class LogListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        System.out.printf("[%s: %s] %s: %s\n",
                event.getGuild().getName(),
                event.getChannel().getName(),
                event.getAuthor().getName(),
                event.getMessage().getContentDisplay());
    }

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            System.out.printf("Logged in [%s] server!\n", guild.getName());
        }
    }
}
