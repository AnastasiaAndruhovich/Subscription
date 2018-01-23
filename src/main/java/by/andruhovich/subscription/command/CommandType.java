package by.andruhovich.subscription.command;

public enum CommandType {
    //user
    LOGIN, SIGN_UP, BLOCK_USER, UNBLOCK_USER, UPDATE_USER, SHOW_USERS, FIND_USER_BY_LOGIN, FIND_USER_BY_ACCOUNT_NUMBER,
    FIND_USER_BY_SUBSCRIPTION_ID, FIND_USER_BY_PAYMENT_NUMBER,

    //publication
    SHOW_PUBLICATIONS, ADD_PUBLICATION, DELETE_PUBLICATION, UPDATE_PUBLICATION, FIND_PUBLICATION_BY_NAME,
    FIND_PUBLICATION_BY_AUTHOR, FIND_PUBLICATION_BY_GENRE,

    //author
    SHOW_AUTHORS, ADD_AUTHOR, DELETE_AUTHOR, UPDATE_AUTHOR,

    //genre
    SHOW_GENRES, ADD_GENRE, DELETE_GENRE, UPDATE_GENRE,

    //publication_type
    SHOW_PUBLICATION_TYPES, ADD_PUBLICATION_TYPE, DELETE_PUBLICATION_TYPE, UPDATE_PUBLICATION_TYPE,
    FIND_PUBLICATION_BY_PUBLICATION_TYPE,

    //subscription
    SHOW_SUBSCRIPTIONS, ADD_SUBSCRIPTION, ACTIVATE_SUBSCRIPTION, DEACTIVATE_SUBSCRIPTION,
}
