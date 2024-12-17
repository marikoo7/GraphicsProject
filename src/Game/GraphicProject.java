package Game;

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class  GraphicProject extends JFrame {

    public static void main(String[] args) {

        new GraphicProject();
        try {
            // select the path of the audio
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(GraphicProject.class.getResource("/resources/gameAudio.wav"));

            Clip clip = AudioSystem.getClip();

            clip.open(audioInputStream);

            // start the audio
            clip.start();

            // wait until the audio is finished
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
        glcanvas.addMouseListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        Animator animator = new FPSAnimator(glcanvas, 30);
        JButton startButton = new JButton("Start Game");
        startButton.setFocusable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animator.isAnimating()) {
                    animator.start();
                    startButton.setText("Pause");
                } else {
                    animator.stop();
                    startButton.setText("Start Game");
                }
            }
        });


        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);

        getContentPane().add(glcanvas, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        configureWindow();
        glcanvas.requestFocus();
    }

    private void configureWindow() {
        setTitle(" T-REX GAME :D ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
    }
}
