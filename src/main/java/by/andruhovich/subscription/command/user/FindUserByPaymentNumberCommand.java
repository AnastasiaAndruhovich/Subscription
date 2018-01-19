package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FindUserByPaymentNumberCommand implements BaseCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
