package org.example.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.internal.http2.Http2Connection;
import org.jetbrains.annotations.NotNull;

public class Greeting extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("greet")) return;
        User user = event.getUser();
        event.reply("Hello there " + user.getEffectiveName() + "!").queue();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById("1327776851745181806");
        guild.upsertCommand("greet", "Greets the user").queue();
    }
}
