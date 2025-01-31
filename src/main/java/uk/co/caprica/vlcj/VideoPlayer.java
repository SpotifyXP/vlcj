package uk.co.caprica.vlcj;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

import java.awt.*;

@SuppressWarnings("unused")
public class VideoPlayer {
    private final EmbeddedMediaPlayerComponent mediaPlayer;
    private boolean repeating;
    private Runnable currentTakeover;

    public VideoPlayer() {
        repeating = false;
        mediaPlayer = new EmbeddedMediaPlayerComponent() {
            @Override
            public void finished(MediaPlayer mediaPlayer)
            {
                super.finished(mediaPlayer);
                if(!mediaPlayer.getRepeat()) return;
                new Thread(() -> {
                    System.gc();
                    mediaPlayer.setTime(0);
                    mediaPlayer.play();
                }).start();
            }
        };
    }

    public boolean isVideoPlaybackEnabled() {
        return true;
    }

    public Panel getComponent() {
        return mediaPlayer;
    }

    public void play(String uriOrFile) {
        mediaPlayer.getMediaPlayer().playMedia(uriOrFile);
    }

    public void pause() {
        mediaPlayer.getMediaPlayer().pause();
    }

    public void stop() {
        mediaPlayer.getMediaPlayer().stop();
    }

    public void setLooping(boolean looping) {
        repeating = looping;
    }

    public boolean isLooping() {
        return repeating;
    }

    public boolean isPlaying() {
        return mediaPlayer.getMediaPlayer().isPlaying();
    }

    public void init(Runnable onTakeover) {
        stop();
        if(currentTakeover != null) currentTakeover.run();
        currentTakeover = onTakeover;
    }
}
