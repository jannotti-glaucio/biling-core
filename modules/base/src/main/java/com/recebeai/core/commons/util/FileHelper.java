package tech.jannotti.billing.core.commons.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public class FileHelper {

    public static File getFileFromClasspath(String resourcePath) throws FileNotFoundException {

        URL url = FileHelper.class.getResource(resourcePath);
        if (url == null)
            throw new FileNotFoundException("Arquivo [" + resourcePath + "] nao existe");

        String realPath = FileHelper.class.getResource(resourcePath).getFile();

        File file = new File(realPath);
        if (!file.exists())
            throw new FileNotFoundException("Arquivo [" + resourcePath + "] nao existe");
        else
            return file;
    }

}