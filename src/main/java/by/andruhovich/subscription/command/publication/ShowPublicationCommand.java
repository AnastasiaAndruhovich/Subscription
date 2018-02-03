package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPublicationCommand extends BaseCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ShowEntityList.showPublicationList(request, response);
    }
}
