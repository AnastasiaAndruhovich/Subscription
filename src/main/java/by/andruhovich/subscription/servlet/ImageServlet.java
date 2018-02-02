package by.andruhovich.subscription.servlet;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.service.UserService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/imageController")
@MultipartConfig
public class ImageServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ImageServlet.class);

    @Override
    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String PUBLICATION_ID_ATTRIBUTE = "publicationId";
        final String PUBLICATION_ATTRIBUTE = "publication";

        final String ERROR_PAGE = "/jsp/error/error.jsp";
        final String ADD_PUBLICATION_PICTURE_ADMIN_PAGE = "path.page.admin.addPublicationPicture";

        PageManager pageManager = PageManager.getInstance();

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        PublicationService publicationService = new PublicationService();
        String publicationId = request.getParameter(PUBLICATION_ID_ATTRIBUTE);

        try {
            List<FileItem> fields = upload.parseRequest(request);
            if (fields.size() != 0) {
                for (FileItem item : fields) {
                    if (item.getSize() != 0) {
                        byte[] picture = item.get();
                        String pictureName = item.getName();
                        if (!publicationService.insertImage(publicationId, picture, pictureName)) {
                            response.sendRedirect(ERROR_PAGE);
                        }
                    }
                }
            }
            int id = Integer.parseInt(publicationId);
            Publication publication = publicationService.findPublicationById(id);
            request.setAttribute(PUBLICATION_ATTRIBUTE, publication);
            String page = pageManager.getProperty(ADD_PUBLICATION_PICTURE_ADMIN_PAGE);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } catch (FileUploadException e) {
            LOGGER.log(Level.ERROR, "Error image uploading");
            response.sendRedirect(ERROR_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            response.sendRedirect(ERROR_PAGE);
        } catch (MissingResourceTechnicalException | ServletException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            response.sendRedirect(ERROR_PAGE);
        }
    }

    @Override
    public void destroy() {
    }
}
