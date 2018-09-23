/*
 * This file is part of heisenberg, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleusheisenberg.listeners;

import com.maxmind.geoip2.record.Country;
import io.github.nucleuspowered.nucleusheisenberg.NucleusHeisenberg;
import io.github.nucleuspowered.nucleusheisenberg.config.GeoIpConfig;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.type.PermissionMessageChannel;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class GeoIpListener {

    private boolean isPrinted = false;
    private final NucleusHeisenberg plugin;

    public GeoIpListener(NucleusHeisenberg plugin) {
        this.plugin = plugin;
    }

    @Listener(order = Order.LAST)
    public void onPlayerJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        Sponge.getScheduler().createAsyncExecutor(this.plugin).execute(() -> {
            try {
                Optional<Country> result = this.plugin.getHandler().getDetails(event.getTargetEntity().getConnection().getAddress().getAddress()).get();
                if (result.isPresent()) {
                    new PermissionMessageChannel("heisenberg.login")
                        .send(
                            Text.of(TextColors.YELLOW, "[GeoIP] ", player
                                            .get(Keys.DISPLAY_NAME)
                                            .orElseGet(() -> Text.of(player.getName())),
                                    TextColors.YELLOW,
                                    "is connecting from",
                                    TextColors.GREEN,
                                    result.get().getName(),
                                    TextColors.YELLOW,
                                    ".")
                        );
                } else {
                    new PermissionMessageChannel("heisenberg.login")
                        .send(
                                Text.of(TextColors.YELLOW, "[GeoIP] Cannot determine where ", player
                                                .get(Keys.DISPLAY_NAME)
                                                .orElseGet(() -> Text.of(player.getName())),
                                        TextColors.YELLOW,
                                        "is connecting from.")
                        );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean shouldEnable() {
        GeoIpConfig gic = this.plugin.getConfig();
        if (gic.isAcceptLicence()) {
            if (!this.isPrinted) {
                this.plugin.getLogger()
                    .info("GeoIP is enabled. Nucleus makes use of GeoLite2 data created by MaxMind, available from http://www.maxmind.com");
                this.isPrinted = true;
            }

            return gic.isAlertOnLogin();
        }

        return false;
    }
}
