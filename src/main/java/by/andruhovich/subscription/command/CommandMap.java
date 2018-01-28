package by.andruhovich.subscription.command;

import by.andruhovich.subscription.command.author.ShowAuthorCommand;
import by.andruhovich.subscription.command.genre.ShowGenreCommand;
import by.andruhovich.subscription.command.publication.FindPublicationByAuthorCommand;
import by.andruhovich.subscription.command.publication.FindPublicationByGenreCommand;
import by.andruhovich.subscription.command.publication.FindPublicationByPublicationTypeCommand;
import by.andruhovich.subscription.command.publication.ShowPublicationCommand;
import by.andruhovich.subscription.command.publicationtype.ShowPublicationTypeCommand;
import by.andruhovich.subscription.command.user.BlockUserCommand;
import by.andruhovich.subscription.command.user.LoginCommand;

import java.util.EnumMap;

public class CommandMap {
    private static EnumMap<CommandType, BaseCommand> commandMap;
    private static CommandMap instance;

    static {
        commandMap = new EnumMap<>(CommandType.class);
        commandMap.put(CommandType.LOGIN, new LoginCommand());
        commandMap.put(CommandType.BLOCK_USER, new BlockUserCommand());
        commandMap.put(CommandType.SHOW_PUBLICATIONS, new ShowPublicationCommand());
        commandMap.put(CommandType.SHOW_GENRES, new ShowGenreCommand());
        commandMap.put(CommandType.SHOW_PUBLICATION_TYPES, new ShowPublicationTypeCommand());
        commandMap.put(CommandType.SHOW_AUTHORS, new ShowAuthorCommand());
        commandMap.put(CommandType.FIND_PUBLICATIONS_BY_GENRE, new FindPublicationByGenreCommand());
        commandMap.put(CommandType.FIND_PUBLICATIONS_BY_AUTHOR, new FindPublicationByAuthorCommand());
        commandMap.put(CommandType.FIND_PUBLICATIONS_BY_PUBLICATION_TYPE, new FindPublicationByPublicationTypeCommand());
    }

    private CommandMap() {}

    public static CommandMap getInstance() {
        if (instance == null) {
            instance = new CommandMap();
        }
        return instance;
    }

    public BaseCommand get(String command) {
        //try catch
        CommandType key = CommandType.valueOf(command);
        return commandMap.get(key);
    }

    public BaseCommand get(CommandType key) {
        return commandMap.get(key);
    }

    public BaseCommand put(CommandType key, BaseCommand value) {
        return commandMap.put(key, value);
    }

    public BaseCommand remove(CommandType key) {
        return commandMap.remove(key);
    }
}
