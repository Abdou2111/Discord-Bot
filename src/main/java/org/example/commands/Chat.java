package org.example.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.OpenAIClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Chat extends ListenerAdapter {
    private boolean aiMode = false;
    private User currentUser;
    private TextChannel currentChannel;
    private final OpenAIClient client = new OpenAIClient();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("chat")) return;
        currentUser = event.getUser();
        currentChannel = Objects.requireNonNull(event.getGuild()).getTextChannelById(event.getChannel().getId());
        assert currentChannel != null;
        currentChannel.sendMessage("Welcome to AI mode, " + currentUser.getAsMention() + "! How can I assist you today?").queue();
        aiMode = true;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!aiMode || !event.getAuthor().equals(currentUser) || !event.getChannel().equals(currentChannel)) return;
        String input = event.getMessage().getContentRaw();
        if (input.equalsIgnoreCase("exit")) {
            currentChannel.sendMessage("Exiting AI mode, see you next time!").queue();
            aiMode = false;
            return;
        }
        try {
            List<String> responses = client.getCompletionContent(input);
            for (String response : responses) {
                currentChannel.sendMessage(response).queue();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            currentChannel.sendMessage("An error occurred while processing your request.").queue();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Guild guild = event.getJDA().getGuildById("1327776851745181806");
        assert guild != null;
        guild.upsertCommand("chat", "Starts a conversation with the user").queue();
    }
}