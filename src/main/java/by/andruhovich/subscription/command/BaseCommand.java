package by.andruhovich.subscription.command;

import javax.servlet.http.HttpServletRequest;

public interface BaseCommand {
    String execute(HttpServletRequest request);
}
