package com.company;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import static java.nio.file.StandardCopyOption.*;


class FilesForMove {
    private List<String> fileList;

    public void setFileList(List<String> _fileList)
    {
        fileList = _fileList;
    }

    public int makeMove(String _destination, boolean _overwriteExisting)
    {
        ListIterator<String>    listIterator    = fileList.listIterator();
        int                     ret             = 0;

        while (listIterator.hasNext()) {
            String tmpFileName = listIterator.next();
            this.copyFile(tmpFileName, _destination, _overwriteExisting);
            ret++;
        }
        return ret;
    }

    public static List<String> scanFolder(String _fromFolder, boolean _recursiveSearch)
    {
        File directory = new File(_fromFolder);

        String [] directoryContents = directory.list();

        List<String> fileLocations = new ArrayList<String>();

        for(String fileName: directoryContents) {
            File temp = new File(String.valueOf(directory),fileName);
            if (temp.isDirectory()) {
                if (_recursiveSearch) {
                    fileLocations.addAll(FilesForMove.scanFolder(String.valueOf(temp), true));
                }
            }
            else {
                fileLocations.add(String.valueOf(temp));
            }
        }

        return fileLocations;
    }

    private void copyFile(String tmpFileName, String destination, boolean _overwriteExesting) {
        File    sourceFile      = new File(tmpFileName);
        Path    sourcePath      = Paths.get(tmpFileName);
        String  sourceFileName  = sourceFile.getName();
        String  destinationFolder;

        BasicFileAttributes attributes = null;
        try {
            attributes = Files.readAttributes(sourcePath, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (attributes != null) {
            destinationFolder = destination + "\\" + this.convertFileTimeToDate(attributes.lastModifiedTime());
        }
        else {
            throw new Error();
        }

        Path destinationPath = Paths.get(destinationFolder);
        if (Files.exists(destinationPath)) {
            if (!Files.isDirectory(destinationPath)) {
                this.createNewDirectory(destinationPath);
            }
        }
        else {
            this.createNewDirectory(destinationPath);
        }
        Path destinationFile = Paths.get(destinationFolder + "\\" + sourceFileName);

        try {
            //Files.copy(sourcePath, destinationFile);
            if (!Files.exists(destinationFile)) {
                Files.move(sourcePath, destinationFile);
            }
            else
            {
                if (_overwriteExesting) {
                    Files.move(sourcePath, destinationFile, REPLACE_EXISTING);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewDirectory(Path destinationPath) {
        try {
            Files.createDirectory(destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String convertFileTimeToDate(FileTime _creationTime)
    {
        Date    date;
        String  strDate = null;
        String  crTimeString =_creationTime.toString();

        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS");

        try {
            date        = formatter.parse(crTimeString.substring(0, 24));
            formatter   = new SimpleDateFormat("yyyy-MM-dd");
            strDate     = formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
}


