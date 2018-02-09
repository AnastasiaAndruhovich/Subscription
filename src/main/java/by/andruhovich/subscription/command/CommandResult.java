package by.andruhovich.subscription.command;

public class CommandResult {
    private TransitionType transitionType;
    private String page;

    public CommandResult(TransitionType transitionType, String page) {
        this.transitionType = transitionType;
        this.page = page;
    }

    public TransitionType getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(TransitionType transitionType) {
        this.transitionType = transitionType;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
