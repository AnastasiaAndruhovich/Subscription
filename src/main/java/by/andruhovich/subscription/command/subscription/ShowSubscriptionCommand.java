package by.andruhovich.subscription.command.subscription;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowSubscriptionCommand extends BaseCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ShowEntityList.showSubscriptionList(request, response);
    }
}