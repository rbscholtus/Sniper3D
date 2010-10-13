/*
 *  Copyright 2010 Barend Scholtus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package nehe;

import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

/**
 *
 * @author Barend Scholtus
 */
public class Lesson01 implements GLEventListener, KeyListener {

    private GLWindow window;
    private GLAnimatorControl animator;

    public static void main(String[] args) {
        boolean fullscreen = JOptionPane.YES_OPTION == JOptionPane.
                showConfirmDialog(
                null, "Would You Like To Run In Fullscreen Mode?",
                "Start FullScreen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        new Lesson01(fullscreen);
    }

    /**
     * Creates an animated GLWindow.
     */
    public Lesson01(boolean fullscreen) {
        GLProfile.initSingleton();
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        window = GLWindow.create(caps);
        window.addGLEventListener(this);
        window.setSize(640, 480);
        window.addKeyListener(this);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                endApplication();
            }
        });

        window.setVisible(true);
        window.setTitle("NeHe Lesson 01: Setting Up An OpenGL Window");
        if (fullscreen) {
            window.setFullscreen(true);
        }

        animator = new FPSAnimator(window, 60);
        animator.start();
    }

    /**
     * Performs the setup for OpenGL.
     * @param drawable
     */
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        // Enables Smooth Shading
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Black Background
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // Depth Buffer Setup
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);

        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    /**
     * Displays a frame.
     * @param drawable
     */
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        // Clear The Screen And The Depth Buffer
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Reset The Current Modelview Matrix
        gl.glLoadIdentity();
    }

    /**
     * Resets the GL viewport, projection matrix, and model view matrix.
     * @param drawable
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        final GLU glu = GLU.createGLU(gl);

        // avoid a divide by zero error!
        if (height <= 0) {
            height = 1;
        }

        // Reset The Current Viewport
        gl.glViewport(0, 0, width, height);

        // Select and Reset The Projection Matrix
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        // Calculate The Aspect Ratio Of The Window
        final float h = (float) width / (float) height;
        glu.gluPerspective(45.0f, h, 1.0, 20.0);

        // Select and Reset The Modelview Matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     *
     * @param drawable
     */
    public void dispose(GLAutoDrawable drawable) {
    }

    /**
     * Handles key presses
     * @param ke
     */
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            endApplication();
        } else if (ke.getKeyCode() == KeyEvent.VK_F1) {
            window.setFullscreen(!window.isFullscreen());
        }
    }

    public void keyReleased(KeyEvent ke) {
    }

    public void keyTyped(KeyEvent ke) {
    }

    /**
     * Stops the animation, exits full screen, and ends the application.
     */
    private void endApplication() {
        animator.stop();
        if (window.isFullscreen()) {
            window.setFullscreen(false);
        }
        window.destroy(true);
        System.exit(0);
    }
}
