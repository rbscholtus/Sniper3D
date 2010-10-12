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
public class Polygon {

    /**  */
    protected Vertex a;
    /**  */
    protected Vertex b;
    /**  */
    protected Vertex c;
    /**  */
    protected double A, B, C, D;

    /**
     *
     */
    public Polygon(final Vertex a, final Vertex b, final Vertex c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Returns a deep copy of this Polygon object.
     * @return the copy
     */
    public Polygon copy() {
        return new Polygon(a, b, c);
    }

    /**
     *
     */
    public void abcd() {
        double Ux = b.x - a.x;
        double Uy = b.y - a.y;
        double Uz = b.z - a.z;
        double Vx = c.x - a.x;
        double Vy = c.y - a.y;
        double Vz = c.z - a.z;
        A = Uy * Vz - Vy * Uz;
        B = Uz * Vx - Vz * Ux;
        C = Ux * Vy - Vx * Uy;
        double len = Math.sqrt(A * A + B * B + C * C);
        A /= len;
        B /= len;
        C /= len;
        D = A * a.x + B * a.y + C * a.z;
    }

    /**
     *
     * @param viewX
     * @param viewY
     * @param viewZ
     */
    public double dotProduct(float viewX, float viewY, int viewZ) {
        return A * viewX + B * viewY + C * viewZ - D;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return new StringBuilder(100).append(a).append(' ').append(b).append(' ').
                append(c).toString();
    }
}
