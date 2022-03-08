package a2;

import tage.*;
import tage.shapes.*;


public class DiamondOfPower extends ManualObject {
    private float[] vertices = new float[] {
        -1.0f, 0.0f, 1.0f,    0.0f, 2.0f, 0.0f,     1.0f, 0.0f, 1.0f,  // top front
        -1.0f, 0.0f, -1.0f,   0.0f, 2.0f, 0.0f,    -1.0f, 0.0f, 1.0f,  // top left
         1.0f, 0.0f, -1.0f,   0.0f, 2.0f, 0.0f,    -1.0f, 0.0f, -1.0f, // top back
         1.0f, 0.0f, 1.0f,    0.0f, 2.0f, 0.0f,     1.0f, 0.0f, -1.0f, // top right
        -1.0f, 0.0f, 1.0f,    0.0f, -2.0f, 0.0f,    1.0f, 0.0f, 1.0f,  // bottom front
        -1.0f, 0.0f, -1.0f,   0.0f, -2.0f, 0.0f,   -1.0f, 0.0f, 1.0f,  // bottom left
         1.0f, 0.0f, -1.0f,   0.0f, -2.0f, 0.0f,   -1.0f, 0.0f, -1.0f, // bottom back
         1.0f, 0.0f, 1.0f,    0.0f, -2.0f, 0.0f,    1.0f, 0.0f, -1.0f  // bottom right
    };

    private float[] texcoords = new float[] {
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f,
        0.0f, 0.0f,   0.5f, 1.0f,   1.0f, 0.0f
    };

    private float[] normals = new float[] {
         0.0f, 1.0f, 1.0f,    0.0f, 1.0f, 1.0f,     0.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 0.0f,   -1.0f, 1.0f, 0.0f,    -1.0f, 1.0f, 0.0f,
         0.0f, 1.0f, -1.0f,   0.0f, 1.0f, -1.0f,    0.0f, 1.0f, -1.0f,
         1.0f, 1.0f, 0.0f,    1.0f, 1.0f, 0.0f,     1.0f, 1.0f, 0.0f,
         0.0f, 1.0f, 1.0f,    0.0f, -1.0f, 1.0f,    0.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 0.0f,    -1.0f, -1.0f, 0.0f,  -1.0f, 1.0f, 0.0f,
         0.0f, 1.0f, -1.0f,   0.0f, -1.0f, -1.0f,   0.0f, 1.0f, -1.0f,
         1.0f, 1.0f, 0.0f,    1.0f, -1.0f, 0.0f,    1.0f, 1.0f, 0.0f
    };

    public DiamondOfPower() {
        super();

        setNumVertices(24);
        setVertices(vertices);
        setTexCoords(texcoords);
        setNormals(normals);

        setMatAmb(Utils.goldAmbient()); 
        setMatDif(Utils.goldDiffuse()); 
        setMatSpe(Utils.goldSpecular()); 
        setMatShi(Utils.goldShininess());
    }
}
