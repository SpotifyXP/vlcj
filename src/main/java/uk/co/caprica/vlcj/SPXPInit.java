package uk.co.caprica.vlcj;

import com.sun.jna.NativeLibrary;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class SPXPInit {
    public static void init() {
        Class<?> logger = null;
        try {
            try {
                logger = Class.forName("com.spotifyxp.logging.ConsoleLoggingModules");
            }catch (ClassNotFoundException ignored) {}
            Class<?> publicValues = Class.forName("com.spotifyxp.PublicValues");
            Class<?> architectureDetection = Class.forName("com.spotifyxp.utils.ArchitectureDetection$Architecture");
            if(publicValues.getField("architecture").get(null) == architectureDetection.getField("x86").get(null)) {
                NativeLibrary.addSearchPath("libvlc", publicValues.getField("appLocation").get(null).toString() + File.separator + "vlc");
            }
            NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");
            NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");
            Class<?> util = Class.forName("com.spotifyxp.PublicValues");
            Class<?> interfaceClass = Class.forName("com.spotifyxp.video.VLCPlayer");
            VideoPlayer player = new VideoPlayer();
            Object proxyInstance = Proxy.newProxyInstance(
                    VideoPlayer.class.getClassLoader(),
                    new Class[] {interfaceClass},
                    ((proxy, method, args) -> {
                        try {
                            return player.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(player, args);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    })
            );
            util.getField("vlcPlayer").set(null, proxyInstance);
        } catch (Exception ex) {
            ex.printStackTrace();
            if(logger != null) {
                try {
                    logger.getDeclaredMethod("Throwable", Throwable.class).invoke(logger, ex);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    ex.printStackTrace();
                }
            } else {
                ex.printStackTrace();
            }
        }
    }
}
