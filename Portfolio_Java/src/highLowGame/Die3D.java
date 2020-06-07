/**
 * @author John Wolf
 * Date March 2020
 * Course: ICS4U
 * Die3D.java
 * Responsible for the 3D Dice functionality in HighLowGameUI.java
 */

package highLowGame;

import org.fxyz3d.shapes.primitives.CuboidMesh;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Die3D {
	// Store the current roll and number of
	// die sides as private class fields
	private int currentRoll = 6;
	private int numDieSides = 6;

	// The CuboidMesh is the 3D representation of the die
	// It is a 100*100*100 cube
	private CuboidMesh dieMesh = new CuboidMesh(100, 100, 100);
	
	// A SubScene is used for the die mesh and camera
    public SubScene dieScene;
    
    // The root of the die animation is a group
    private Group root = new Group();
    
    // The camera provides the appearance of rolling
    // during the animation
    PerspectiveCamera camera = new PerspectiveCamera(true);

    // Rotations of the camera are used in combination with
    // keyframes for the dice roll animation
    // (Rotation Transitions of the Cuboid Mesh itself resulted
    // in random behaviour)
    private Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    private Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    private Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);
    
    // The following int arrays store the rotations [x, y, z] about
    // the origin required to move the camera to each face 
    private int[] face1Coords = {0, 180, 0};
    private int[] face2Coords = {-90, 0, 0};
    private int[] face3Coords = {0, -90, 0};
    private int[] face4Coords = {0, 90, 0};
    private int[] face5Coords = {90, 0, 0};
    private int[] face6Coords = {0, 0, 0};
    
    // All those face coordinates are stored in another array
    private int[] allFaceCoords[] = {
    		face1Coords, face2Coords, face3Coords,
    		face4Coords, face5Coords, face6Coords
    		};
    
    // The currentRoatiosn array stores the current rotation 
    // vectors of the camera about the origin of the die relative
    // to its initial state
    private int[] currentRotations = {0, 0, 0}; // x, y, z

	
    /**
     * The Die3D constructor creates a new Die3D object
     * and does some initialization of its own
     */
    public Die3D() {
    	// Add the die net image to texture the cuboid mesh
		dieMesh.setTextureModeImage(getClass().getResource("/images/dice_net.png").toExternalForm());

        // Set camera properties
        camera.setNearClip(0.1);
        camera.setFarClip(1000.0);
        camera.setFieldOfView(100);
        
        // Add the x, y, and z rotation transformations
        camera.getTransforms().addAll (
        		xRotate,
                yRotate,
                zRotate,
                // Translate the camera in the z-axis
                // so it is outside of the die itself
                new Translate(0, 0, -100)
        );

		// Add the camera and dieMesh to the Group
        root.getChildren().addAll(camera, dieMesh);
        
        // Create a 100*100px SubScene using the group
        // Enable depthbuffer and anti-aliasing
		dieScene = new SubScene(
	            root,
	            100,100,
	            true,
	            SceneAntialiasing.BALANCED
	    );

		// Set the Subscene's camera
		dieScene.setCamera(camera);
	}
	
	/**
	 * Creates the 3D dice roll animation and returns it as a Timeline
	 * @param rotationDuration  the initial (and fastest) rotation's duration
	 * @return Timeline  the complete 3D dice roll animation
	 */
	public Timeline rollDie(int rotationDuration) {
		// Generate a random number of dice turns (1 to 3)
		int numTurns = (int)(Math.random() * 3) + 1;
		
		// Store the current duration of the animation animationDuration
		// (used to determine when Keyframes should begin and end)
		int animationDuration = 0;

		// Create the Timeline to store the 3D dice roll
		Timeline fullDiceRoll = new Timeline();
		for (int i = 0; i < numTurns; i++) {
			// Update the Timeline with the new keyFrames for each 
			// intermediary rotation
			fullDiceRoll = rollDieOnce(fullDiceRoll, animationDuration, rotationDuration,  false);

			// Update animationDuration
			animationDuration += rotationDuration;

			// Following Rotations should be slower
			rotationDuration *= 2;
		}
		// Update the Timeline with the new keyFrames for the final
		// rotation to the ending face
		fullDiceRoll = rollDieOnce(fullDiceRoll, animationDuration, rotationDuration, true);
		
		// Return the dice roll animation
		return fullDiceRoll;
	}

	/**
	 * Adds keyFrames to the dice roll animation Timeline to rotate the
	 * camera to an arbitrary point (if rollEndingFace is false) or to
	 * the roll's ending face (if rollEndingFace is true)
	 * @param fullDiceRoll  the Timeline containing the dice animation
	 * @param animationDuration  the current duration of the animation
	 * @param rotationDuration  the duration of the next rotation
	 * @param rollEndingFace  determines whether to move the camera to an
	 * 						  arbitrary point or to the dice roll's ending face
	 * @return Timeline the dice roll animation with the new keyFrames
	 */
	private Timeline rollDieOnce(Timeline fullDiceRoll, int animationDuration, int rotationDuration, boolean rollEndingFace) {
		// Set the next axis rotation coordinates to the current
		// rotations coordinates initially
		int nextAxisRotationX = currentRotations[0];
		int nextAxisRotationY = currentRotations[1];
		int nextAxisRotationZ = currentRotations[2];

		// If it's time to roll the last face
		if (rollEndingFace == true) {
			// Generate a random number between 1 and 6,
			// that's the current dice roll
			currentRoll = (int)(Math.random() * numDieSides) + 1;
			
			// Retrieve the rotation vectors to get to that face
			// and store them as the next axis rotations
			int[] faceCoords = allFaceCoords[currentRoll - 1];
			nextAxisRotationX = faceCoords[0];
			nextAxisRotationY = faceCoords[1];
			nextAxisRotationZ = faceCoords[2];
		}
		else {
			// Otherwise, the next axis rotations will be an arbitrary
			// rotation vector from -360 to 360 degrees
			nextAxisRotationX = (int)(Math.random() * 720) - 360;
			nextAxisRotationY = (int)(Math.random() * 720) - 360;
			nextAxisRotationZ = (int)(Math.random() * 720) - 360;
		}

		// Create the next keyFrames for each axis of rotation
		// Add rotationDuration to animationDuration for each keyFrame
		// The keyFrame for each rotation axis should be the nextAxisRotation
		KeyFrame nextStateX = new KeyFrame(
                Duration.millis(animationDuration + rotationDuration), 
                new KeyValue(xRotate.angleProperty(), nextAxisRotationX)
        );
		KeyFrame nextStateY = new KeyFrame(
                Duration.millis(animationDuration + rotationDuration), 
                new KeyValue(yRotate.angleProperty(), nextAxisRotationY)
        );
		KeyFrame nextStateZ = new KeyFrame(
                Duration.millis(animationDuration + rotationDuration), 
                new KeyValue(zRotate.angleProperty(), nextAxisRotationZ)
        );
		
		// Update currentRotations, to reflect the current position
		// of the camera
		currentRotations[0] = nextAxisRotationX;
		currentRotations[1] = nextAxisRotationY;
		currentRotations[2] = nextAxisRotationZ;
        
		// Add all the keyFrames form this rotation to the Timeline
		fullDiceRoll.getKeyFrames().addAll(nextStateX, nextStateY, nextStateZ); 
		
		// Return the updated Timeline
		return fullDiceRoll;
	}

	/**
	 * Retrieves the current roll
	 * @return int  currentRoll
	 */
	public int getCurrentRoll() {
		return currentRoll;
	}

	/**
	 * Retrieves the number of die sides
	 * @return int  numDieSides
	 */
	public int getNumDieSides() {
		return numDieSides;
	}
	
	/**
	 * Sets the number of sides on the die (method is currently unused)
	 * @param numDieSides  the new number of die sides
	 */
	public void setNumDieSides(int numDieSides) {
		this.numDieSides = numDieSides;
	}
}
