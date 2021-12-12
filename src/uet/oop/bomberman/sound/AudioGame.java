package uet.oop.bomberman.sound;

import uet.oop.bomberman.BombermanGame;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class AudioGame {
    public static final String BACKGROUND = "background";
    public static final String PUT_BOMB = "put_bomb";
    public static final String EXPLOSION = "explosion";
    public static final String PLAYER_DEAD = "player_dead";
    public static final String GET_ITEM = "get_item";
    public static final String NEXT_LEVEL = "next_level";
    public static final String ENEMY_DEAD = "enemy_dead"; // them vao enemy sau

    private Clip clip;

    public AudioGame(String fileName) {
        String path = BombermanGame.path + "sound/" + fileName + ".wav";
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(path)));
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playAudio() {
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    // Khi hết sẽ tự động phát lại: nhạc nền.
    public void playLoopAudio() {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
