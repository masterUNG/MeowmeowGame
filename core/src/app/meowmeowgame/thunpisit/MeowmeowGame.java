package app.meowmeowgame.thunpisit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MeowmeowGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private int intX;
	private BitmapFont myBitmapFont;

	private Music musicBackground;
	private com.badlogic.gdx.math.Rectangle catRectangle, coinsRectangel;
	private Texture imgCat, imgCoins;
	private OrthographicCamera objOrthographicCamery;
	private Vector3 objVector3;
	private Array<com.badlogic.gdx.math.Rectangle>objCoinsDrop;
	private long lastDropTime;
	private Iterator<com.badlogic.gdx.math.Rectangle> objIterator;
	private Sound soundSuccess, soundFalse;

	public int score;
	public int live;
	public static final int INCREASE_1LIVE_INTERVAL = 50;




	public void create () {
		//Score
		score = 0;

		// Live
		live = 3;

		batch = new SpriteBatch();
		img = new Texture("BG.png");

		//Setup Image
		imgCat = new Texture("cat.png");
		imgCoins = new Texture("c1.png");

		//Create Camera & SpriteBatch
		objOrthographicCamery = new OrthographicCamera();
		objOrthographicCamery.setToOrtho(false, 800,400);
		batch = new SpriteBatch();

		//Inherit Rectangle
		catRectangle = new com.badlogic.gdx.math.Rectangle();
		catRectangle.x = 368;
		catRectangle.y = 20;
		catRectangle.width = 64;
		catRectangle.height = 64;

		//Create Coins & drop
		objCoinsDrop = new Array<com.badlogic.gdx.math.Rectangle>();
		gameCoinsDrop();



		myBitmapFont = new BitmapFont(); //เพิ่งเปิด
		myBitmapFont.setColor(Color.BLUE); //เพิ่งเปิด

		//Music Background
		musicBackground = Gdx.audio.newMusic(Gdx.files.internal("music1.wav"));
		soundSuccess = Gdx.audio.newSound(Gdx.files.internal("s.mp3"));
		soundFalse = Gdx.audio.newSound(Gdx.files.internal("f.mp3"));


		//Start & Loop Music
		musicBackground.setLooping(true);
		musicBackground.play();
	} // Create Method

	private void gameCoinsDrop() {

		coinsRectangel = new com.badlogic.gdx.math.Rectangle();
		coinsRectangel.x = MathUtils.random(0,736);
		coinsRectangel.y = 480;
		coinsRectangel.width = 64;
		coinsRectangel.height = 64;
		objCoinsDrop.add(coinsRectangel);
		lastDropTime = TimeUtils.nanoTime();

	} // gameCoinsDrop

	@Override
	public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);

		//Setup Background
		Gdx.gl.glClearColor(166/255.0f, 217/255.0f, 239/255.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//About Camera
		objOrthographicCamery.update();

		//Updata Render
		batch.setProjectionMatrix(objOrthographicCamery.combined);

		//Render Screen
		batch.begin();

		batch.draw(img, 0, 0);
		myBitmapFont.draw(batch, "Score : "+score, 300, 300); //เพิ่งเปิด
		myBitmapFont.draw(batch, "Live : "+live, 300, 200); //เพิ่งเปิด

		// Draw Cat
		batch.draw(imgCat, catRectangle.x, catRectangle.y);

		// Draw Coins
		for (com.badlogic.gdx.math.Rectangle forRectangle: objCoinsDrop){
			batch.draw(imgCoins, forRectangle.x, forRectangle.y);
		} // for

		batch.end();

		//Input From Touch Screen
		if(Gdx.input.isTouched()){

			objVector3 = new Vector3();
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			objOrthographicCamery.unproject(objVector3);
			catRectangle.x = objVector3.x - 32;
		} // if

		// Control in Screen
		if(catRectangle.x <0){
			catRectangle.x = 0;
		}

		if (catRectangle.x > 736){
			catRectangle.x = 736;
		}

		// Check Time End of Drop
		if(TimeUtils.nanoTime()-lastDropTime > 1E9){
			gameCoinsDrop();
		}//if
		objIterator = objCoinsDrop.iterator();
		while (objIterator.hasNext()){
			Rectangle objMyCoins = objIterator.next();
			objMyCoins.y -=200 * Gdx.graphics.getDeltaTime();
			if(objMyCoins.y + 64<0){
				objIterator.remove();
				score--;
				live--;
				soundFalse.play();
			}// if
			if(objMyCoins.overlaps(catRectangle)){
				soundSuccess.play();
				score++;
				objIterator.remove();
			}


		} // while


	} //render
} // main class
