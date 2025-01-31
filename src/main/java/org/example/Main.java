package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.example.commands.Chat;
import org.example.commands.Greeting;

import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        if (token == null) {
            System.err.println("DISCORD_TOKEN environment variable not set");
            System.exit(1);
        }
        JDA jda = JDABuilder.createLight(token, EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .addEventListeners(new LogListener())
                .addEventListeners(new Greeting())
                .addEventListeners(new Chat())
                .build();


        // Register your commands to make them visible globally on Discord:

        CommandListUpdateAction commands = jda.updateCommands();

        // Add all your commands on this action instance
        commands.addCommands(
                Commands.slash("say", "Makes the bot say what you tell it to")
                        .addOption(STRING, "content", "What the bot should say", true), // Accepting a user input
                Commands.slash("leave", "Makes the bot leave the server")
                        .setGuildOnly(true) // this doesn't make sense in DMs
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED) // only admins should be able to use this command.
        );

        // Then finally send your commands to discord using the API
        commands.queue();
    }
}