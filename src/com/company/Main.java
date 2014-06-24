package com.company;


class Main {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();
        if (fileProcessor.loadPref()) {
            FilesForMove ffm = new FilesForMove();
            ffm.setFileList(FilesForMove.scanFolder(fileProcessor.getFromFolder(), fileProcessor.isSubfolderSearch()));
            int result = ffm.makeMove(fileProcessor.getToFolder(), fileProcessor.isOverwriteExistingFiles());
            System.out.printf("Was copied %d files", result);
        } else {
            System.out.printf("Input file not found");
        }
    }
}
