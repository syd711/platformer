package com.platformer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platformer.Game;
import com.platformer.util.Settings;

public class DesktopLauncher {
  public static void main(String[] arg) {
    Settings settings = Settings.getInstance();

    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.fullscreen = settings.fullscreen;
    config.title = "Platformer (Version " + settings.version + ")";
    config.width = settings.width;
    config.height = settings.height;
    config.resizable = settings.resizable;
    config.backgroundFPS = settings.backgroundFPS;
    config.foregroundFPS = settings.foregroundFPS;
    //config.addIcon("ico/favicon-32x32.png", Files.FileType.Internal);

    if(settings.x != 0) {
      config.x = settings.x;
    }

    new LwjglApplication(new Game(), config);
  }
}
