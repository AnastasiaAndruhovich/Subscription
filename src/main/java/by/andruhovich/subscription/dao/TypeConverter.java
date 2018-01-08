package by.andruhovich.subscription.dao;

class TypeConverter {
    String convertBooleanToString(boolean b) {
        return (b) ? "y" : "n";
    }

    boolean convertStringToBoolean(String s) {
        return ("y").equals(s);
    }
}
