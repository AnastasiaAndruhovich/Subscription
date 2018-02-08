package by.andruhovich.subscription.command;

import by.andruhovich.subscription.command.account.FindAccountByUserCommand;
import by.andruhovich.subscription.command.account.RechargeCommand;
import by.andruhovich.subscription.command.account.TakeLoanCommand;
import by.andruhovich.subscription.command.author.*;
import by.andruhovich.subscription.command.genre.*;
import by.andruhovich.subscription.command.payment.AddPaymentCommand;
import by.andruhovich.subscription.command.payment.FindPaymentByUserCommand;
import by.andruhovich.subscription.command.publication.*;
import by.andruhovich.subscription.command.publicationtype.*;
import by.andruhovich.subscription.command.subscription.AddSubscriptionCommand;
import by.andruhovich.subscription.command.subscription.DeleteSubscriptionCommand;
import by.andruhovich.subscription.command.subscription.FindSubscriptionByUserCommand;
import by.andruhovich.subscription.command.subscription.ShowSubscriptionCommand;
import by.andruhovich.subscription.command.user.*;
import by.andruhovich.subscription.exception.UndefinedCommandTechnicalException;

import java.util.EnumMap;

public class CommandMap {
    private static EnumMap<CommandType, BaseCommand> commandMap;
    private static CommandMap instance;

    static {
        commandMap = new EnumMap<>(CommandType.class);
        commandMap.put(CommandType.LOGIN, new LoginCommand());
        commandMap.put(CommandType.SIGN_UP, new SignUpCommand());
        commandMap.put(CommandType.LOGOUT, new LogoutCommand());
        commandMap.put(CommandType.BLOCK_USER, new BlockUserCommand());
        commandMap.put(CommandType.UNBLOCK_USER, new UnblockUserCommand());
        commandMap.put(CommandType.SHOW_PUBLICATIONS, new ShowPublicationCommand());
        commandMap.put(CommandType.SHOW_GENRES, new ShowGenreCommand());
        commandMap.put(CommandType.SHOW_PUBLICATION_TYPES, new ShowPublicationTypeCommand());
        commandMap.put(CommandType.SHOW_AUTHORS, new ShowAuthorCommand());
        commandMap.put(CommandType.SHOW_USERS, new ShowUserCommand());
        commandMap.put(CommandType.FIND_PUBLICATION_BY_GENRE, new FindPublicationByGenreCommand());
        commandMap.put(CommandType.FIND_PUBLICATION_BY_AUTHOR, new FindPublicationByAuthorCommand());
        commandMap.put(CommandType.FIND_PUBLICATION_BY_PUBLICATION_TYPE, new FindPublicationByPublicationTypeCommand());
        commandMap.put(CommandType.ADD_GENRE, new AddGenreCommand());
        commandMap.put(CommandType.ADD_AUTHOR, new AddAuthorCommand());
        commandMap.put(CommandType.ADD_PUBLICATION, new AddPublicationCommand());
        commandMap.put(CommandType.ADD_PUBLICATION_TYPE, new AddPublicationTypeCommand());
        commandMap.put(CommandType.DELETE_AUTHOR, new DeleteAuthorCommand());
        commandMap.put(CommandType.DELETE_GENRE, new DeleteGenreCommand());
        commandMap.put(CommandType.DELETE_PUBLICATION_TYPE, new DeletePublicationTypeCommand());
        commandMap.put(CommandType.DELETE_PUBLICATION, new DeletePublicationCommand());
        commandMap.put(CommandType.EDIT_AUTHOR, new EditAuthorCommand());
        commandMap.put(CommandType.PARSE_AUTHOR, new ParseAuthorCommand());
        commandMap.put(CommandType.EDIT_GENRE, new EditGenreCommand());
        commandMap.put(CommandType.PARSE_GENRE, new ParseGenreCommand());
        commandMap.put(CommandType.EDIT_PUBLICATION_TYPE, new EditPublicationTypeCommand());
        commandMap.put(CommandType.PARSE_PUBLICATION_TYPE, new ParsePublicationTypeCommand());
        commandMap.put(CommandType.EDIT_PUBLICATION, new EditPublicationCommand());
        commandMap.put(CommandType.PARSE_PUBLICATION, new ParsePublicationCommand());
        commandMap.put(CommandType.FIND_ACCOUNT_BY_USER, new FindAccountByUserCommand());
        commandMap.put(CommandType.RECHARGE, new RechargeCommand());
        commandMap.put(CommandType.TAKE_LOAN, new TakeLoanCommand());
        commandMap.put(CommandType.FIND_SUBSCRIPTION_BY_USER, new FindSubscriptionByUserCommand());
        commandMap.put(CommandType.ADD_SUBSCRIPTION, new AddSubscriptionCommand());
        commandMap.put(CommandType.ADD_PAYMENT, new AddPaymentCommand());
        commandMap.put(CommandType.DELETE_SUBSCRIPTION, new DeleteSubscriptionCommand());
        commandMap.put(CommandType.SHOW_SUBSCRIPTIONS, new ShowSubscriptionCommand());
        commandMap.put(CommandType.FIND_USER, new FindUserCommand());
        commandMap.put(CommandType.UPDATE_USER, new UpdateUserCommand());
        commandMap.put(CommandType.CHANGE_PASSWORD, new ChangePasswordCommand());
        commandMap.put(CommandType.FIND_PAYMENT_BY_USER, new FindPaymentByUserCommand());
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
