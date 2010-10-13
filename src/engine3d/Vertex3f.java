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
public class Vertex3f {

    /**  */
    public float x;
    /**  */
    public float y;
    /**  */
    public float z;

    /**  */
    public long processedID;

    /**
     * Creates a new Vertex3f object with the specified x,y,z 3D coordinate.
     * @param x
     * @param y
     * @param z
     */
    public Vertex3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns a copy of this Vertex3f object.
     * @return the copy
     */
    public final Vertex3f copy() {
        return new Vertex3f(x, y, z);
    }

    /**
     * Returns a String representation of this Vertex3f, i.e. "(x, y, z)".
     * @return
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
