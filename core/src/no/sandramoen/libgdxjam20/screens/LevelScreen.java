package no.sandramoen.libgdxjam20.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.libgdxjam20.actors.Background;
import no.sandramoen.libgdxjam20.actors.Pickup;
import no.sandramoen.libgdxjam20.actors.Player;
import no.sandramoen.libgdxjam20.actors.Rock;
import no.sandramoen.libgdxjam20.utils.BaseActor;
import no.sandramoen.libgdxjam20.utils.BaseGame;
import no.sandramoen.libgdxjam20.utils.BaseScreen;

public class LevelScreen extends BaseScreen {
    private Player player;
    private Pickup bodyPickup;
    private Pickup upperArmPickup;
    private Pickup forearmPickup;
    private Pickup handPickup;

    private Array<Rock> rocks;

    private Vector3 touchDownWorldCoordinates = new Vector3();
    private Vector2 grabCoordinates = new Vector2();

    private Label winLabel;
    private Label restartLabel;

    private boolean isGameOver = false;

    @Override
    public void initialize() {
        BaseActor.setWorldBounds(300, 200);
        new Background(mainstage);

        BaseActor jam = new BaseActor(130f, 130f, mainstage);
        jam.loadImage("strawberry-jam");
        jam.setSize(20, 20);

        bodyPickup = new Pickup(20, 30, mainstage, "body");
        upperArmPickup = new Pickup(215, 30, mainstage, "upperArm");
        forearmPickup = new Pickup(10, 50, mainstage, "forearm");
        handPickup = new Pickup(220, 2, mainstage, "handOpen");

        rocks = new Array();
        rocks.add(new Rock(170, 15, mainstage));
        rocks.add(new Rock(100, 20, mainstage));
        rocks.add(new Rock(10, 70, mainstage));
        rocks.add(new Rock(200, 30, mainstage));
        rocks.add(new Rock(220, 90, mainstage));
        rocks.add(new Rock(250, 110, mainstage));

        player = new Player(50f, 50f, mainstage);
        player.setBounds(300, 125);

        uiSetup();
        BaseGame.levelMusic.play();
        BaseGame.levelMusic.setLooping(true);
        BaseGame.levelMusic.setVolume(BaseGame.musicVolume);

        BaseGame.draggingMusic.play();
        BaseGame.draggingMusic.setLooping(true);
        BaseGame.draggingMusic.setVolume(0);

        winLabel.addAction(Actions.fadeOut(0f));
        upperArmPickup.addAction(Actions.sequence(
                Actions.delay(2f),Actions.run(new Runnable() {
                    public void run() {
                        BaseGame.introVoiceSound.play(BaseGame.soundVolume);
                    }
                }),
                Actions.delay(13f),
                Actions.run(new Runnable() {
                    public void run() {
                        winLabel.addAction(Actions.fadeIn(2f));
                    }
                }),
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    public void run() {
                        bodyPickup.appear();
                    }
                })
        ));
    }

    @Override
    public void update(float delta) {
        if (player.getBody().overlaps(bodyPickup)) {
            player.setHeadOnBody();
            bodyPickup.collisionEnabled = false;
            bodyPickup.remove();
            upperArmPickup.appear();
            winLabel.setText("Find her upper arm!");
            BaseGame.bonesSound.play(BaseGame.soundVolume * .5f, MathUtils.random(.5f, 1.5f), 0f);
            BaseGame.tutorialVoiceSound.play(BaseGame.soundVolume);
        } else if (player.getBody().overlaps(upperArmPickup)) {
            player.getUpperArm().setVisible(true);
            upperArmPickup.collisionEnabled = false;
            upperArmPickup.remove();
            forearmPickup.appear();
            winLabel.setText("Find her forearm!");
            BaseGame.bonesSound.play(BaseGame.soundVolume * .5f, MathUtils.random(.5f, 2f), 0f);
            BaseGame.tutorialVoiceSound.stop();
            BaseGame.gameplayVoiceSound.play(BaseGame.soundVolume);
        } else if (player.getBody().overlaps(forearmPickup)) {
            player.getForearm().setVisible(true);
            forearmPickup.collisionEnabled = false;
            forearmPickup.remove();
            handPickup.appear();
            winLabel.setText("Find her hand!");
            BaseGame.bonesSound.play(BaseGame.soundVolume * .5f, MathUtils.random(.5f, 2f), 0f);
            BaseGame.gameplayVoiceSound.stop();
            BaseGame.playerVoiceSound.play(BaseGame.soundVolume);
        } else if (player.getBody().overlaps(handPickup)) {
            player.getHand().setVisible(true);
            handPickup.collisionEnabled = false;
            handPickup.remove();
            player.setBounds(300, 200);
            winLabel.setText("Get some sun on her bones!");
            BaseGame.bonesSound.play(BaseGame.soundVolume * .5f, MathUtils.random(.5f, 2f), 0f);
            BaseGame.playerVoiceSound.stop();
            BaseGame.sunVoiceSound.play(BaseGame.soundVolume);
        }

        for (Rock rock : rocks)
            if (player.getBody().overlaps(rock))
                player.getBody().preventOverlap(rock);

        if (player.getBody().getY() >= 125 && player.getHand().isVisible() && !isGameOver) {
            winLabel.setColor(Color.GOLDENROD);
            winLabel.setText("A WINNER IS YOU!");
            restartLabel.addAction(Actions.fadeIn(5f));
            isGameOver = true;
            BaseGame.trumpetsSound.play(BaseGame.soundVolume, MathUtils.random(.8f, 1.2f), 0f);
            BaseGame.winVoiceSound.play(BaseGame.soundVolume);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        player.clenchHand();
        touchDownWorldCoordinates = mainstage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
        grabCoordinates = player.getHand().localToStageCoordinates(new Vector2(player.getHand().getWidth() / 2, 0));
        if (!isGameOver) {
            int random = MathUtils.random(0, 2);
            if (random == 0)
                BaseGame.dig0Sound.play(BaseGame.soundVolume, MathUtils.random(.5f, 2f), 0f);
            if (random == 1)
                BaseGame.dig1Sound.play(BaseGame.soundVolume, MathUtils.random(.5f, 2f), 0f);
            if (random == 2)
                BaseGame.dig2Sound.play(BaseGame.soundVolume, MathUtils.random(.5f, 2f), 0f);

        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.openHand();
        BaseGame.draggingMusic.setVolume(0f);
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!isGameOver) {
            player.touchDragged(screenX, screenY, grabCoordinates, touchDownWorldCoordinates);
            touchDownWorldCoordinates = mainstage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
            BaseGame.draggingMusic.setVolume(BaseGame.soundVolume * .25f);
        }
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        player.mouseMoved(screenX, screenY);
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Input.Keys.NUM_2 == keycode) player.toggleDebug();
        else if (Input.Keys.R == keycode) BaseGame.setActiveScreen(new LevelScreen());
        else if (Input.Keys.Q == keycode) Gdx.app.exit();
        return super.keyDown(keycode);
    }

    private void uiSetup() {
        winLabel = new Label("Find her torso!", BaseGame.label36Style);
        winLabel.setColor(Color.PINK);
        uiTable.add(winLabel).padTop(Gdx.graphics.getHeight() * .02f).row();

        restartLabel = new Label("press 'R' to restart", BaseGame.label26Style);
        restartLabel.addAction(Actions.fadeOut(0f));
        uiTable.add(restartLabel).expandY().top().padTop(Gdx.graphics.getHeight() * .02f);
        /*uiTable.setDebug(true);*/
    }
}
