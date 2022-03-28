package no.sandramoen.libgdxjam20.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.I18NBundle;

public abstract class BaseGame extends Game implements AssetErrorListener {
    private static BaseGame game;

    public static AssetManager assetManager;
    public static final float WORLD_WIDTH = 100.0F;
    public static final float WORLD_HEIGHT = 100.0F;
    public static final float scale = 1.0F;
    private static float RATIO;
    private static final Color lightPink = new Color(1.0F, 0.816F, 0.94F, 1.0F);
    private static final Color lightBrown = new Color(0.859F, 0.788F, 0.706F, 1.0F);
    private static final Color lightYellowBrown = new Color(0.969F, 0.812F, 0.569F, 1.0F);
    private static boolean enableCustomShaders = true;

    // game assets
    public static Label.LabelStyle label36Style;
    public static Label.LabelStyle label26Style;
    private static TextButtonStyle textButtonStyle;
    public static TextureAtlas textureAtlas;
    private static Skin skin;
    public static Music levelMusic;
    public static Music draggingMusic;
    public static Sound bonesSound;
    public static Sound dig0Sound;
    public static Sound dig1Sound;
    public static Sound dig2Sound;
    public static Sound trumpetsSound;
    public static Sound introVoiceSound;
    public static Sound tutorialVoiceSound;
    public static Sound gameplayVoiceSound;
    public static Sound playerVoiceSound;
    public static Sound sunVoiceSound;
    public static Sound winVoiceSound;

    // game state
    public static Preferences prefs;
    public static boolean loadPersonalParameters;
    public static float soundVolume = 1f;
    public static float musicVolume = 0.5f;
    public static String currentLocale;
    public static I18NBundle myBundle;

    public BaseGame() {
        game = this;
    }

    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }

    public void create() {
        Gdx.input.setInputProcessor(new InputMultiplexer());

        assetManager();

        label36Style = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("fonts/arcadeRounded.fnt"));
        label36Style.font = myFont;

        label26Style = new Label.LabelStyle();
        BitmapFont myFont2 = new BitmapFont(Gdx.files.internal("fonts/arcade26.fnt"));
        label26Style.font = myFont2;

        if (Gdx.app.getType() != Application.ApplicationType.Android) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/excluded/cursor.png")), 0, 0));
        }
    }

    public void dispose() {
        super.dispose();

        try {
            assetManager.dispose();
        } catch (Error error) {
            Gdx.app.error(this.getClass().getSimpleName(), error.toString());
        }

    }

    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(this.getClass().getSimpleName(), "Could not load asset: " + asset.fileName, throwable);
    }

    private void assetManager() {
        long startTime = System.currentTimeMillis();
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load("images/included/packed/images.pack.atlas", TextureAtlas.class);

        // music
        assetManager.load("audio/music/320732__shadydave__time-break-drum-only.mp3", Music.class);
        assetManager.load("audio/music/dragging.wav", Music.class);

        // sound
        assetManager.load("audio/sound/202091__spookymodem__falling-bones.wav", Sound.class);
        assetManager.load("audio/sound/dig0.wav", Sound.class);
        assetManager.load("audio/sound/dig1.wav", Sound.class);
        assetManager.load("audio/sound/dig2.wav", Sound.class);
        assetManager.load("audio/sound/trumpets.wav", Sound.class);
        assetManager.load("audio/sound/voice/intro.wav", Sound.class);
        assetManager.load("audio/sound/voice/tutorial.wav", Sound.class);
        assetManager.load("audio/sound/voice/gameplay.wav", Sound.class);
        assetManager.load("audio/sound/voice/player.wav", Sound.class);
        assetManager.load("audio/sound/voice/sun.wav", Sound.class);
        assetManager.load("audio/sound/voice/win.wav", Sound.class);

        assetManager.finishLoading();

        // music
        levelMusic = assetManager.get("audio/music/320732__shadydave__time-break-drum-only.mp3", Music.class);
        draggingMusic = assetManager.get("audio/music/dragging.wav", Music.class);

        // sound
        bonesSound = assetManager.get("audio/sound/202091__spookymodem__falling-bones.wav", Sound.class);
        dig0Sound = assetManager.get("audio/sound/dig0.wav", Sound.class);
        dig1Sound = assetManager.get("audio/sound/dig1.wav", Sound.class);
        dig2Sound = assetManager.get("audio/sound/dig2.wav", Sound.class);
        trumpetsSound = assetManager.get("audio/sound/trumpets.wav", Sound.class);
        introVoiceSound = assetManager.get("audio/sound/voice/intro.wav", Sound.class);
        tutorialVoiceSound = assetManager.get("audio/sound/voice/tutorial.wav", Sound.class);
        gameplayVoiceSound = assetManager.get("audio/sound/voice/gameplay.wav", Sound.class);
        playerVoiceSound = assetManager.get("audio/sound/voice/player.wav", Sound.class);
        sunVoiceSound = assetManager.get("audio/sound/voice/sun.wav", Sound.class);
        winVoiceSound = assetManager.get("audio/sound/voice/win.wav", Sound.class);

        textureAtlas = assetManager.get("images/included/packed/images.pack.atlas");

        long endTime = System.currentTimeMillis();
        Gdx.app.error(this.getClass().getSimpleName(), "Asset manager took " + (endTime - startTime) + " ms to load all game assets.");
    }
}
