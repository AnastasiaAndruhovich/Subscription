package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;

import java.util.EnumMap;

public class PublicationCommandMap {
    private static EnumMap<PublicationCommandType, BaseCommand> commandMap;
    private static PublicationCommandMap instance;

    static {
        commandMap = new EnumMap<>(PublicationCommandType.class);
        commandMap.put(PublicationCommandType.SHOW_PUBLICATIONS, new ShowPublicationCommand());
    }

    private PublicationCommandMap() {}

    public static PublicationCommandMap getInstance() {
        if (instance == null) {
            instance = new PublicationCommandMap();
        }
        return instance;
    }

    public BaseCommand get(String command) {
        //try catch
        PublicationCommandType key = PublicationCommandType.valueOf(command);
        return commandMap.get(key);
    }

    public BaseCommand get(PublicationCommandType key) {
        return commandMap.get(key);
    }

    public BaseCommand put(PublicationCommandType key, BaseCommand value) {
        return commandMap.put(key, value);
    }

    public BaseCommand remove(PublicationCommandType key) {
        return commandMap.remove(key);
    }
}
