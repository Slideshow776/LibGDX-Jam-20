package no.sandramoen.libgdxjam20.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import no.sandramoen.libgdxjam20.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
                config.padHorizontal = 0;
                config.padVertical = 0;
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new MyGdxGame();
        }
}