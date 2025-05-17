package com.raduvoinea.commandmanager.backend.fabric;

import com.raduvoinea.commandmanager.backend.common.manager.CommonBackendCommandManager;
import com.raduvoinea.commandmanager.common.config.CommandManagerConfig;
import com.raduvoinea.utils.dependency_injection.Injector;
import com.raduvoinea.utils.generic.dto.Holder;
import com.raduvoinea.utils.reflections.Reflections;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class FabricCommandManager extends CommonBackendCommandManager {
	public FabricCommandManager(Reflections.@NotNull Crawler reflectionsCrawler, @NotNull CommandManagerConfig config,
	                            @NotNull MinecraftServer server, @NotNull Holder<Injector> injector) {
		super(reflectionsCrawler, config, server, injector, MinecraftServerAudiences.of(server));
	}
}
