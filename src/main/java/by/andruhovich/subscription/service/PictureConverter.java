package by.andruhovich.subscription.service;

import by.andruhovich.subscription.exception.ServiceTechnicalException;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PictureConverter {
    static byte[] convertFileToByteArray(InputStream inputStream) throws ServiceTechnicalException {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    static void convertByteArrayToFile(FileOutputStream file, byte[] array) throws ServiceTechnicalException {
        try {
            file.write(array);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
