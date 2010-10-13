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

import engine3d.*;
import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.JOptionPane;

/**
 *
 * @author Barend Scholtus
 */
public class Lesson06 implements GLEventListener, KeyListener {

    private GLWindow window;
    private final String frameTitle = "NeHe Lesson 06: Texture Mapping";
    private GLAnimatorControl animator;
    // The object(s) used in this lesson
    private Object3D object;
    // XYZ Rotation ( NEW )
    private Rotation4f[] rotation = {
        new Rotation4f(0.0f, 1.0f, 0.0f, 0.0f),
        new Rotation4f(0.0f, 0.0f, 1.0f, 0.0f),
        new Rotation4f(0.0f, 0.0f, 0.0f, 1.0f)
    };
    // filenames of the textures
    private final String[] texFilenames = {
        "textures/texture01.jpg",
        "textures/texture02.jpg",
        "textures/texture03.jpg",
        "textures/texture04.jpg",
        "textures/texture05.jpg",
        "textures/texture06.jpg"
    };

    /**
     * Start up the lesson.
     * @param args
     */
    public static void main(String[] args) {
        boolean fullscreen = JOptionPane.YES_OPTION == JOptionPane.
                showConfirmDialog(
                null, "Would You Like To Run In Fullscreen Mode?",
                "Start FullScreen?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        Lesson06 lesson = new Lesson06(fullscreen);
    }

    /**
     * Creates an animated GLWindow.
     */
    public Lesson06(boolean fullscreen) {
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
        window.setTitle(frameTitle);
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

        // fps would go wild!
        gl.setSwapInterval(0);

        // Initialise object(s) (incl. loading textures) ( NEW )
        initObjects();

        // Enable Texture Mapping ( NEW )
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

        // Enables Smooth Shading
        gl.glShadeModel(GL2.GL_SMOOTH);

        // Black Background
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

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
        // Change The Rotation
        rotation[0].angle += 0.1f;
        rotation[1].angle += 0.4f;
        rotation[2].angle += 0.2f;
    }

    /**
     * Renders the scene to the GL drawable surface.
     * @param drawable the GL drawable surface
     * @param gl the pipeline object associated with the surface
     */
    public void render(final GLAutoDrawable drawable, final GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Move Into The Screen 5 Units
        gl.glTranslatef(object.getX(), object.getY(), object.getZ());

        // Rotate in all directions
        for (Rotation4f rot : rotation) {
            gl.glRotatef(rot.angle, rot.x, rot.y, rot.z);
        }

        object.drawGL(drawable, gl);
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
        Texture[] textures = loadGLTextures(texFilenames);


        object = new Object3D(new Vertex3f[]{
            new Vertex3f(1.0f, 1.0f, -1.0f), // Top Right Of The Quad (Top)
            new Vertex3f(-1.0f, 1.0f, -1.0f), // Top Left Of The Quad (Top)
            new Vertex3f(-1.0f, 1.0f, 1.0f), // Bottom Left Of The Quad (Top)
            new Vertex3f(1.0f, 1.0f, 1.0f), // Bottom Right Of The Quad (Top)
            new Vertex3f(1.0f, -1.0f, 1.0f), // Top Right Of The Quad (Bottom)
            new Vertex3f(-1.0f, -1.0f, 1.0f), // Top Left Of The Quad (Bottom)
            new Vertex3f(-1.0f, -1.0f, -1.0f), // Bottom Left Of The Quad (Bottom)
            new Vertex3f(1.0f, -1.0f, -1.0f) // Bottom Right Of The Quad (Bottom)
        },
        null, // No color specified per vertex
        GL2.GL_QUADS, // Each polygon is a quad
        new int[][]{ // Specify polygons by indexing vertices
            {5, 4, 3, 2}, // Front
            {7, 6, 1, 0}, // Back
            {2, 3, 0, 1}, // Top
            {4, 5, 6, 7}, // Bottom
            {6, 5, 2, 1}, // Left
            {4, 7, 0, 3} // Right
        },
        null, // No color per vertex per polygon specified
        textures, // Textures, one for each polygon
        getDefaultQuadTexCoords(textures), // The texture coordinates
        0.0f, 0.0f, -5.0f); // X, Y, Z translation (position)
    }

    /**
     * Loads the textures specified by filenames. Any exceptions are caught,
     * and the next one is attempted.
     */
    public final Texture[] loadGLTextures(String[] filenames) {
        Texture[] textures = new Texture[filenames.length];
        for (int i = 0; i < filenames.length; i++) {
            try {
                textures[i] = TextureIO.newTexture(getClass().
                        getResourceAsStream(filenames[i]),
                        false, getExtension(filenames[i]));
            } catch (Exception e) {
                System.err.println("Cannot read texture: " + filenames[i]
                        + ":\nError: " + e.getMessage());
            }
        }
//
        return textures;
    }

    /**
     *
     * @param textures
     * @return
     */
    public final TexCoord2f[][] getDefaultQuadTexCoords(Texture[] textures) {
        TexCoord2f[][] coords = new TexCoord2f[textures.length][];
        for (int i = 0; i < coords.length; i++) {
            TextureCoords tc = textures[i].getImageTexCoords();
            coords[i] = new TexCoord2f[]{
                new TexCoord2f(tc.left(), tc.bottom()),
                new TexCoord2f(tc.right(), tc.bottom()),
                new TexCoord2f(tc.right(), tc.top()),
                new TexCoord2f(tc.left(), tc.top())
            };
        }
        return coords;
    }

    /**
     * Returns the extension of a filename.
     * @param s the filename
     */
    public static String getExtension(final String s) {
        String ext = null;
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
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
