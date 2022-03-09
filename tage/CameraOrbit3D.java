package tage;

import org.joml.*;
import java.lang.Math;

public class CameraOrbit3D {
    private Engine engine;
    private Camera camera;
    private GameObject target;
    private float cameraAzimuth;
    private float cameraElevation;
    private float cameraRadius;

    public CameraOrbit3D(Camera cam, GameObject t, Engine e) {
        engine = e;
        camera = cam;
        target = t;
        initCameraPosition();
        updateCameraPosition();
    }

    public void initCameraPosition() {
        cameraAzimuth = 0.0f;
        cameraElevation = 20.0f;
        cameraRadius = 2.0f;
        updateCameraPosition();
    }

    public void updateCameraPosition() {
        Vector3f targetRot = target.getWorldForwardVector();
		double targetAngle = Math.toDegrees((double)targetRot.angleSigned(new Vector3f(0,0,-1), new Vector3f(0,1,0)));
		float totalAz = cameraAzimuth - (float)targetAngle;
		double theta = Math.toRadians(totalAz);	// rotation around target
		double phi = Math.toRadians(cameraElevation);	// altitude angle
		float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
		float y = cameraRadius * (float)(Math.sin(phi));
		float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));
		camera.setLocation(new Vector3f(x,y,z).add(target.getWorldLocation()));
		camera.lookAt(target);
	}

    public float getCameraAzimuth() {
        return cameraAzimuth;
    }

    public float getCameraElevation() {
        return cameraElevation;
    }

    public float getCameraRadius() {
        return cameraRadius;
    }

    public void setCameraAzimuth(float az) {
        cameraAzimuth = az;
    }

    public void setCameraElevation(float el) {
        cameraElevation = el;
    }

    public void setCameraRadius(float r) {
        cameraRadius = r;
    }
}