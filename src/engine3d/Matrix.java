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
public class Matrix {

    /** cell is a 4x4 matrix with [rows][cols] */
    public float[] cell = new float[16];

    /**
     *
     */
    public Matrix() {
    }

    /**
     * 
     * @param values
     */
    public Matrix(final float[] values) {
        cell = values;
    }

    /**
     *
     */
    public final float rc(final int r, final int c) {
        return cell[r * 4 + c];
    }

    /**
     * 
     * @param m2
     * @return
     */
    public Matrix mul(final Matrix m2) {
        Matrix conc = new Matrix();
        for (int r = 0; r < 16; r += 4) {
            conc.cell[r] =
                    cell[0] * m2.cell[r]
                    + cell[4] * m2.cell[r + 1]
                    + cell[8] * m2.cell[r + 2]
                    + cell[12] * m2.cell[r + 3];
            conc.cell[r + 1] =
                    cell[1] * m2.cell[r]
                    + cell[5] * m2.cell[r + 1]
                    + cell[9] * m2.cell[r + 2]
                    + cell[13] * m2.cell[r + 3];
            conc.cell[r + 2] =
                    cell[2] * m2.cell[r]
                    + cell[6] * m2.cell[r + 1]
                    + cell[10] * m2.cell[r + 2]
                    + cell[14] * m2.cell[r + 3];
            conc.cell[r + 3] =
                    cell[3] * m2.cell[r]
                    + cell[7] * m2.cell[r + 1]
                    + cell[11] * m2.cell[r + 2]
                    + cell[15] * m2.cell[r + 3];
        }
        return conc;
    }
//
//    /**
//     *
//     */
//    public static Matrix mul(final Matrix m1, final Matrix m2) {
//        Matrix mul = new Matrix();
//        for (int r = 0; r < 4; r++) {
//            for (int c = 0; c < 4; c++) {
//                mul.cell[r][c] = m1.cell[0][c] * m2.cell[r][0]
//                        + m1.cell[1][c] * m2.cell[r][1]
//                        + m1.cell[2][c] * m2.cell[r][2]
//                        + m1.cell[3][c] * m2.cell[r][3];
//            }
//        }
//        return mul;
//    }

    public void invert() {

        // transpose rotation part
        float tmp = cell[4];
        cell[4] = cell[1];
        cell[1] = tmp;
        tmp = cell[8];
        cell[8] = cell[2];
        cell[2] = tmp;
        tmp = cell[9];
        cell[9] = cell[6];
        cell[6] = tmp;

        // invert translation part
        cell[3] = -cell[3];
        cell[7] = -cell[7];
        cell[11] = -cell[11];
//        this doesn't work?
//        float tx = -cell[3], ty = -cell[7], tz = -cell[11];
//        cell[3] = tx * cell[0] + ty * cell[1] + tz * cell[2];
//        cell[7] = tx * cell[4] + ty * cell[5] + tz * cell[6];
//        cell[11] = tx * cell[8] + ty * cell[9] + tz * cell[10];
    }

    @Override
    public String toString() {
        return new StringBuilder(150).append(String.format("[%.2f, %.2f, %.2f, %.2f,\n", cell[0], cell[1], cell[2], cell[3])).
                append(String.format(" %.2f, %.2f, %.2f, %.2f,\n", cell[4], cell[5], cell[6], cell[7])).
                append(String.format(" %.2f, %.2f, %.2f, %.2f,\n", cell[8], cell[9], cell[10], cell[11])).
                append(String.format(" %.2f, %.2f, %.2f, %.2f]", cell[12], cell[13], cell[14], cell[15])).
                toString();
    }

    /**
     * Creates a matrix that rotates around the X axis with rx radians.
     * @param rx the radians to rotate around the X axis
     */
    public static Matrix createRotX(double rx) {
        return new Matrix(new float[]{
                    1, 0, 0, 0,
                    0, (float) Math.cos(rx), (float) Math.sin(rx), 0,
                    0, (float) -Math.sin(rx), (float) Math.cos(rx), 0,
                    0, 0, 0, 1
                });
    }

    /**
     * Creates a matrix that rotates around the Y axis with ry radians.
     * @param ry the radians to rotate around the Y axis
     */
    public static Matrix createRotY(double ry) {
        return new Matrix(new float[]{
                    (float) Math.cos(ry), 0, (float) -Math.sin(ry), 0,
                    0, 1, 0, 0,
                    (float) Math.sin(ry), 0, (float) Math.cos(ry), 0,
                    0, 0, 0, 1
                });
    }

    /**
     * Creates a matrix that rotates around the Z axis with rz radians.
     * @param rz the radians to rotate around the Z axis
     */
    public static Matrix createRotZ(double rz) {
        return new Matrix(new float[]{
                    (float) Math.cos(rz), (float) Math.sin(rz), 0, 0,
                    (float) -Math.sin(rz), (float) Math.cos(rz), 0, 0,
                    0, 0, 1, 0,
                    0, 0, 0, 1
                });
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static Matrix createTrans(final float x, final float y, final float z) {
        return new Matrix(new float[]{
                    1, 0, 0, x,
                    0, 1, 0, y,
                    0, 0, 1, z,
                    0, 0, 0, 1
                });
    }
}
