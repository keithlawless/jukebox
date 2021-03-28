package com.keithlawless.jukebox.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {

    String mrl;
    ArrayList<String> folders;
    ArrayList<String> files;

    public Folder() {
        mrl = new String();
        folders = new ArrayList<>();
        files = new ArrayList<>();
    }

    public String getMrl() {
        return mrl;
    }

    public void setMrl(String mrl) {
        this.mrl = mrl;
    }

    public ArrayList<String> getFolders() {
        return folders;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFolders(ArrayList<String> folders) {
        this.folders = folders;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public void addFolder(String folder) {
        folders.add(folder);
    }

    public void addFile(String file) {
        files.add(file);
    }

}
