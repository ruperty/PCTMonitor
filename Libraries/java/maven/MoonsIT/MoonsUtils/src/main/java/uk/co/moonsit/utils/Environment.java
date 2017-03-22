/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.utils;

import java.io.File;
import java.util.List;

/**
 *
 * @author Rupert Young
 * Moon's Information Technology
 *
 */
public class Environment {

    private static Environment instance = null;
    private Long rate = 0L;
    private Long mark = 0L;
    private String fileRoot=null;
    private String filePath=null;
    private  List<String> listOutputFunctions = null;
    private boolean shortNames=true;
    private int fontSize=14;

    protected Environment() {
        // Exists only to defeat instantiation.
    }

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    public List<String> getListOutputFunctions() {
        return listOutputFunctions;
    }

    public int getFontSize(boolean flag) {
        int fs=fontSize;
        if(flag)
            fs= (int) (fontSize*0.7);
        return fs;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setListOutputFunctions(List<String> listOutputFunctions) {
        this.listOutputFunctions = listOutputFunctions;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        int index = filePath.lastIndexOf(File.separator);
        this.filePath = filePath.substring(0,index);
    }

    public String getFileRoot() {
        return fileRoot;
    }

    public void setFileRoot(String fileRoot) {
        String suffix=".xml";
        if(fileRoot.contains(".odg")){
            suffix=".odg";
        }
        int index  = fileRoot.indexOf(suffix);
        this.fileRoot = fileRoot.substring(0, index);
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    public boolean isShortNames() {
        return shortNames;
    }

    public void setShortNames(boolean shortNames) {
        this.shortNames = shortNames;
    }
    
    
}
