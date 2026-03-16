package com.keithlawless.jukebox.entity;

import java.io.Serializable;
import java.util.ArrayList;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a folder in the file system with its contents
 */
@Schema(description = "Represents a folder in the file system with its contents")
public class Folder implements Serializable {
    @Schema(description = "Media Resource Locator (MRL) of the folder", 
            example = "file:///music/artists/Queen")
    private String mrl;
    
    @ArraySchema(
        arraySchema = @Schema(description = "List of subfolder names within this folder"),
        schema = @Schema(description = "Subfolder name", example = "Greatest Hits")
    )
    private ArrayList<String> folders;
    
    @ArraySchema(
        arraySchema = @Schema(description = "List of file names within this folder"),
        schema = @Schema(description = "File name", example = "bohemian_rhapsody.mp3")
    )
    private ArrayList<String> files;

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
