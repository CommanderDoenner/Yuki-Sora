package botApplication.discApplication.commands;

import botApplication.discApplication.librarys.DiscApplicationServer;
import botApplication.discApplication.librarys.DiscApplicationUser;
import core.Engine;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.awt.*;

public class DiscCmdAutoChannel implements DiscCommand {
    @Override
    public boolean calledServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        return engine.getDiscEngine().getUtilityBase().userHasGuildAdminPermission(event.getMember(), event.getGuild(), event.getChannel());
    }

    @Override
    public void actionServer(String[] args, GuildMessageReceivedEvent event, DiscApplicationServer server, DiscApplicationUser user, Engine engine) {
        if (args.length >= 1){
            switch (args[0]){
                case "add":
                    if(args.length >= 2){
                        VoiceChannel newAutoChannel = event.getGuild().getVoiceChannelById(args[1]);
                        if(newAutoChannel != null){
                            server.getAutoChannels().add(newAutoChannel.getId());
                            engine.getDiscEngine().getTextUtils().sendSucces("Autochannel erfolgreich erstellt!", event.getChannel());
                        } else {
                            engine.getDiscEngine().getTextUtils().sendError("Der Channel existiert nicht!", event.getChannel(), false);
                        }
                    } else {
                        engine.getDiscEngine().getTextUtils().sendError("Zu wenig argumente!", event.getChannel(), false);
                    }
                    break;

                case "remove":
                    if(args.length >= 2){
                        VoiceChannel newAutoChannel = event.getGuild().getVoiceChannelById(args[1]);
                        if(newAutoChannel != null){
                            newAutoChannel.getManager().setTopic("").complete();
                            for (String vc:server.getAutoChannels()) {
                                if(vc.equals(newAutoChannel.getId())){
                                    server.getAutoChannels().remove(vc);
                                    break;
                                }
                            }
                            engine.getDiscEngine().getTextUtils().sendSucces("Autochannel erfolgreich entfernt!", event.getChannel());
                        } else {
                            engine.getDiscEngine().getTextUtils().sendError("Der Channel existiert nicht!", event.getChannel(), false);
                        }
                    } else {
                        engine.getDiscEngine().getTextUtils().sendError("Zu wenig argumente!", event.getChannel(), false);
                    }
                    break;

                case "list":
                    String channels = "**Auto Channels**\n\n";
                    int c = 1;
                    for (String vc:server.getAutoChannels()) {
                        channels = channels + c + ": `[" + event.getGuild().getVoiceChannelById(vc).getName() + "]`\n";
                        c++;
                    }
                    engine.getDiscEngine().getTextUtils().sendCustomMessage(channels, event.getChannel(), "Auto channels", Color.blue);
                    break;
            }
        } else {
            engine.getDiscEngine().getTextUtils().sendError("Zu wenig argumente!", event.getChannel(), false);
        }
    }

    @Override
    public boolean calledPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {
        return false;
    }

    @Override
    public void actionPrivate(String[] args, PrivateMessageReceivedEvent event, DiscApplicationUser user, Engine engine) {

    }

    @Override
    public String help(Engine engine) {
        return "add <VC-ID> - Fügt einen Auto-Channel hinzu\nremove <VC-ID> - entfernt einen Auto-Channel\nlist - zeigt eine Liste der Auto-Channels";
    }

    @Override
    public void actionTelegram(Member member, Engine engine, DiscApplicationUser user, String[] args) {

    }
}
