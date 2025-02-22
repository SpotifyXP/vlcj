package uk.co.caprica.vlcj;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")
public class VideoPlayer {
    private EmbeddedMediaPlayer mediaPlayer;
    private Runnable currentTakeover;
    private CanvasVideoSurface canvasVideoSurface;
    private boolean wasReleased = true;
    private final MediaPlayerFactory mediaPlayerFactory;
    private final Panel component;
    private final Canvas canvas;
    private Runnable onPlay;

    public VideoPlayer() {
        mediaPlayerFactory = new MediaPlayerFactory(
                "--video-title=SpotifyXP video output",
                "--no-snapshot-preview",
                "--quiet-synchro",
                "--sub-filter=logo:marq",
                "--intf=dummy");
        canvas = new Canvas();
        canvas.setBackground(Color.BLACK);
        component = new Panel();
        component.setBackground(Color.BLACK);
        component.setLayout(new BorderLayout());
        component.add(canvas, BorderLayout.CENTER);
        canvasVideoSurface = mediaPlayerFactory.newVideoSurface(canvas);
    }

    private void initializeVideoPlayback() {
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new FullScreenStrategy() {
            @Override public void enterFullScreenMode() {}
            @Override public void exitFullScreenMode() {}
            @Override public boolean isFullScreenMode() { return false; }
        });
        mediaPlayer.setVideoSurface(canvasVideoSurface);
    }

    public boolean isVideoPlaybackEnabled() {
        return true;
    }

    public Panel getComponent() {
        return component;
    }

    public void play(String uriOrFile) {
        canvas.setVisible(true);
        mediaPlayer.playMedia(uriOrFile);
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        canvas.setVisible(false);
        mediaPlayer.stop();
    }

    public boolean wasReleased() {
        return wasReleased;
    }

    public void release() {
        mediaPlayer.release();
        mediaPlayer = null;
        wasReleased = true;
    }

    public void setLooping(boolean looping) {
        mediaPlayer.setRepeat(looping);
    }

    public boolean isLooping() {
        return mediaPlayer.getRepeat();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void init(Runnable onTakeover) {
        if(wasReleased) {
            initializeVideoPlayback();
            wasReleased = false;
        }
        if(mediaPlayer.isPlaying()) mediaPlayer.stop();
        if(currentTakeover != null) currentTakeover.run();
        currentTakeover = onTakeover;
    }

    public void resume() {
        mediaPlayer.play();
    }
}
