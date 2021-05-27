package com.cxtx.common.unit.deoperate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public FileUtil() {
    }

    public static void newFolder(String folderPath) {
        try {
            String filePath = folderPath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                recursionNewFolder(myFilePath);
            }
        } catch (Exception var3) {
            logger.error("新建目录操作出错");
            var3.printStackTrace();
        }

    }

    private static void recursionNewFolder(File targerFile) {
        if (!targerFile.getParentFile().exists()) {
            recursionNewFolder(targerFile.getParentFile());
        }

        targerFile.mkdir();
    }

    public static void newFile(String filePathAndName, String fileContent) {
        try {
            String filePath = filePathAndName.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }

            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            myFile.println(fileContent);
            resultFile.close();
        } catch (Exception var7) {
            logger.error("新建文件操作出错");
            var7.printStackTrace();
        }

    }

    public static void delFile(String filePathAndName) {
        try {
            File myDelFile = new File(filePathAndName);
            myDelFile.delete();
        } catch (Exception var3) {
            logger.error("删除文件操作出错");
            var3.printStackTrace();
        }

    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception var3) {
            logger.error("删除文件夹操作出错");
            var3.printStackTrace();
        }

    }

    public static void delAllFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] tempList = file.list();

                for(int i = 0; i < tempList.length; ++i) {
                    File temp;
                    if (path.endsWith(File.separator)) {
                        temp = new File(path + tempList[i]);
                    } else {
                        temp = new File(path + File.separator + tempList[i]);
                    }

                    if (temp.isFile()) {
                        temp.delete();
                    }

                    if (temp.isDirectory()) {
                        delAllFile(path + "/" + tempList[i]);
                        delFolder(path + "/" + tempList[i]);
                    }
                }

            }
        }
    }


    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();

            for(int i = 0; i < file.length; ++i) {
                File temp;
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
                    byte[] b = new byte[5120];

                    int len;
                    while((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }

                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception var10) {
            logger.error("复制整个文件夹内容操作出错");
            var10.printStackTrace();
        }

    }



    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }
}
