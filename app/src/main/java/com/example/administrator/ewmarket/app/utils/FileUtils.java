package com.example.administrator.ewmarket.app.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author by
 */

public class FileUtils
{
    private static String sdState = Environment.getExternalStorageState();

    //将SD卡文件删除
    public static void  deleteFile(File file)
    {
        if(sdState.equals(Environment.MEDIA_MOUNTED))
        {
            if (file.exists())
            {
                if (file.isFile())
                {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory())
                {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++)
                    { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
            }
        }
    }
}
