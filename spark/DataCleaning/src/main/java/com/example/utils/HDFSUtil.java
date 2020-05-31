package com.example.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HDFSUtil {
    private static Configuration conf = new Configuration();
    private static FileSystem fs = null;
    private static String path = "hdfs://localhost:9000/";

    static {
        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy",
                "NEVER");

        try {
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readHDFSFile(String filePath) throws Exception {
        FSDataInputStream inputStream = fs.open(new Path(filePath));
        BufferedReader bf = new BufferedReader(
                new InputStreamReader(inputStream)
        );
        String line = null;
        Pattern pattern = Pattern.compile("(?<=\\?).\\S*");
        while ((line = bf.readLine()) != null) {
            String[] lineArr = line.split(" ");
            String _data = lineArr[6];
            String data;
            Matcher matcher = pattern.matcher(_data);
            if (matcher.find()) {
                data = matcher.group();
                OperaFormat.doFormat(data);
            }
        }

        bf.close();
        inputStream.close();

    }

    public static void readHDFSFiles() throws Exception{

        Map<String,String> dateMap = FormatTool.dataFormat(new Date().getTime());

        String _path = dateMap.get("year") + "/" +
                dateMap.get("month") + "/" +
                dateMap.get("day");

        String wholePath = path + "book-recommend/log/" + _path;

        RemoteIterator<LocatedFileStatus> ri = fs.listFiles(
                new Path(wholePath),true);

        while (ri.hasNext()) {
            LocatedFileStatus lfs = ri.next();

            readHDFSFile(lfs.getPath().toString());
        }
        fs.close();
    }

    public static void writeHDFSFile(
            String fileName, String line) throws Exception {

        String _fileName = path + fileName;
        Path file = new Path(_fileName);

        FSDataOutputStream out;
        byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
        if (!fs.exists(file)) {
            out = createFile(_fileName);
        } else {
            out = fs.append(file);
        }
        out.write(bytes);
        out.close();

    }

    private static FSDataOutputStream createFile(String path) throws Exception {
        return fs.create(new Path(path));
    }
}
