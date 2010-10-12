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

import java.awt.Graphics;

/**
 *
 * @author Barend Scholtus
 */
public class Object3D {

    /**  */
    protected final Vertex[] vertices;
    /**  */
    protected final Polygon[] polygons;

    /**
     *
     */
    public Object3D(final Vertex[] vertices, final Polygon[] polygons) {
        this.vertices = vertices;
        this.polygons = polygons;
    }

    /**
     * Returns a deep copy of this Object3D.
     * @return the copy
     */
    public Object3D copy() {
        Vertex[] v = new Vertex[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            v[i] = vertices[i].copy();
        }
        Polygon[] p = new Polygon[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            p[i] = polygons[i].copy();
        }
        return new Object3D(v, p);
    }

    /**
     *
     * @param m
     * @return
     */
    public Object3D apply(final Matrix m) {
        for (Vertex v : vertices) {
            v.apply(m);
        }
        return this;
    }

    /**
     *
     * @param m
     * @return
     */
    public Object3D apply(final Matrix m, final long frameID) {
        for (Vertex v : vertices) {
            v.apply(m, frameID);
        }
        return this;
    }

    public void paint(Graphics g, float transX, float transY, float transZ) {
    }

    /**
     * 
     * @return
     */
    public Vertex[] getVertices() {
        return vertices;
    }

    /**
     *
     * @return
     */
    public Polygon[] getPolygons() {
        return polygons;
    }
}
