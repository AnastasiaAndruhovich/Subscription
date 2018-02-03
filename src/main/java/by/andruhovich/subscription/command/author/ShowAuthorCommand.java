package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.AuthorService;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

public class ShowAuthorCommand extends BaseCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ShowEntityList.showAuthorList(request, response);
    }

}
