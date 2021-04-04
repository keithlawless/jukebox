package com.keithlawless.jukebox.entity;

import java.io.Serializable;

public class MusicResourceLocator implements Serializable {

    private String mrl;

    public MusicResourceLocator() {}
    public MusicResourceLocator(String mrl) {
        this.mrl = mrl;
    }

    public String getMrl() {
        return mrl;
    }

    public void setMrl(String mrl) {
        this.mrl = mrl;
    }

}
