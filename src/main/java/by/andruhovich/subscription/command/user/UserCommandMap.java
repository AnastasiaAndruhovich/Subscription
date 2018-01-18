package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;

import java.util.EnumMap;

public class UserCommandMap {
    private static EnumMap<UserCommandType, BaseCommand> commandMap;
    private static UserCommandMap instance;

    static {
        commandMap = new EnumMap<>(UserCommandType.class);
        commandMap.put(UserCommandType.LOGIN, new LoginCommand());
    }

    private UserCommandMap() {}

    public static UserCommandMap getInstance() {
        if (instance == null) {
            instance = new UserCommandMap();
        }
        return instance;
    }

    public BaseCommand get(String command) {
        //try catch
        UserCommandType key = UserCommandType.valueOf(command);
        return commandMap.get(key);
    }

    public BaseCommand get(UserCommandType key) {
        return commandMap.get(key);
    }

    public BaseCommand put(UserCommandType key, BaseCommand value) {
        return commandMap.put(key, value);
    }

    public BaseCommand remove(UserCommandType key) {
        return commandMap.remove(key);
    }
}
