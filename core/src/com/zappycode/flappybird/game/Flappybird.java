package com.zappycode.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Flappybird extends ApplicationAdapter {// first we chage rhe mode of the game porate
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;

	Texture gameover;

	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	int gameState = 0;
	float gravity = 2;

	Texture topTube;
	Texture bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;



	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameover = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];


		startGame();





	}

	public void startGame() {

		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		for (int i = 0; i < numberOfTubes; i++) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();

		}

	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {

				score++;

				Gdx.app.log("Score", String.valueOf(score));

				if (scoringTube < numberOfTubes - 1) {

					scoringTube++;

				} else {

					scoringTube = 0;

				}

			}

			if (Gdx.input.justTouched()) {

				velocity = -30;

			}

			for (int i = 0; i < numberOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()) {

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {

					tubeX[i] = tubeX[i] - tubeVelocity;



				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}



			if (birdY > 0) {

				velocity = velocity + gravity;
				birdY -= velocity;

			} else {

				gameState = 2;

			}

		} else if (gameState == 0) {

			if (Gdx.input.justTouched()) {

				gameState = 1;


			}

		} else if (gameState == 2) {

			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);

			if (Gdx.input.justTouched()) {

				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;


			}

		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}



		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

		font.draw(batch, String.valueOf(score), 100, 200);

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);



		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());


			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

				gameState = 2;

			}

		}

		batch.end();

		//shapeRenderer.end();



	}


}

//	SpriteBatch batch;
//	Texture background;
//	Texture[] image;
//	int flapstate=0;
//	float velocity=0;
//	float gravity=2;
//	int birdy=0;
//	int gameState=0;
//	Texture bottom;
//	Texture top;
//	Random random;
//	float gap=400;
//	float maxoffset;
//	float tubevelocity=4;
//	int numberoftubes=4;
//	float[] tubeX=new float[ numberoftubes];
//	float[] tubeoffset=new float[numberoftubes];
//	float distancebetwenntubes;
//	Circle birdcircle;// for shape of bird
//	//ShapeRenderer shapeRenderer;//=new ShapeRenderer (  );//we draw the shape of birdcircle for collosion dedication
//	Rectangle[] toprectangle;
//	Rectangle[] bottomtube;
//	int score=0;
//	int scoringtube=0;
//	BitmapFont bitmapFont;// for the score the font set
//	Texture gameover;
//
//	@Override
//	public void create () {
//		batch = new SpriteBatch();
//		background = new Texture("bg.png");
//		gameover=new Texture ( "gameover.png" );
//		bitmapFont=new BitmapFont (  );
//		bitmapFont.setColor ( Color.WHITE );// for font of score
//		bitmapFont.getData ().setScale ( 10 );// scale of font
//		birdcircle=new Circle (  );// for shape of bird
//	//	shapeRenderer=new ShapeRenderer (  );//we draw the shape of birdcircle for collosion dedication
//		image=new Texture[2];
//		image[0]=new Texture ( "bird.png" );
//		image[1]=new Texture ( "bird2.png" );
//	//	birdy=Gdx.graphics.getHeight ()/2-image[0].getHeight ();
//		random=new Random (  );
//		bottom=new Texture ( "bottomtube.png" );
//		top=new Texture ( "toptube.png" );
//		maxoffset=Gdx.graphics.getHeight ()/2-gap/2-100;
//		distancebetwenntubes=Gdx.graphics.getWidth ()*3/4;//make distance betwenn tubes
//		toprectangle=new Rectangle[numberoftubes];
//		bottomtube=new Rectangle[numberoftubes];
//		startGame();
//
//	}
//
//public void startGame(){
//	birdy=Gdx.graphics.getHeight ()/2-image[0].getHeight ()/2;
//
//	for (int i=0;i<numberoftubes;i++){
//		tubeoffset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight ()-gap-200);
//		tubeX[i]=Gdx.graphics.getWidth ()/2-top.getWidth ()/2+i*distancebetwenntubes;// we use this for shift the tube to the left there is more code
//		toprectangle[i]=new Rectangle (  );
//		bottomtube[i]=new Rectangle (  );
//
//	}
//}
//	@Override
//	public void render () {
//		batch.begin ();
//		batch.draw ( background, 0, 0, Gdx.graphics.getWidth (), Gdx.graphics.getHeight () );
//		if (gameState==1) {
//			if (tubeX[scoringtube]<Gdx.graphics.getWidth ()/2){// this for score
//				score++;
//				Gdx.app.log ( "Score",String.valueOf ( score ) );
//				if (scoringtube<numberoftubes-1) {// because we have 0,1,2,3 tubes
//					scoringtube++;
//
//				}
//				else {
//					scoringtube=0;
//				}
//			}
//			if (Gdx.input.justTouched ()) {
//				velocity=-20;
//			//	tubeoffset=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight ()-gap-200);
//			//	tubeX=Gdx.graphics.getWidth ()/2-top.getWidth ()/2;// we use this for shift the tube to the left there is more code
//
//			}
//			for (int i=0;i<numberoftubes;i++) {
//				if (tubeX[i]<-top.getWidth ()) {// is the tubex is in the corner so we do thid
//
//					tubeX[i]+=numberoftubes*distancebetwenntubes;
//					tubeoffset[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight ()-gap-200);
//				}else {
//				tubeX[i] -= tubevelocity;
//
//				}
//
//				batch.draw ( top, tubeX[i], Gdx.graphics.getHeight () / 2 + gap / 2 + tubeoffset[i] );
//				batch.draw ( bottom, tubeX[i ], Gdx.graphics.getHeight () / 2 - gap / 2 - bottom.getHeight () + tubeoffset[i] );
//				toprectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i], top.getWidth(), top.getHeight());
//				bottomtube[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottom.getHeight() + tubeoffset[i], bottom.getWidth(), bottom.getHeight());
//			}
//
//			if (birdy > 0) {
//
//				velocity = velocity + gravity;
//				birdy -= velocity;
//
//			} else {
//
//				gameState = 2;
//
//			}
//
//		} else if (gameState == 0) {
//
//			if (Gdx.input.justTouched()) {
//
//				gameState = 1;
//
//
//			}
//
//		} else if (gameState == 2) {
//
//			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
//
//			if (Gdx.input.justTouched()) {
//
//				gameState = 1;
//				startGame();
//				score = 0;
//				scoringtube = 0;
//				velocity = 0;
//
//
//			}
//
//		}
//		if (flapstate == 0) {
//			flapstate = 1;
//		} else {
//			flapstate = 0;
//		}
//		//batch.begin ();
//	// 	batch.draw ( background, 0, 0, Gdx.graphics.getWidth (), Gdx.graphics.getHeight () );
//
//
//		batch.draw ( image[flapstate], Gdx.graphics.getWidth () / 2 - image[flapstate].getWidth () / 2, birdy );
//		bitmapFont.draw ( batch,String.valueOf ( score ),100,200 );// for score displaying
//		birdcircle.set ( Gdx.graphics.getWidth ()/2,birdy ,image[flapstate].getWidth () / 2);
//
//
//
////		shapeRenderer.begin (ShapeRenderer.ShapeType.Filled);// it is same as batch but  batch is not for collision
////		shapeRenderer.setColor ( Color.BLUE );
////		shapeRenderer.circle ( birdcircle.x,birdcircle.y,birdcircle.radius );
//		for (int i=0;i<numberoftubes;i++) {
//			// shape for the top and bottom tube
////			shapeRenderer.rect (tubeX[i], Gdx.graphics.getHeight () / 2 + gap / 2 + tubeoffset[i],top.getWidth (),top.getHeight ()  );
////			shapeRenderer.rect ( tubeX[i], Gdx.graphics.getHeight () / 2 - gap / 2 - bottom.getHeight () + tubeoffset[i],bottom.getWidth (),bottom.getHeight () );
//			if (Intersector.overlaps ( birdcircle,toprectangle[i])||Intersector.overlaps ( birdcircle,bottomtube[i])) {// for collision
//				Gdx.app.log ( "Collsion","yes" );
//				gameState=2;//game over
//			}
//		}
//	//	shapeRenderer.end ();
//		batch.end ();
//	}
//	@Override
//	public void dispose () {
//		batch.dispose();
//		//img.dispose();
//	}
//}
