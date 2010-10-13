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

import com.jogamp.opengl.util.texture.*;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author Barend Scholtus
 */
public class Object3D {

    /**  */
    protected final Vertex3f[] vertices;
    /**  */
    protected final Polygon[] polygons;
    /**  */
    protected float x, y, z;
    /**  */
    private int glMode;

    /**
     *
     * @param vertices
     * @param glMode
     * @param polygons
     * @param x
     * @param y
     * @param z
     */
    public Object3D(final Vertex3f[] vertices, final int glMode, final Polygon[] polygons,
            final float x, final float y, final float z) {
        this.vertices = vertices;
        this.glMode = glMode;
        this.polygons = polygons;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * @param vertices
     * @param vColors
     * @param glMode
     * @param pIndexes
     * @param pColors
     * @param textures
     * @param texCoords
     * @param x
     * @param y
     * @param z
     */
    public Object3D(final Vertex3f[] vertices, final Color3f[] vColors,
            final int glMode, final int[][] pIndexes, final Color3f[] pColors,
            final Texture[] textures, final TexCoord2f[][] texCoords,
            final float x, final float y, final float z) {
        this.vertices = vertices;
        this.glMode = glMode;
        this.polygons = new Polygon[pIndexes.length];
        for (int pi = 0; pi < pIndexes.length; pi++) {
            Vertex3f[] v = new Vertex3f[pIndexes[pi].length];
            for (int vi = 0; vi < v.length; vi++) {
                v[vi] = vertices[pIndexes[pi][vi]];
            }
            Color3f[] c = null;
            Texture t = null;
            TexCoord2f[] tc = null;
            if (textures != null) {
                t = textures[pi];
                tc = texCoords[pi];
            } else if (vColors != null) {
                c = new Color3f[pIndexes[pi].length];
                for (int vi = 0; vi < c.length; vi++) {
                    c[vi] = vColors[pIndexes[pi][vi]].copy();
                }
            } else {
                c = new Color3f[pIndexes[pi].length];
                for (int vi = 0; vi < c.length; vi++) {
                    c[vi] = pColors[pi].copy();
                }
            }
            this.polygons[pi] = new Polygon(v, c, t, tc);
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns a deep copy of this Object3D.
     * @return the copy
     */
    public final Object3D copy() {
        Vertex3f[] v = new Vertex3f[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            v[i] = vertices[i].copy();
        }
        Polygon[] p = new Polygon[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            p[i] = polygons[i].copy();
        }
        return new Object3D(v, glMode, p, x, y, z);
    }

    /**
     * 
     * @param drawable
     * @param gl
     */
    public void drawGL(final GLAutoDrawable drawable, final GL2 gl) {
        for (Polygon p : polygons) {
            if (p.texture != null) {
                p.texture.bind();
            }
            gl.glBegin(glMode);
            for (int i = 0; i < p.vertices.length; i++) {
                if (p.texture != null) {
                    gl.glTexCoord2f(p.texCoords[i].x, p.texCoords[i].y);
                } else {
                    Color3f c = p.colors[i];
                    gl.glColor3f(c.r, c.g, c.b);
                }
                Vertex3f v = p.vertices[i];
                gl.glVertex3f(v.x, v.y, v.z);
            }
            gl.glEnd();
        }
    }

    /**
     * 
     * @return
     */
    public final Vertex3f[] getVertices() {
        return vertices;
    }

    /**
     *
     * @return
     */
    public final Polygon[] getPolygons() {
        return polygons;
    }

    /**
     *
     * @return
     */
    public final float getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public final float getY() {
        return y;
    }

    /**
     *
     * @return
     */
    public final float getZ() {
        return z;
    }

    /**
     *
     * @param x
     */
    public final void setX(final float x) {
        this.x = x;
    }

    /**
     *
     * @param y
     */
    public final void setY(final float y) {
        this.y = y;
    }

    /**
     *
     * @param z
     */
    public final void setZ(final float z) {
        this.z = z;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public final void setXYZ(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     *
     * @param dx
     */
    public final void translateX(final float dx) {
        this.x += dx;
    }

    /**
     *
     * @param dy
     */
    public final void translateY(final float dy) {
        this.y += dy;
    }

    /**
     *
     * @param dz
     */
    public final void translateZ(final float dz) {
        this.z += dz;
    }

    /**
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public final void translateXYZ(final float dx, final float dy, final float dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }
}
