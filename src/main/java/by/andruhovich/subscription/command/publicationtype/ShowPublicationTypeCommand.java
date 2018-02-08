package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowPublicationTypeCommand extends BaseCommand{
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ShowEntityList.showPublicationTypeList(request, response);
    }
}
