package no.sandramoen.libgdxjam20.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdxjam20.utils.BaseActor;

public class Pickup extends BaseActor {
    public Pickup(float x, float y, Stage stage, String imageName) {
        super(x, y, stage);
        addAction(Actions.fadeOut(0f));
        collisionEnabled = false;
        loadImage(imageName);

        if (imageName.equals("body"))
            setSize(10, 10);
        else if (imageName.equals("upperArm"))
            setSize(4, 8);
        else if (imageName.equals("forearm"))
            setSize(4, 8);
        else if (imageName.equals("handOpen"))
            setSize(4, 6);

        setOrigin(Align.center);
        rotateBy(MathUtils.random(0, 360));
        setBoundaryRectangle();
    }

    public void appear() {
        addAction(Actions.fadeIn(2f));
        collisionEnabled = true;
    }
}