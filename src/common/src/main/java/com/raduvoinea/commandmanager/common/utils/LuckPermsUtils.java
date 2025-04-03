package com.raduvoinea.commandmanager.common.utils;

import com.raduvoinea.utils.logger.Logger;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import net.luckperms.api.platform.PlayerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LuckPermsUtils {

    private static final String DEFAULT_GROUP_NAME = "default";

    public static boolean checkPermission(Class<?> playerClass, Object player, String permission) {
        try {
            LuckPerms luckPerms = LuckPermsProvider.get();

            //noinspection rawtypes
            PlayerAdapter playerAdapter = luckPerms.getPlayerAdapter(playerClass);
            //noinspection unchecked
            return playerAdapter.getPermissionData(player).checkPermission(permission).asBoolean();
        } catch (Exception error) {
            // No LuckPerms - has no permission
            Logger.error(error);
            return false;
        }
    }

    public static @NotNull Group getGroup(@NotNull UUID userUUID) {
        User user = getUser(userUUID);
        return getGroup(user);
    }

    public static @NotNull Group getGroup(@Nullable User user) {
        LuckPerms luckPerms = LuckPermsProvider.get();

        if (user == null) {
            return getDefaultGroup();
        }

        String groupName = user.getPrimaryGroup();
        Group group = luckPerms.getGroupManager().getGroup(groupName);

        if (group == null) {
            return getDefaultGroup();
        }

        return group;
    }

    public static @NotNull Group getDefaultGroup() {
        LuckPerms luckPerms = LuckPermsProvider.get();

        Group group = luckPerms.getGroupManager().getGroup(DEFAULT_GROUP_NAME);

        if (group == null) {
            throw new IllegalStateException("'default' group not found");
        }

        return group;
    }

    public static String getPrefix(@Nullable User user, @Nullable Group group) {
        return getNodeType(user, group, NodeType.PREFIX);
    }

    public static String getSuffix(@Nullable User user, @Nullable Group group) {
        return getNodeType(user, group, NodeType.SUFFIX);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends ChatMetaNode> String getNodeType(@Nullable User user, @Nullable Group group,
                                                              NodeType<?> nodeType) {
        List<T> userPrefixNodes = new ArrayList<>();
        List<T> groupPrefixNodes = new ArrayList<>();

        if (user != null) {
            userPrefixNodes = (List<T>) new ArrayList<>(user.getNodes(nodeType).stream().toList());
        }
        if (group != null) {
            groupPrefixNodes = (List<T>) new ArrayList<>(group.getNodes(nodeType).stream().toList());
        }

        List<T> prefixNodes = new ArrayList<>();
        prefixNodes.addAll(userPrefixNodes);
        prefixNodes.addAll(groupPrefixNodes);

        prefixNodes.sort(Comparator.comparingInt(ChatMetaNode::getPriority));
        T prefix = prefixNodes.stream().findFirst().orElse(null);

        String prefixString = prefix == null ? "" : prefix.getKey();
        if (prefixString.contains(".")) {
            String[] split = prefixString.split("\\.");
            prefixString = split[split.length - 1];
        }

        return prefixString;
    }


    public static String getPrefix(@NotNull UUID uuid) {
        User user = getUser(uuid);
        Group group = getGroup(user);

        return getPrefix(user, group);
    }

    public static String getSuffix(@NotNull UUID uuid) {
        User user = getUser(uuid);
        Group group = getGroup(user);

        return getSuffix(user, group);
    }

    @SneakyThrows(value = {InterruptedException.class, ExecutionException.class})
    public static @Nullable User getUser(@NotNull UUID userUUID) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user;

        user = luckPerms.getUserManager().getUser(userUUID);

        if (user == null) {
            CompletableFuture<User> completableLPUser = luckPerms.getUserManager().loadUser(userUUID);
            user = completableLPUser.get();
        }

        return user;
    }

    public static int getWeight(Group group) {
        OptionalInt optionalWeight = group.getWeight();

        if (optionalWeight.isEmpty()) {
            return 0;
        }

        return optionalWeight.getAsInt();
    }

    public static int getWeight(User user) {
        Group group = getGroup(user);
        return getWeight(group);
    }

    public static int getWeight(UUID userUUID) {
        Group group = getGroup(userUUID);
        return getWeight(group);
    }

}
