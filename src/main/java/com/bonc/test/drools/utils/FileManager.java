//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bonc.test.drools.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.UUID;

public class FileManager {
    private File root;

    public FileManager() {
    }

    public FileManager setUp() {
        this.root = this.getRootDirectory();
        return this;
    }

    public void tearDown() {
        this.deleteDir(this.root);
    }

    public File newFile(String name) {
        File file = new File(this.getRootDirectory(), name);
        return file;
    }

    private File newFile(String path, String fileName) {
        File file = new File(this.getRootDirectory(), path);
        file.mkdir();
        return new File(file, fileName);
    }

    public File newFile(File dir, String name) {
        File file = new File(dir, name);
        return file;
    }

    public File getRootDirectory() {
        if (this.root != null) {
            return this.root;
        } else {
            File tmp = new File(System.getProperty("java.io.tmpdir"));
            File f = new File(tmp, "__drools__" + UUID.randomUUID().toString());
            if (f.exists()) {
                if (f.isFile()) {
                    throw new IllegalStateException("The temp directory exists as a file. Nuke it now !");
                }

                this.deleteDir(f);
                f.mkdir();
            } else {
                f.mkdir();
            }

            this.root = f;
            return this.root;
        }
    }

    public void deleteDir(File dir) {
        String[] children = dir.list();
        String[] arr$ = children;
        int len$ = children.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String child = arr$[i$];
            File file = new File(dir, child);
            if (file.isFile()) {
                this.deleteFile(file);
            } else {
                this.deleteDir(file);
            }
        }

        this.deleteFile(dir);
    }

    public void deleteFile(File file) {
        if (!file.delete()) {
            int var2 = 0;

            while(!file.delete() && var2++ < 5) {
                System.gc();

                try {
                    Thread.sleep(250L);
                } catch (InterruptedException var5) {
                    throw new RuntimeException("This should never happen");
                }
            }
        }

        if (file.exists()) {
            try {
                if (!System.getProperty("os.name").toLowerCase().contains("win")) {
                    throw new RuntimeException("Unable to delete file:" + file.getCanonicalPath());
                }
            } catch (IOException var4) {
                throw new RuntimeException("Unable to delete file", var4);
            }
        }

    }

    public void write(File f, String text) throws IOException {
        if (f.exists()) {
            try {
                Thread.sleep(1000L);
            } catch (Exception var8) {
                throw new RuntimeException("Unable to sleep");
            }
        } else {
            (new File(f.getParent())).mkdirs();
        }

        BufferedWriter output = new BufferedWriter(new FileWriter(f));
        output.write(text);
        output.close();
        String t1 = StringUtils.toString(new StringReader(text));

        int count;
        for(count = 0; !t1.equals(StringUtils.toString(new BufferedReader(new FileReader(f)))) && count < 5; ++count) {
            System.gc();

            try {
                Thread.sleep(250L);
            } catch (InterruptedException var7) {
                throw new RuntimeException("This should never happen");
            }

            output = new BufferedWriter(new FileWriter(f));
            output.write(text);
            output.close();
        }

        if (count == 5) {
            throw new IOException("Unable to write to file:" + f.getCanonicalPath());
        }
    }

    public File write(String fileName, String text) throws IOException {
        File f = this.newFile(fileName);
        this.write(f, text);
        return f;
    }

    public File write(String path, String fileName, String text) throws IOException {
        File f = this.newFile(path, fileName);
        this.write(f, text);
        return f;
    }

    public String readInputStreamReaderAsString(InputStreamReader in) throws IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(in);
        char[] buf = new char[1024];

        int numRead;
        for(boolean var5 = false; (numRead = reader.read(buf)) != -1; buf = new char[1024]) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }

        reader.close();
        return fileData.toString();
    }
}
