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
public class Color3f {

    /**  */
    public float r;
    /**  */
    public float g;
    /**  */
    public float b;

    /**
     *
     * @param r
     * @param g
     * @param b
     */
    public Color3f(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     *
     * @return
     */
    public Color3f copy() {
        return new Color3f(r, g, b);
    }
}
