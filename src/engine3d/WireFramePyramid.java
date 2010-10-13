///*
// *  Copyright 2010 Barend Scholtus
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package engine3d;
//
//import java.awt.Graphics;
//
///**
// *
// * @author Barend Scholtus
// */
//public class WireFramePyramid extends Object3D {
//
//    /**
//     *
//     * @param polygons
//     */
//    public WireFramePyramid(Vertex[] v, Polygon[] p) {
//        super(v, p);
//    }
//
//    /**
//     *
//     * @param g
//     * @param transX
//     * @param transY
//     * @param transZ
//     */
//    @Override
//    public void paint(Graphics g, float transX, float transY, float transZ) {
//
//        // project to 2D
//        for (int i = 0; i < vertices.length; i++) {
//            Vertex v = vertices[i];
//            v.projectedX = v.x * 500f / (v.z + transZ) + transX;
//            v.projectedY = v.y * 500f / (v.z + transZ) + transY;
//        }
//
//        // determine which polygons are visible
//        for (Polygon p : polygons) {
//            p.abcd();
//            if (p.dotProduct(0, 0, -300) > 0.0) {
//                drawEdge(g, p.a, p.b);
//                drawEdge(g, p.b, p.c);
//                if (p != polygons[4] && p != polygons[5]) {
//                    drawEdge(g, p.c, p.a);
//                }
//            }
//        }
//    }
//
//    /**
//     *
//     * @param g
//     * @param p1
//     * @param p2
//     */
//    private void drawEdge(Graphics g, Vertex p1, Vertex p2) {
//        g.drawLine((int) p1.projectedX, (int) p1.projectedY,
//                (int) p2.projectedX, (int) p2.projectedY);
//    }
//
//    /**
//     * Creates a simple WireFramePyramid object. The width, height, and depth
//     * are all the same. The center of the cub is (0,0,0), and the vertices of
//     * the pyramid are at distances specified by size along the axes.
//     * @param size the distance from the origin to each Vertex' projection on
//     * each axis.
//     * @return the new WireFramePyramid
//     */
//    public static WireFramePyramid createSimplePyramid(float size) {
//        Vertex[] v = new Vertex[]{
//            new Vertex(0, -size, 0),
//            new Vertex(-size, size, -size),
//            new Vertex(+size, size, -size),
//            new Vertex(+size, size, +size),
//            new Vertex(-size, size, +size)
//        };
//        Polygon[] p = new Polygon[]{
//            new Polygon(v[0], v[1], v[2]),
//            new Polygon(v[0], v[2], v[3]),
//            new Polygon(v[0], v[3], v[4]),
//            new Polygon(v[0], v[4], v[1]),
//            new Polygon(v[2], v[1], v[4]),
//            new Polygon(v[4], v[3], v[2])
//        };
//        return new WireFramePyramid(v, p);
//    }
//}
