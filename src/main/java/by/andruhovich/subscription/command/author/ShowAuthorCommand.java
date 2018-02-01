package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
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
    private AuthorService authorService = new AuthorService();

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_COUNT = "pageCount";
    private static final String PUBLICATION_USER_PAGE = "path.page.user.authorList";
    private static final String PUBLICATION_ADMIN_PAGE = "path.page.admin.authorList";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "authors";
    private static final String INFORMATION_MESSAGE_ATTRIBUTE = "informationIsAbsent";
    private static final String PUBLICATION_MESSAGE = "message.informationIsAbsent";
    private static final String ERROR_PAGE = "path.page.error";
    private static final String CLIENT_TYPE = "clientType";
    private static final String LOCALE = "locale";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Author> authors = authorService.showAuthors(pageNumber);
            if (!authors.isEmpty()) {
                int pageCount = authorService.findAuthorPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, authors);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            } else {
                request.setAttribute(INFORMATION_MESSAGE_ATTRIBUTE, localeManager.getProperty(PUBLICATION_MESSAGE));
            }

            HttpSession session = request.getSession();
            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(PUBLICATION_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(PUBLICATION_USER_PAGE);
            }
        } catch (ServiceTechnicalException | MissingResourceTechnicalException e) {
            //log
            try {
                page = pageManager.getProperty(ERROR_PAGE);
            } catch (MissingResourceTechnicalException e1) {
                //?????
                page = null;
            }
        }
        return page;
    }
}
