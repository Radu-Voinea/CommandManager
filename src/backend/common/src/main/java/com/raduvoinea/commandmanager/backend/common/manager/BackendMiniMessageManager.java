package com.raduvoinea.commandmanager.backend.common.manager;

import com.raduvoinea.commandmanager.common.manager.CommonMiniMessageManager;
import net.kyori.adventure.platform.modcommon.MinecraftAudiences;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class BackendMiniMessageManager extends CommonMiniMessageManager<Component> {

	private final MinecraftAudiences audiences;

	public BackendMiniMessageManager(MinecraftAudiences audiences) {
		this.audiences = audiences;
	}

	@Override
	public @NotNull Component toNative(@NotNull net.kyori.adventure.text.Component component) {
		return audiences.asNative(component);
	}

}
