package by.andruhovich.subscription.tag;

import by.andruhovich.subscription.type.ClientType;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class HeaderTag extends TagSupport {
    private static final String CLIENT_TYPE = "clientType";

    @Override
    public int doStartTag() throws JspException {
        ClientType type = (ClientType) pageContext.getSession().getAttribute(CLIENT_TYPE);
        String header;

        if (type.equals(ClientType.ADMIN)) {
            header = "../../../static/admin/header.jsp";
        } else if (type.equals(ClientType.USER)) {
            header = "../../../static/user/header.jsp";
        } else {
            header = "../../../static/guest/header.jsp";
        }

        try {
            pageContext.include(header);
        } catch (IOException | ServletException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}
