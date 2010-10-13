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

import com.jogamp.opengl.util.texture.Texture;

/**
 *
 * @author Barend Scholtus
 */
public class Polygon {

    /**  */
    protected final Vertex3f[] vertices;
    /**  */
    protected final Color3f[] colors;
    /**  */
    protected final Texture texture;
    /**  */
    public  final TexCoord2f[] texCoords;

    /**
     *
     */
    public Polygon(final Vertex3f[] vertices, final Color3f[] colors,
            final Texture texture, final TexCoord2f[] texCoords) {
        this.vertices = vertices;
        this.colors = colors;
        this.texture = texture;
        this.texCoords = texCoords;
    }

    /**
     * Returns a deep copy of this Polygon object. The vertices and colors are
     * duplicated. However, the Texture points to the same OpenGL texture, and
     * the texture coordinates are the same as well.
     * @return the copy
     */
    public final Polygon copy() {
        Vertex3f[] v = new Vertex3f[vertices.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = vertices[i].copy();
        }
        Color3f[] c = new Color3f[colors.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = colors[i].copy();
        }
        return new Polygon(v, c, texture, texCoords);
    }

    /**
     *
     */
    @Override
    public String toString() {
        // length of Vertex.toString is 27 if (xxxx.xx, xxxx.xx, xxxx.xx)
        StringBuilder sb = new StringBuilder(30*vertices.length);
        for (int i = 0; i < vertices.length; i++) {
            if (i != 0) {
                sb.append(' ');
            }
            sb.append(vertices[i]);
        }
        return sb.toString();
    }
}
