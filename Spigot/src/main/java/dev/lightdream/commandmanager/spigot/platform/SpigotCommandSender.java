package dev.lightdream.commandmanager.spigot.platform;

import dev.lightdream.commandmanager.common.platform.PlatformCommandSender;
import org.bukkit.command.CommandSender;

public class SpigotCommandSender extends PlatformCommandSender<CommandSender> {
    public SpigotCommandSender(CommandSender commandSource) {
        super(commandSource);
    }
}
