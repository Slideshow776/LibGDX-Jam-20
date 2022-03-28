package no.sandramoen.libgdxjam20.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdxjam20.utils.BaseActor;

public class Rock extends BaseActor {
    public Rock(float x, float y, Stage stage) {
        super(x, y, stage);
        loadImage("rock" + MathUtils.random(0, 2));
        setSize(12, 12);

        setOrigin(Align.center);
        rotateBy(MathUtils.random(0, 360));
        setBoundaryPolygon(8);
    }
}
