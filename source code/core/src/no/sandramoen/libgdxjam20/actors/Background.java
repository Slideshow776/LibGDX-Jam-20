package no.sandramoen.libgdxjam20.actors;

import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam20.utils.BaseActor;

public class Background extends BaseActor {
    public Background(Stage stage) {
        super(0, 0, stage);
        loadImage("ground");
        setSize(worldBounds.width, worldBounds.height);
    }
}