package br.ufsc.ine.archwizardduel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.junit.Test;
import br.ufsc.ine.archwizardduel.Game;

public class GameTest { // DesktopLauncher
	@Test
	public void testGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Game(), config);
	}
}
