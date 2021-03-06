package by.andruhovich.subscription.command;

/**
 * Stores command types
 */
public enum CommandType {
    //account
    FIND_ACCOUNT_BY_USER, RECHARGE, TAKE_LOAN,

    //user
    SHOW_USERS, LOGIN, LOGOUT, SIGN_UP, BLOCK_USER, UNBLOCK_USER, UPDATE_USER, FIND_USER_BY_LOGIN,
    FIND_BLOCKED_USERS_BY_ADMIN, FIND_USER, CHANGE_PASSWORD,

    //publication
    SHOW_PUBLICATIONS, ADD_PUBLICATION, DELETE_PUBLICATION, EDIT_PUBLICATION, FIND_PUBLICATION_BY_NAME,
    FIND_PUBLICATION_BY_AUTHOR, FIND_PUBLICATION_BY_GENRE, FIND_PUBLICATION_BY_PUBLICATION_TYPE, PARSE_PUBLICATION,

    //author
    SHOW_AUTHORS, ADD_AUTHOR, DELETE_AUTHOR, EDIT_AUTHOR, PARSE_AUTHOR,

    //genre
    SHOW_GENRES, ADD_GENRE, DELETE_GENRE, EDIT_GENRE, PARSE_GENRE,

    //publication_type
    SHOW_PUBLICATION_TYPES, ADD_PUBLICATION_TYPE, DELETE_PUBLICATION_TYPE, EDIT_PUBLICATION_TYPE, PARSE_PUBLICATION_TYPE,

    //subscription
    SHOW_SUBSCRIPTIONS, ADD_SUBSCRIPTION, FIND_SUBSCRIPTION_BY_USER, DELETE_SUBSCRIPTION,

    //payment
    ADD_PAYMENT, FIND_PAYMENT_BY_USER,
}
