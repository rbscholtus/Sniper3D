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
package game;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

/**
 *
 * @author Barend Scholtus
 */
public class SimpleScene implements GLEventListener {

    public static void main(String[] args) {
        GLProfile.initSingleton();
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        final GLWindow window = GLWindow.create(caps);
        window.setTitle("NEWT Window Test");
        window.addGLEventListener(new SimpleScene());
        window.setSize(640, 480);

        final FPSAnimator animator = new FPSAnimator(window, 60);
        animator.add(window);
        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                animator.stop();
                System.exit(0);
            }
        });

        window.setVisible(true);
        animator.start();
    }
    private double theta = 0;
    private double s = 0;
    private double c = 0;

    public void init(GLAutoDrawable drawable) {
        drawable.getGL().setSwapInterval(1);
    }

    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    private void update() {
        theta += 0.01;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    private void render(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        // draw a triangle filling the window
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(-c, -c);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2d(0, c);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2d(s, -s);
        gl.glEnd();
    }
}
