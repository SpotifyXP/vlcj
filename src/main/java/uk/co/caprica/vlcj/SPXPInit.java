package uk.co.caprica.vlcj;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class SPXPInit {
    public static void init() {
        try {
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
            throw new RuntimeException(ex);
        }
    }
}
