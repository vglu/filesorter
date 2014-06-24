package com.company;

import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

class FileProcessor {

    private static final String configFile = "filesorter.xml";
    private static final String sourceFolder = "sourceFolder";
    private static final String destinationFolder = "destinationFolder";
    private static final String incrementalSearch = "incrementalSearch";
    private static final String overwriteExisting = "overwriteExisting";


    private String fromFolder;
    private String toFolder;
    private boolean subfolderSearch;
    private boolean overwriteExistingFiles;

    public boolean isSubfolderSearch() {
        return subfolderSearch;
    }

    void setSubfolderSearch(boolean subfolderSearch) {
        this.subfolderSearch = subfolderSearch;
    }

    public boolean isOverwriteExistingFiles() {
        return overwriteExistingFiles;
    }

    void setOverwriteExistingFiles(boolean overwriteExistingFiles) {
        this.overwriteExistingFiles = overwriteExistingFiles;
    }




    void createDefaultPref() {
        Preferences pref = Preferences.userNodeForPackage(String.class);
        pref.put(sourceFolder, sourceFolder);
        pref.put(destinationFolder, destinationFolder);
        pref.putBoolean(incrementalSearch, true);
        pref.putBoolean(overwriteExisting, true);

        try {
            pref.exportNode(new FileOutputStream(configFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

    }

    public boolean loadPref() {
        boolean ret = false;

        try {
            Preferences.importPreferences(new FileInputStream(configFile));
        } catch (IOException e) {
            this.createDefaultPref();
            e.printStackTrace();
        } catch (InvalidPreferencesFormatException e) {
            e.printStackTrace();
        }

        Preferences node;

        node            = Preferences.userRoot().node("java");
        node            = node.node("lang");
        fromFolder      = node.get(sourceFolder, "");
        toFolder        = node.get(destinationFolder, "");
        this.setSubfolderSearch(node.getBoolean(incrementalSearch, true));
        this.setOverwriteExistingFiles(node.getBoolean(overwriteExisting, true));

        if (fromFolder.length() != 0 && toFolder.length() != 0 ) {
            File file = new File(fromFolder);
            if (file.exists()) {
                ret = true;
            }
        }
        return ret;
    }

    public String getFromFolder()
    {
        return fromFolder;
    }

    public  String getToFolder()
    {
        return toFolder;
    }
}
