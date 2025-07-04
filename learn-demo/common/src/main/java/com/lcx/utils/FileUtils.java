package com.lcx.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : lichangxin
 * @create : 2024/5/21 17:25
 * @description
 */
public class FileUtils {

    public static String readFileToString(String filePath) {
        try {
            // 使用Files类的readAllBytes方法读取文件的所有字节
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            // 将字节转换为字符串，使用UTF-8字符集
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 或者返回一个空字符串""来表示读取失败
        }
    }
}
