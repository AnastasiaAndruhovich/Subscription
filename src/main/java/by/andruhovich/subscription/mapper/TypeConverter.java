package by.andruhovich.subscription.mapper;

class TypeConverter {
    static String convertBooleanToString(boolean b) {
        return (b) ? "y" : "n";
    }

    boolean convertStringToBoolean(String s) {
        return ("y").equals(s);
    }
}
