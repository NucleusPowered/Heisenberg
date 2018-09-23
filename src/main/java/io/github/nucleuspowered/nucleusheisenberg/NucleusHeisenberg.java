/*
 * This file is part of heisenberg, licensed under the MIT License (MIT). See the LICENSE.txt file
 * at the root of this project for more details.
 */
package io.github.nucleuspowered.nucleusheisenberg;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.github.nucleuspowered.nucleusheisenberg.commands.GeoIpCommand;
import io.github.nucleuspowered.nucleusheisenberg.commands.GeoIpUpdateCommand;
import io.github.nucleuspowered.nucleusheisenberg.config.GeoIpConfig;
import io.github.nucleuspowered.nucleusheisenberg.listeners.GeoIpListener;
import io.github.nucleuspowered.nucleusheisenberg.services.GeoIpDatabaseHandler;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Path;

@Plugin(
        id = "nucleus-heisenberg",
        name = PluginInfo.NAME,
        version = PluginInfo.VERSION,
        description = PluginInfo.DESCRIPTION
)
public class NucleusHeisenberg {

    private final Logger logger;
    private final Path configDirectory;
    private final ConfigurationLoader<CommentedConfigurationNode> loader;
    private final ObjectMapper<GeoIpConfig>.BoundInstance config;
    private final GeoIpDatabaseHandler handler;
    private final GeoIpListener listener;

    @Inject
    public NucleusHeisenberg(Logger logger,
            @ConfigDir(sharedRoot = false) Path configDir,
            @DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> loader) throws ObjectMappingException {
        this.logger = logger;
        this.config = ObjectMapper.forObject(new GeoIpConfig());
        this.loader = loader;
        this.configDirectory = configDir;
        this.handler = new GeoIpDatabaseHandler(this, this.configDirectory);
        this.listener = new GeoIpListener(this);
    }

    @Listener
    public void onServerInit(GameInitializationEvent event) {
        Sponge.getCommandManager().register(
                this,
                CommandSpec.builder()
                    .permission("heisenberg.lookup")
                    .child(
                            CommandSpec.builder()
                                    .permission("heisenberg.update")
                                    .executor(new GeoIpUpdateCommand(this))
                                    .build()
                            , "update"
                    )
                    .child(
                            CommandSpec.builder()
                                    .executor((s, a) -> {
                                        reload();
                                        s.sendMessage(Text.of("Heisenberg Reloaded"));
                                        return CommandResult.success();
                                    })
                                    .build()
                            , "reload"
                    )
                    .arguments(GenericArguments.player(Text.of("player")))
                    .executor(new GeoIpCommand(this))
                    .build(),
                "geoip"
        );

        try {
            CommentedConfigurationNode cn = this.loader.load();

            CommentedConfigurationNode defaults = this.loader.createEmptyNode();
            defaults.setValue(TypeToken.of(GeoIpConfig.class), new GeoIpConfig());
            cn.mergeValuesFrom(defaults);
            this.config.populate(cn);
            this.loader.save(cn);
        } catch (IOException | ObjectMappingException ex) {
            this.logger.error("Could not read/save GeoIP configuration.");
        }
    }

    @Listener
    public void onServerReload(GameReloadEvent event) {
        reload();
    }

    private void reload() {
        try {
            this.config.populate(this.loader.load());
        } catch (Exception ex) {
            this.logger.error("Could not reload the GeoIP config.", ex);
        }

        listener();
    }

    private void listener() {
        Sponge.getEventManager().unregisterListeners(this.listener);
        if (this.listener.shouldEnable()) {
            Sponge.getEventManager().registerListeners(this, this.listener);
        }
    }

    public Path getConfigDirectory() {
        return this.configDirectory;
    }

    public GeoIpConfig getConfig() {
        return this.config.getInstance();
    }

    public GeoIpDatabaseHandler getHandler() {
        return this.handler;
    }

    public Logger getLogger() {
        return this.logger;
    }
}
