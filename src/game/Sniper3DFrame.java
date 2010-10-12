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

import engine3d.*;
import java.awt.*;

/**
 *
 * @author Barend Scholtus
 */
public class Sniper3DFrame extends AnimationFrame {

    /**  */
    private long frameID;
    /**  */
    private Object3D[] objects;
    /**  */
    private WireFrameCube cube2;
    /**  */
    private WireFramePyramid pyr;
    /**  */
    private int width = -1;
    /**  */
    private int height;
    /**  */
    private int hwidth;
    /**  */
    private int hheight;

    /**
     *
     */
    public Sniper3DFrame() {
        super(60);
        setTitle("Sniper3D Frame");
        setSize(480, 360);

        frameID = 0;
        objects = new Object3D[]{
                    WireFrameCube.createSimpleCube(30),
                    WireFramePyramid.createSimplePyramid(20),
                    WireFrameCube.createSimpleCube(10)
                };
    }

    /**
     *
     */
    @Override
    public void update() {
        frameID++;

        Matrix rotateMatrix = Matrix.createRotX(frameID * 0.5 * Math.PI / getFps()).
                mul(Matrix.createRotY(frameID * .25 * Math.PI / getFps())).
                mul(Matrix.createRotZ(frameID * .125 * Math.PI / getFps()));
        objects[0].apply(rotateMatrix, frameID);

        Matrix rotateMatrix2 = Matrix.createTrans(-100, 0, 0).
                mul(Matrix.createRotX(frameID * Math.PI / getFps())).
                mul(Matrix.createRotY(frameID * .75 * Math.PI / getFps())).
                mul(Matrix.createRotZ(frameID * .5 * Math.PI / getFps()));
        objects[1].apply(rotateMatrix2, frameID);

        Matrix rotateMatrix3 = Matrix.createTrans(-150, 0, 0).
                mul(Matrix.createRotX(frameID * 1.5 * Math.PI / getFps())).
                mul(Matrix.createRotY(frameID * 1.0 * Math.PI / getFps())).
                mul(Matrix.createRotZ(frameID * 0.5 * Math.PI / getFps()));
        objects[2].apply(rotateMatrix3, frameID);
    }

    /**
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        if (width < 0) {
            width = getWidth();
            height = getHeight();
            hwidth = width / 2;
            hheight = height / 2;
        }

        // clear screen
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        // draw drame ID
        g.setColor(Color.green);
        g.drawString(String.valueOf(frameID) + " : " + String.valueOf(frameID / 60), 15, 50);

        for (Object3D obj : objects) {
            obj.paint(g, hwidth, hheight, 300);
        }
    }
}
