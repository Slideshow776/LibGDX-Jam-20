package no.sandramoen.libgdxjam20;

import com.badlogic.gdx.math.MathUtils;

import no.sandramoen.libgdxjam20.screens.LevelScreen;
import no.sandramoen.libgdxjam20.utils.BaseGame;

public class MyGdxGame extends BaseGame {

    @Override
    public void create() {
        super.create();
        setActiveScreen(new LevelScreen());
    }
}
