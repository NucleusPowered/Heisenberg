/*
 * This file is part of heisenberg, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleusheisenberg.commands;

import com.maxmind.geoip2.record.Country;
import io.github.nucleuspowered.nucleusheisenberg.NucleusHeisenberg;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Optional;

@NonnullByDefault
public class GeoIpCommand implements CommandExecutor {

    private final NucleusHeisenberg plugin;

    public GeoIpCommand(NucleusHeisenberg plugin) {
        this.plugin = plugin;
    }

    @Override public CommandResult execute(CommandSource src, CommandContext args) {
        Player player = args.requireOne("player");
        try {
            Optional<Country> country = this.plugin.getHandler().getDetails(player.getConnection().getAddress().getAddress()).get();
            if (country.isPresent()) {
                src.sendMessage(
                        Text.of(TextColors.YELLOW, "[GeoIP] ", player
                                        .get(Keys.DISPLAY_NAME)
                                        .orElseGet(() -> Text.of(player.getName())),
                                TextColors.YELLOW,
                                "is connecting from",
                                TextColors.GREEN,
                                country.get().getName(),
                                TextColors.YELLOW,
                                "."));
            }
            return CommandResult.success();
        } catch (Exception ex) {
            // ignored
        }

        src.sendMessage(
                Text.of(TextColors.YELLOW, "[GeoIP] Cannot determine where ", player
                                .get(Keys.DISPLAY_NAME)
                                .orElseGet(() -> Text.of(player.getName())),
                        TextColors.YELLOW,
                        "is connecting from."));
        return CommandResult.success();
    }
}
