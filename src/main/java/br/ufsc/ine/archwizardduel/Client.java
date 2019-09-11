package br.ufsc.ine.archwizardduel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Client extends ApplicationAdapter {
	private Stage stage;

	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());

		Skin buttonSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		final Button playButton = new TextButton("Play", buttonSkin, "small");
		playButton.setPosition(600, 1);
		playButton.setSize(200, 50);

		final Button newButton = new TextButton("New Game", buttonSkin, "small");
		newButton.setPosition(0, 1);
		newButton.setSize(120, 50);

		final Button disconnectButton = new TextButton("Disconnect", buttonSkin, "small");
		disconnectButton.setPosition(120, 1);
		disconnectButton.setSize(120, 50);

		final Button quitButton = new TextButton("Quit Game", buttonSkin, "small");
		quitButton.setPosition(120, 1);
		quitButton.setSize(120, 50);
		quitButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				quitButton.remove();
				stage.addActor(newButton);
				stage.addActor(disconnectButton);
				playButton.remove();
				return true;
			}
		});

		final Button configButton = new TextButton("Connect...", buttonSkin, "small");
		configButton.setPosition(0, 1);
		configButton.setSize(120, 50);
		stage.addActor(configButton);
		configButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				stage.addActor(newButton);
				stage.addActor(disconnectButton);
				configButton.remove();

				return true;
			}
		});

		final Button sessionButton = new TextButton("New Session", buttonSkin, "small");
		sessionButton.setPosition(120, 1);
		sessionButton.setSize(120, 50);
		stage.addActor(sessionButton);
		sessionButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				stage.addActor(newButton);
				stage.addActor(disconnectButton);
				configButton.remove();
				return true;
			}
		});

		disconnectButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				disconnectButton.remove();
				stage.addActor(configButton);
				stage.addActor(sessionButton);
				return true;
			}
		});

		newButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				stage.addActor(playButton);
				disconnectButton.remove();
				stage.addActor(quitButton);
				newButton.remove();
				return true;
			}
		});

		TextField usernameTextField = new TextField("This is a GUI prototype", buttonSkin);
		usernameTextField.setPosition(0,52);
		usernameTextField.setSize(800, 247);
		stage.addActor(usernameTextField);

		Gdx.input.setInputProcessor(stage);

		Texture guiTest = new Texture(Gdx.files.internal("wol.jpeg"));
		Image gui = new Image(guiTest);
		gui.setSize(800, 300);
		gui.setPosition(0,300);
		stage.addActor(gui);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
}
