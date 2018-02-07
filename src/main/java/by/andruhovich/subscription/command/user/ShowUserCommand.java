package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowUserCommand extends BaseCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ShowEntityList.showUserList(request, response);
    }
}
