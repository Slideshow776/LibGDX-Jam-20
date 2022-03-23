package no.sandramoen.libgdxjam20.actors;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdxjam20.utils.BaseActor;

public class Player extends BaseActor {
    private BaseActor body;
    private BaseActor upperArm;
    private BaseActor forearm;
    private BaseActor hand;

    private Vector2 shoulderPosition;
    private float shoulderX;
    private float shoulderY;
    private float armLength;

    private float movementSpeed = .2f;

    public Player(float x, float y, Stage stage) {
        super(x, y, stage);

        body = new BaseActor(0f, 0f, stage);
        body.loadImage("head");
        body.setSize(10f, 18f);
        body.centerAtPosition(x, y);
        body.setDebug(false);
        body.setBoundaryPolygon(4);

        shoulderX = body.getWidth() / 2 + 1;
        shoulderY = body.getHeight() / 2 - 1;
        shoulderPosition = new Vector2(
                body.getX() + shoulderX,
                body.getY() + shoulderY
        );

        upperArm = new BaseActor(0f, 0f, stage);
        upperArm.loadImage("upperArm");
        upperArm.setSize(4f, 10f);
        upperArm.setOrigin(Align.bottom);
        upperArm.setPosition(shoulderX, shoulderY);
        upperArm.setDebug(body.getDebug());
        body.addActor(upperArm);

        forearm = new BaseActor(0f, 0f, stage);
        forearm.loadImage("forearm");
        forearm.setSize(4f, 10f);
        forearm.setOrigin(Align.bottom);
        forearm.setPosition(0f, upperArm.getHeight());
        forearm.setDebug(body.getDebug());
        upperArm.addActor(forearm);

        hand = new BaseActor(0f, 0f, stage);
        hand.loadImage("handOpen");
        hand.setSize(forearm.getWidth(), 6f);
        hand.setOrigin(Align.bottom);
        hand.setPosition(0f, forearm.getHeight());
        hand.setDebug(body.getDebug());
        forearm.addActor(hand);

        upperArm.setVisible(false);
        forearm.setVisible(false);
        hand.setVisible(false);

        body.setOrigin(Align.center);
        wiggle(body);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shoulderX = body.getWidth() / 2 + 1;
        shoulderY = body.getHeight() / 2 - 1;
        shoulderPosition = new Vector2(
                body.getX() + shoulderX,
                body.getY() + shoulderY
        );
        // shoulderPosition.rotateDeg(body.getRotation());

        boundToWorld();
        alignCamera(new Vector2(body.getX(), body.getY()), .1f);
    }

    public void clenchHand() {
        hand.loadImage("handClenched");
        hand.setSize(forearm.getWidth(), 6f);
        hand.setOrigin(Align.bottom);
    }

    public void openHand() {
        hand.loadImage("handOpen");
        hand.setSize(forearm.getWidth(), 6f);
        hand.setOrigin(Align.bottom);
    }

    public void touchDragged(int screenX, int screenY, Vector2 grabCoordinates, Vector3 touchDownWorldCoordinates) {
        angleUpperArmTowardsCoordinates(grabCoordinates);
        moveHandTowardsCoordinates(grabCoordinates);

        Vector3 worldCoordinates = this.getStage().getCamera().unproject(new Vector3(screenX, screenY, 0f));
        moveVertically(worldCoordinates, grabCoordinates, touchDownWorldCoordinates.y);
        moveHorizontally(worldCoordinates, grabCoordinates, touchDownWorldCoordinates.x);
    }

    public void mouseMoved(int screenX, int screenY) {
        Vector3 worldCoordinates = this.getStage().getCamera().unproject(new Vector3(screenX, screenY, 0f));
        angleUpperArmTowardsCoordinates(new Vector2(worldCoordinates.x, worldCoordinates.y));
        moveHandTowardsCoordinates(new Vector2(worldCoordinates.x, worldCoordinates.y));
    }

    public void setHeadOnBody() {
        body.loadImage("headAndBody");
        body.setSize(10f, 18f);
    }

    public BaseActor getBody() {
        return body;
    }

    public BaseActor getUpperArm() {
        return upperArm;
    }

    public BaseActor getForearm() {
        return forearm;
    }

    public BaseActor getHand() {
        return hand;
    }

    public void toggleDebug() {
        body.setDebug(!body.getDebug());
        upperArm.setDebug(!upperArm.getDebug());
        forearm.setDebug(!forearm.getDebug());
        hand.setDebug(!hand.getDebug());
    }

    private void wiggle(BaseActor bodypart) {
        bodypart.addAction(Actions.sequence(
                Actions.rotateBy(10f, .1f),
                Actions.rotateBy(-20f, .1f),
                Actions.rotateBy(20f, .1f),
                Actions.rotateBy(-20f, .1f),
                Actions.rotateBy(20f, .1f),
                Actions.rotateBy(-20f, .1f),
                Actions.rotateBy(20f, .1f),
                Actions.rotateBy(-20f, .1f),
                Actions.rotateBy(20f, .1f),
                Actions.rotateBy(-20f, .1f),
                Actions.rotateBy(20f, .1f),
                Actions.rotateBy(-10f, .1f)
        ));
    }

    private void moveVertically(Vector3 worldCoordinates, Vector2 grabCoordinates, float touchDownWorldCoordinatesY) {
        float leftSideY = abs(shoulderPosition.y - grabCoordinates.y);
        float offsetY = 0;
        armLength = upperArm.getHeight() + forearm.getHeight();
        float rightSideY = armLength + offsetY;
        boolean conditionY = leftSideY < rightSideY;
        // System.out.println("$leftSideY, $rightSideY, $conditionY")
        if (conditionY) {
            float translocation = worldCoordinates.y - touchDownWorldCoordinatesY;
            if (shoulderPosition.y - grabCoordinates.y < 0) {
                if (leftSideY + translocation + offsetY < rightSideY + offsetY) // top to bottom
                    body.setY(body.getY() - translocation * movementSpeed);
            } else {
                if (leftSideY - translocation < rightSideY) // bottom to top
                    body.setY(body.getY() - translocation * movementSpeed);
            }
        }
    }

    private void moveHorizontally(Vector3 worldCoordinates, Vector2 grabCoordinates, float touchDownWorldCoordinatesX) {
        float offsetX = 2;
        if (shoulderPosition.x - grabCoordinates.x > 0) offsetX = 0;
        float rightSideX = armLength + offsetX;
        float leftSideX = abs(shoulderPosition.x - grabCoordinates.x);
        boolean conditionX = leftSideX < rightSideX;
        // println("$leftSideX, $rightSideX, $conditionX")
        if (conditionX) {
            float translocation = worldCoordinates.x - touchDownWorldCoordinatesX;
            if (shoulderPosition.x - grabCoordinates.x < 0) {
                if (leftSideX + translocation + offsetX < rightSideX + offsetX) // left to right
                    body.setX(body.getX() - translocation * movementSpeed);
            } else {
                if (leftSideX - translocation - offsetX < rightSideX) // right to left
                    body.setX(body.getX() - translocation * movementSpeed);
            }
        }
    }

    private void angleUpperArmTowardsCoordinates(Vector2 coordinates) {
        float angle = MathUtils.atan2(
                coordinates.y - shoulderPosition.y - hand.getHeight() / 2 + hand.getHeight() / 2,
                coordinates.x - shoulderPosition.x - hand.getWidth() / 2
        ) * MathUtils.radiansToDegrees;
        upperArm.setRotation(angle - 90 - body.getRotation());
    }

    private void moveHandTowardsCoordinates(Vector2 coordinates) {
        Vector2 ghostHand = new Vector2(
                coordinates.x - shoulderPosition.x,//  - hand.width / 2,
                coordinates.y - shoulderPosition.y// - hand.height / 2
        );
        armLength = upperArm.getHeight() + forearm.getHeight();
        ghostHand.x = MathUtils.clamp(ghostHand.x, -armLength, armLength);
        ghostHand.y = MathUtils.clamp(ghostHand.y, -armLength, armLength);

        rotateLimbsOfArm(ghostHand);
    }

    private void rotateLimbsOfArm(Vector2 coordinates) {
        float sideA = forearm.getHeight();
        float sideB = hypotenuse(
                (shoulderPosition.x + coordinates.x) - shoulderPosition.x,
                (shoulderPosition.y + coordinates.y) - shoulderPosition.y
        );
        float sideC = upperArm.getHeight();
        Vector3 angles = threeSidesGivenSSS(sideA, sideB, sideC);

        upperArm.setRotation(upperArm.getRotation() - angles.x);
        forearm.setRotation(180 - angles.y);
        hand.setRotation(angles.z);
    }

    private Vector3 threeSidesGivenSSS(Float a, Float b, Float c) {
        float alpha = lawOfCosine(b, c, a);
        if (Float.isNaN(alpha)) alpha = 0f;

        float beta = lawOfCosine(c, a, b);
        if (Float.isNaN(beta)) beta = 180f;

        float gamma = 180 - alpha - beta;

        return new Vector3(alpha, beta, gamma);
    }

    private Float lawOfCosine(Float a, Float b, Float c) {
        if (2 * a * b == 0f) return 0f;
        return MathUtils.acos((a * a + b * b - c * c) / (2 * a * b)) * MathUtils.radiansToDegrees;
    }

    private float hypotenuse(Float x, Float y) {
        return (float) sqrt((x * x) + (y * y));
    }
}
