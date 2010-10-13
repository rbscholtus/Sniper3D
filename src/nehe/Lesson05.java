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
import com.jogamp.opengl.util.Animator;
import engine3d.Color3f;
import engine3d.Object3D;
import engine3d.Rotation4f;
import engine3d.Vertex3f;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

/**
 *
 * @author Barend Scholtus
 */
public class Lesson05 implements GLEventListener, KeyListener {

    private GLWindow window;
    private GLAnimatorControl animator;
    private Object3D[] objects;
    private Rotation4f[] rotation;

    public static void main(String[] args) {
        boolean fullscreen = JOptionPane.YES_OPTION == JOptionPane.
                showConfirmDialog(
                null, "Would You Like To Run In Fullscreen Mode?",
                "Start FullScreen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        Lesson05 lesson = new Lesson05(fullscreen);
    }

    /**
     * Creates an animated GLWindow.
     */
    public Lesson05(boolean fullscreen) {
        GLProfile.initSingleton();
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        initObjects();

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
        window.setTitle("NeHe Lesson 05: 3D Shapes");
        if (fullscreen) {
            window.setFullscreen(true);
        }
        
        animator = new Animator(window);
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
     * @param drawable the GL drawable surface
     */
    public void display(GLAutoDrawable drawable) {
        render(drawable, drawable.getGL().getGL2());
        update();
    }

    /**
     * Updates the state of the scene.
     */
    public void update() {
        // Change The Rotation Variables
        rotation[0].angle += 2.0f;
        rotation[1].angle -= 1.5f;
    }

    /**
     * Renders the scene to the GL drawable surface.
     * @param drawable the GL drawable surface
     * @param gl the pipeline object associated with the surface
     */
    public void render(final GLAutoDrawable drawable, final GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < objects.length; i++) {
            Object3D obj = objects[i];
            Rotation4f rot = rotation[i];

            // reset
            gl.glLoadIdentity();

            // translate
            gl.glTranslatef(obj.getX(), obj.getY(), obj.getZ());

            // rotate
            gl.glRotatef(rot.angle, rot.x, rot.y, rot.z);

            // draw object
            obj.drawGL(drawable, gl);
        }
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
     * Creates a pyramid and a cube Object3D with their respective Rotations.
     */
    public final void initObjects() {
        objects = new Object3D[]{
            new Object3D(new Vertex3f[]{
                new Vertex3f(0.0f, 1.0f, 0.0f),
                new Vertex3f(-1.0f, -1.0f, 1.0f),
                new Vertex3f(1.0f, -1.0f, 1.0f),
                new Vertex3f(1.0f, -1.0f, -1.0f),
                new Vertex3f(-1.0f, -1.0f, -1.0f)
            },
            new Color3f[]{
                new Color3f(1.0f, 0.0f, 0.0f),
                new Color3f(0.0f, 1.0f, 0.0f),
                new Color3f(0.0f, 0.0f, 1.0f),
                new Color3f(0.0f, 1.0f, 0.0f),
                new Color3f(0.0f, 0.0f, 1.0f)
            },
            GL2.GL_TRIANGLES,
            new int[][]{
                {0, 1, 2},
                {0, 2, 3},
                {0, 3, 4},
                {0, 4, 1}
            },
            null,
            null, null, //textures not used
            -1.5f, 0.0f, -6.0f),
            new Object3D(new Vertex3f[]{
                new Vertex3f(1.0f, 1.0f, -1.0f), // Top Right Of The Quad (Top)
                new Vertex3f(-1.0f, 1.0f, -1.0f), // Top Left Of The Quad (Top)
                new Vertex3f(-1.0f, 1.0f, 1.0f), // Bottom Left Of The Quad (Top)
                new Vertex3f(1.0f, 1.0f, 1.0f), // Bottom Right Of The Quad (Top)
                new Vertex3f(1.0f, -1.0f, 1.0f), // Top Right Of The Quad (Bottom)
                new Vertex3f(-1.0f, -1.0f, 1.0f), // Top Left Of The Quad (Bottom)
                new Vertex3f(-1.0f, -1.0f, -1.0f), // Bottom Left Of The Quad (Bottom)
                new Vertex3f(1.0f, -1.0f, -1.0f) // Bottom Right Of The Quad (Bottom)
            },
            null,
            GL2.GL_QUADS,
            new int[][]{
                {0, 1, 2, 3}, // Top
                {4, 5, 6, 7}, // Bottom
                {3, 2, 5, 4}, // Front
                {7, 6, 1, 0}, // Back
                {2, 1, 6, 5}, // Left
                {0, 3, 4, 7} // Right
            },
            new Color3f[]{
                new Color3f(0.0f, 1.0f, 0.0f),
                new Color3f(1.0f, 0.5f, 0.0f),
                new Color3f(1.0f, 0.0f, 0.0f),
                new Color3f(1.0f, 1.0f, 0.0f),
                new Color3f(0.0f, 0.0f, 1.0f),
                new Color3f(1.0f, 0.0f, 1.0f)
            },
            null, null, //textures not used
            1.5f, 0.0f, -6.0f)
        };
        rotation = new Rotation4f[]{
            new Rotation4f(0.0f, 0.0f, 1.0f, 0.0f),
            new Rotation4f(0.0f, 1.0f, 1.0f, 1.0f)
        };
    }

    /**
     * Stops the animation, exits full screen, and ends the application.
     */
    private void endApplication() {
        animator.stop();
        System.out.printf("Frames: %d (%.2f fps)\n", animator.getTotalFrames(),
                1000f * animator.getTotalFrames() / animator.getDuration());
        if (window.isFullscreen()) {
            window.setFullscreen(false);
        }
        window.destroy(true);
        System.exit(0);
    }
}
