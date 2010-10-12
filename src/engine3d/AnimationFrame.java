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
package engine3d;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

/**
 *
 * @author Barend
 */
public abstract class AnimationFrame extends Frame implements Runnable {

    // the thread that manages the animation cycle
    private volatile Thread bgThread;
    // number of frames per second
    private float fps;
    // the time for one frame (in nanos)
    private long frameTimeNanos;
    // throttle the fps
    private boolean throttle = true;
    // simple statistics
    private long startTimeNanos;
    private long endTimeNanos;
    private long updateCount;
    private long frameCount;
    private boolean displayStats = true;
    // double buffering
    private Image dbImage;
    private Graphics dbg;
    // buffer strategy
    private int numBuffers = 2;
    private long bufCreatedTime;
    private static final long WAIT_BUF_TIME = 1000;
    // bounds before full screen exclusive mode
    private Rectangle savedBounds = new Rectangle();
    // whether we are in fullscreen at the moment
    private boolean fullScreen;
    private boolean semiFullScreen;
    // Keylistener listening for ESC key
    private KeyListener escListener;

    /**
     * Creates a new animated Frame that animates fps frames per second.
     * @param fps
     */
    public AnimationFrame(float fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("fps must be > 0");
        }
        this.fps = fps;
        frameTimeNanos = (long) (1000000000.0 / fps);
    }

    /**
     * Starts the animation loop. This loop will call update() and render()
     * fps times per second. If rendering is too slow, update() will be called
     * additional times to ensure the "physics" of the animation are synced
     * to time.
     */
    public void start() {
        if (bgThread == null) {
            bgThread = new Thread(this);
            bgThread.start();
        }
    }

    /**
     * Stops the animation at the end of the current frame.
     */
    public void stop() {
        bgThread = null;
    }

    /**
     * Runs the animation.
     */
    public void run() {
        Thread currentThread = Thread.currentThread();
        updateCount = 0;
        frameCount = 0;
        long nextFrameNanos = System.nanoTime();
        startTimeNanos = nextFrameNanos;
        while (currentThread == bgThread) {
            do {
                update();
                updateCount++;
                nextFrameNanos += frameTimeNanos;
            } while (System.nanoTime() >= (long) nextFrameNanos);

            if (fullScreen) {
                renderToBufferStrategy();
            } else {
                renderToDoubleBuffer();
            }
            frameCount++;

            // sleep to the end of the frame
            if (throttle) {
                long sleepTimeNanos = (nextFrameNanos - System.nanoTime()) / 1000000;
                if (sleepTimeNanos > 0) {
                    try {
                        Thread.sleep(sleepTimeNanos);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }
        endTimeNanos = System.nanoTime();
        bgThread = null;

        if (displayStats) {
            System.out.printf("Updates: %d (%.2f), Frames: %d (%.2f)\n",
                    updateCount, 1000000000.0 * updateCount / (endTimeNanos - startTimeNanos),
                    frameCount, 1000000000.0 * frameCount / (endTimeNanos - startTimeNanos));
        }
    }

    /**
     * Makes an update to the program's state.
     */
    public abstract void update();

    /**
     *
     */
    private void renderToBufferStrategy() {
        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null) {
            if (System.currentTimeMillis() > (bufCreatedTime + WAIT_BUF_TIME)) {
                createBufferStrategy(numBuffers);
                bufCreatedTime = System.currentTimeMillis();
            }
            return;
        }

        try {
            Graphics g = strategy.getDrawGraphics();
            if (g != null) {
                render(g);
                if (!strategy.contentsLost()) {
                    strategy.show();
                }
                g.dispose();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void renderToDoubleBuffer() {
        if (dbImage == null || dbImage.getWidth(null) != getWidth()
                || dbImage.getHeight(null) != getHeight()) {
            dbImage = getGraphicsConfiguration().getDevice().
                    getDefaultConfiguration().createCompatibleImage(getWidth(), getHeight());
            dbg = dbImage.getGraphics();
        }

        render(dbg);
        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(dbImage, 0, 0, this);
            g.dispose();
        }
    }

    /**
     * Renders a frame on g.
     * @param g graphics context to draw on
     */
    public abstract void render(Graphics g);

    /**
     * 
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        render(g);
    }

    /**
     * Returns whether the animation thread is still actively animating.
     * @return true if the animation is still running
     */
    public boolean isRunning() {
        return bgThread != null && bgThread.isAlive();
    }

    /**
     * Returns the fps of the animation.
     * @return fps
     */
    public float getFps() {
        return fps;
    }

    /**
     * Sets the fps. The animation keeps running.
     * @param fps the new fps
     */
    public void setFps(float fps) {
        this.fps = fps;
        frameTimeNanos = (long) (1000000000 / fps);
    }

    /**
     * 
     * @param throttle
     */
    public void setThrottle(boolean throttle) {
        this.throttle = throttle;
    }

    /**
     *
     * @return
     */
    public boolean isThrottle() {
        return throttle;
    }

    /**
     *
     * @param displayStats
     */
    public void setDisplayStats(boolean displayStats) {
        this.displayStats = displayStats;
    }

    /**
     *
     */
    public boolean isDisplayStats() {
        return displayStats;
    }

    /**
     *
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            enterFullScreen(true);
        } else {
            GraphicsDevice gd = getGraphicsConfiguration().getDevice();
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(null);
            }
            dispose();
            setUndecorated(false);
            setBounds(savedBounds);
            this.fullScreen = false;
            setVisible(true);
        }
    }

    /**
     *
     * @param allowSemiFullScreen
     */
    public void enterFullScreen(boolean allowSemiFullScreen) {
        GraphicsDevice gd = getGraphicsConfiguration().getDevice();
        if (gd.isFullScreenSupported() || allowSemiFullScreen) {
            getBounds(savedBounds);
            if (isDisplayable()) {
                dispose();
            }
            setUndecorated(true);
            if (gd.isFullScreenSupported()) {
                semiFullScreen = false;
                gd.setFullScreenWindow(this);
            } else {
                semiFullScreen = true;
                setBounds(0, 0, gd.getDisplayMode().getWidth(), gd.
                        getDisplayMode().getHeight());
            }
            fullScreen = true;
        }
    }

    /**
     * 
     * @return
     */
    public boolean isFullScreen() {
        return fullScreen;
    }

    /**
     *
     * @return
     */
    public boolean isSemiFullScreen() {
        return semiFullScreen;
    }

    /**
     *
     * @param escToExitFullScreen
     */
    public void setESCToExitFullScreen(boolean escToExitFullScreen) {
        if (escToExitFullScreen && escListener == null) {
            addKeyListener(escListener = new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE && fullScreen) {
                        setFullScreen(false);
                    }
                }
            });
        } else if (!escToExitFullScreen && escListener != null) {
            removeKeyListener(escListener);
            escListener = null;
        }
    }

    /**
     * 
     * @return
     */
    public boolean isESCToExitFullScreen() {
        return escListener != null;
    }
}
