package by.andruhovich.subscription.mapper;

/**
 * Provides methods to convert special symbols from database to boolean type and back
 */
class TypeConverter {
    static String convertBooleanToString(boolean b) {
        return (b) ? "y" : "n";
    }

    static boolean convertStringToBoolean(String s) {
        return ("y").equals(s);
    }
}
