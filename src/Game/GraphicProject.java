package Game;

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;

public class GraphicProject extends JFrame {

    public static void main(String[] args) {

        new GraphicProject();
        try {
            // تحديد المسار لملف الصوت
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundTest.class.getResource("/resources/gameAudio.wav"));

            
            Clip clip = AudioSystem.getClip();

           
            clip.open(audioInputStream);

            
            clip.start();

           
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (UnsupportedAudioFileException | LineUnavailableException | InterruptedException | IOException e) {
//            e.finalize();
        }

    }

    public GraphicProject() {
        AnimGLEventListener listener = new AnimGLEventListener();
        GLCanvas glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        Animator animator = new FPSAnimator(glcanvas, 60);
        animator.start();
        configureWindow();
        glcanvas.requestFocus();
    }

    private void configureWindow() {
        setTitle(" T-REX GAME :D ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
    }
}
