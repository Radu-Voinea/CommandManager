package dev.lightdream.commandmanager.manager;

import dev.lightdream.commandmanager.CommandMain;
import dev.lightdream.commandmanager.annotation.Command;
import dev.lightdream.commandmanager.command.CommonCommand;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandManager {

    public List<CommonCommand> commands = new ArrayList<>();

    public CommandManager(CommandMain main, Object... args) {
        Set<Class<?>> classes = new Reflections(main.getPackageName()).getTypesAnnotatedWith(Command.class);
        Logger.good("Found and registered " + classes.size() + " commands");
        classes.forEach(aClass -> {
            try {
                for (CommonCommand command : commands) {
                    if (command.getClass().getName().equals(aClass.getName())) {
                        return;
                    }
                }
                Object obj;
                Debugger.info(aClass.getName() + " constructors: ");
                for (Constructor<?> constructor : aClass.getDeclaredConstructors()) {
                    StringBuilder parameters = new StringBuilder();
                    for (Class<?> parameter : constructor.getParameterTypes()) {
                        parameters.append(parameter.getName()).append(" ");
                    }
                    if (parameters.toString().equals("")) {
                        Debugger.info("    - zero argument");
                    } else {
                        Debugger.info("    - " + parameters);
                    }
                }
                if (aClass.getDeclaredConstructors()[0].getParameterCount() == 0) {
                    obj = aClass.getDeclaredConstructors()[0].newInstance();
                } else if (aClass.getDeclaredConstructors()[0].getParameterCount() == 1) {
                    obj = aClass.getDeclaredConstructors()[0].newInstance(main);
                } else if (aClass.getDeclaredConstructors()[0].getParameterCount() == 2) {
                    obj = aClass.getDeclaredConstructors()[0].newInstance(main, args);
                } else {
                    Logger.error("Class " + aClass.getName() + " does not have a valid constructor");
                    return;
                }
                if (obj instanceof CommonCommand) {
                    commands.add((CommonCommand) obj);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

}
