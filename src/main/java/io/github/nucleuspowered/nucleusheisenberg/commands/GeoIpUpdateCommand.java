/*
 * This file is part of heisenberg, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleusheisenberg.commands;

import io.github.nucleuspowered.nucleusheisenberg.NucleusHeisenberg;
import io.github.nucleuspowered.nucleusheisenberg.services.GeoIpDatabaseHandler;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.annotation.NonnullByDefault;

@NonnullByDefault
public class GeoIpUpdateCommand implements CommandExecutor {

    private final NucleusHeisenberg plugin;
    private static final Text STARTING = Text.of(TextColors.YELLOW, "Starting download of GeoIP databases.");
    private static final Text COMPLETE = Text.of(TextColors.YELLOW, "Update of GeoIP databases has completed.");
    private static final Text LICENCE = Text.of(TextColors.YELLOW, "You must accept the GeoIP module licence in the main configration file before "
            + "downloading the IP location databases.");

    public GeoIpUpdateCommand(NucleusHeisenberg plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(STARTING);
        try {
            this.plugin.getHandler().load(GeoIpDatabaseHandler.LoadType.DOWNLOAD);
            src.sendMessage(COMPLETE);
        } catch (IllegalStateException e) {
            src.sendMessage(LICENCE);
        } catch (Exception e) {
            throw new CommandException(Text.of(TextColors.RED, "Could not download databases."), e);
        }

        return CommandResult.success();
    }
}
