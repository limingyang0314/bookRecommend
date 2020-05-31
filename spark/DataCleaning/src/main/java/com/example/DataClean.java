package com.example;


import com.example.utils.HDFSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataClean {

    private static final Logger log = LoggerFactory.getLogger(DataClean.class);
    public static void main(String[] args) throws Exception{
        HDFSUtil.readHDFSFiles();
    }
}
