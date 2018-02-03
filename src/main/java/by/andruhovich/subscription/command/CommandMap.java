package by.andruhovich.subscription.command;

import by.andruhovich.subscription.command.author.AddAuthorCommand;
import by.andruhovich.subscription.command.author.DeleteAuthorCommand;
import by.andruhovich.subscription.command.author.ShowAuthorCommand;
import by.andruhovich.subscription.command.genre.AddGenreCommand;
import by.andruhovich.subscription.command.genre.ShowGenreCommand;
import by.andruhovich.subscription.command.publication.*;
import by.andruhovich.subscription.command.publicationtype.ShowPublicationTypeCommand;
import by.andruhovich.subscription.command.user.BlockUserCommand;
import by.andruhovich.subscription.command.user.LoginCommand;
import by.andruhovich.subscription.command.user.LogoutCommand;
import by.andruhovich.subscription.command.user.SignUpCommand;
import by.andruhovich.subscription.exception.UndefinedCommandTechnicalException;

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
        commandMap.put(CommandType.ADD_PUBLICATION, new AddPublicationCommand());
        commandMap.put(CommandType.SIGN_UP, new SignUpCommand());
        commandMap.put(CommandType.LOGOUT, new LogoutCommand());
        commandMap.put(CommandType.ADD_GENRE, new AddGenreCommand());
        commandMap.put(CommandType.ADD_AUTHOR, new AddAuthorCommand());
        commandMap.put(CommandType.DELETE_AUTHOR, new DeleteAuthorCommand());
    }

    private CommandMap() {}

    public static CommandMap getInstance() {
        if (instance == null) {
            instance = new CommandMap();
        }
        return instance;
    }

    public BaseCommand get(String command) throws UndefinedCommandTechnicalException {
        try {
            CommandType key = CommandType.valueOf(command);
            return commandMap.get(key);
        } catch (IllegalArgumentException e) {
            throw new UndefinedCommandTechnicalException("Command " + command + " is undefined");
        }

    }

    public BaseCommand get(CommandType key) {
        return commandMap.get(key);
    }
}
