package de.gommzy.cloud.cloud.cache.serverloader;

import com.releasenetworks.executor.annotations.LymmzyCloud;
import com.releasenetworks.network.NetworkComponent;
import de.gommzy.cloud.config.Config;

import java.io.File;

@LymmzyCloud(mode = "master")
public class Serverloader {

    public Serverloader() {
        this.loadVelocity();
        this.loadBungeecord();
        this.loadPaper();
    }

    private void loadVelocity() {
        File file = new File(Config.getOptionAsString("cacheLocation") + "/servers/velocity");
        if (!file.exists()) {
            file.mkdirs();
            NetworkComponent.downloadToPath("https://api.papermc.io/v2/projects/velocity/versions/3.1.2-SNAPSHOT/builds/184/downloads/velocity-3.1.2-SNAPSHOT-184.jar", new File(file.getAbsolutePath() + "/server.jar"));
        }
    }

    private void loadPaper() {
        File file = new File(Config.getOptionAsString("cacheLocation") + "/servers/paper");
        if (!file.exists()) {
            file.mkdirs();
            NetworkComponent.downloadToPath("https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/220/downloads/paper-1.19.2-220.jar", new File(file.getAbsolutePath() + "/server.jar"));
        }
    }

    private void loadBungeecord() {
        File file = new File(Config.getOptionAsString("cacheLocation") + "/servers/bungeecord");
        if (!file.exists()) {
            file.mkdirs();
            NetworkComponent.downloadToPath("https://api.papermc.io/v2/projects/waterfall/versions/1.19/builds/504/downloads/waterfall-1.19-504.jar", new File(file.getAbsolutePath() + "/server.jar"));
        }
    }
}