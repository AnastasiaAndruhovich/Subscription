package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.command.common.ShowEntityList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Finds page according to relevant command show author entity
 */
public class ShowAuthorCommand extends BaseCommand {

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        return new CommandResult(TransitionType.FORWARD, ShowEntityList.showAuthorList(request, response));
    }

}
