import javax.sound.sampled.AudioInputStream; //sound libraries
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File; //access files

/***********Interface Summary
 * This interface contains methods for background music and sound effects.
 * The Music_playSound method is used in the Main class and the GameWorld class, which implement Music.
 * The Music_playBackground method is used in the Main class to  play the background music until the user
 * exits the program.
 *************/
public interface Music {
    /*********** Method Summary
     * This method plays a sound effect. The sound it plays is determined by the FilePath argument,
     * which finds the sound file in the src folder.
     *************/
    public static void Music_playSound(String Filepath) { //play sound effect
        try {
            File file = new File(Filepath); // This line collects the audio file into an item that java can access.
            AudioInputStream stream = AudioSystem.getAudioInputStream(file); //This code reads the audio stream of the file referenced
            Clip clip = AudioSystem.getClip(); //This converts the stream back into an audio clip
            clip.open(stream); // This opens that audio clip so that it is queued up and able to be played
            clip.start(); // Start playing
            // sleep to allow enough time for the clip to play
            Thread.sleep(400);
            stream.close(); //stop playing
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /*********** Method Summary
     * This method plays background music. The sound it plays is determined by the FilePath argument,
     * which finds the sound file in the src folder.
     *************/
    public static void Music_playBackground(String Filepath) { //play background music
        try { // The process is the same as the method above with one change.
            File file = new File(Filepath); // This line collects the audio file into an item that java can access.
            AudioInputStream stream = AudioSystem.getAudioInputStream(file); //This code reads the audio stream of the file referenced
            Clip clip = AudioSystem.getClip(); //This converts the stream back into an audio clip
            clip.open(stream); // This opens that audio clip so that it is queued up and able to be played
            clip.start(); // Start playing
            // sleep to allow enough time for the clip to play
            Thread.sleep(400);
            clip.loop(Clip.LOOP_CONTINUOUSLY); //rather than ending the slip, this loops it to start again when it ends.
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
