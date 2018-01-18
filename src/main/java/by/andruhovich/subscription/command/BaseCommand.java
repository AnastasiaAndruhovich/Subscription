package by.andruhovich.subscription.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BaseCommand {
    String execute(HttpServletRequest request, HttpServletResponse response);
}
