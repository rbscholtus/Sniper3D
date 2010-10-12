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

/**
 *
 * @author Barend Scholtus
 */
public class Vertex {

    /**  */
    public final float originalX;
    /**  */
    public final float originalY;
    /**  */
    public final float originalZ;
    /**  */
    public float x;
    /**  */
    public float y;
    /**  */
    public float z;
    /**  */
    public float projectedX;
    /**  */
    public float projectedY;
    /**  */
    public long processedID;

    /**
     * Creates a new Vertex object with the specified x,y,z 3D coordinate.
     * @param x
     * @param y
     * @param z
     */
    public Vertex(final float x, final float y, final float z) {
        this.originalX = this.x = x;
        this.originalY = this.y = y;
        this.originalZ = this.z = z;
    }

    /**
     * Returns a copy of this Vertex object.
     * @return the copy
     */
    public Vertex copy() {
        return new Vertex(originalX, originalY, originalZ);
    }

    /**
     * Applies Matrix m to this Vertex, and returns this.
     * @param m the Matrix to be applied
     * @return this
     */
    public Vertex apply(final Matrix m) {

        // transform Vertex
        x = m.rc(0, 0) * originalX + m.rc(0, 1) * originalY + m.rc(0, 2) * originalZ
                + m.rc(0, 3);
        y = m.rc(1, 0) * originalX + m.rc(1, 1) * originalY + m.rc(1, 2) * originalZ
                + m.rc(1, 3);
        z = m.rc(2, 0) * originalX + m.rc(2, 1) * originalY + m.rc(2, 2) * originalZ
                + m.rc(2, 3);

        // return this for chaining calls
        return this;
    }

    /**
     * Applies Matrix m to this Vertex, and returns this.
     * @param m the Matrix to be applied
     * @return this
     */
    public Vertex apply(final Matrix m, final long frameID) {

        // skip this transformation if this Vertex is already processed
        if (frameID > processedID) {

            // transform Vertex
            x = m.rc(0, 0) * originalX + m.rc(0, 1) * originalY + m.rc(0, 2) * originalZ
                    + m.rc(0, 3);
            y = m.rc(1, 0) * originalX + m.rc(1, 1) * originalY + m.rc(1, 2) * originalZ
                    + m.rc(1, 3);
            z = m.rc(2, 0) * originalX + m.rc(2, 1) * originalY + m.rc(2, 2) * originalZ
                    + m.rc(2, 3);
            processedID = frameID;
        }

        // return this for chaining calls
        return this;
    }

    /**
     * Returns a String representation of this Vertex, i.e. "(x, y, y)".
     * @return
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
