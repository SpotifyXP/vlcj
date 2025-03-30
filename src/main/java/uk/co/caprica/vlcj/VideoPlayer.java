package uk.co.caprica.vlcj;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
    private final JLayeredPane container;

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
        canvasVideoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        container = new JLayeredPane();
        container.setBackground(Color.BLACK);
        container.add(canvas, JLayeredPane.DEFAULT_LAYER);
        container.add(component, JLayeredPane.PALETTE_LAYER);
        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvas.setSize(container.getSize());
                component.setSize(container.getSize());
            }
        });
    }

    private void initializeVideoPlayback() {
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(new FullScreenStrategy() {
            @Override public void enterFullScreenMode() {}
            @Override public void exitFullScreenMode() {}
            @Override public boolean isFullScreenMode() { return false; }
        });
        mediaPlayer.setVideoSurface(canvasVideoSurface);
        mediaPlayer.attachVideoSurface();
    }

    public boolean isVideoPlaybackEnabled() {
        return true;
    }

    public Container getComponent() {
        return container;
    }

    public void play(String uriOrFile) {
        mediaPlayer.startMedia(uriOrFile);
        component.setVisible(false);
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
        component.setVisible(true);
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

    public void removeOnTakeOver() {
        currentTakeover = null;
    }
}
