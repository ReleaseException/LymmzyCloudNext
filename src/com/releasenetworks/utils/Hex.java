package com.releasenetworks.utils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class Hex {

    public static String toHex(String string) {
        char[] chars = string.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString(ch));
        }

        return hex.toString();
    }

    public static String toString(String hexStr) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    public static void main(String[] args) throws IOException {
        FolderUtils.extractDirectory(new ZipFile("D:\\Desktop\\ReleaseCloud\\LymmzyCloudNextSpace\\global\\templates.zip"),new File("D:\\Desktop\\ReleaseCloud\\LymmzyCloudNextSpace\\global\\test\\"));
    }
}
