package com.releasenetworks.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class NetworkComponent {

    public static void downloadToPath(String url, File destination) {
        try {
            URL website = new URL(url);
            ReadableByteChannel readableByteChannel = Channels.newChannel(website.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}