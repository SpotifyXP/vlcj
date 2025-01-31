package uk.co.caprica.vlcj;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import java.awt.*;

@SuppressWarnings("unused")
public class VideoPlayer {
    private final EmbeddedMediaPlayerComponent mediaPlayer;
    private Runnable currentTakeover;

    public VideoPlayer() {
        mediaPlayer = new EmbeddedMediaPlayerComponent();
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
        mediaPlayer.getMediaPlayer().setRepeat(looping);
    }

    public boolean isLooping() {
        return mediaPlayer.getMediaPlayer().getRepeat();
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
