package com.slamdunk.simplegame.screen;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slamdunk.simplegame.SpaceGame;
import com.slamdunk.simplegame.entities.Asteroid;
import com.slamdunk.simplegame.entities.Bullet;
import com.slamdunk.simplegame.entities.Enemy;
import com.slamdunk.simplegame.entities.EnemyBoss;
import com.slamdunk.simplegame.entities.Explosion;
import com.slamdunk.simplegame.tools.BoxCollider;

public class GameScreen implements Screen
{
	private static final int POSITION_DIFFERENCE_BUFEER = 5;//More for less jitter
	private static final float ENEMY_SPAWN_TIME = 2f;
	private static final float FULLHEALTH = 200f;
	private static final float BULLET_SPAWN_TIME = 0.5f;
	private static final float ASTEROID_SPAWN_TIME = 2f;
	private static final float ANIMATION_SPEED = 0.2f;
	private static final float TILT_LIMIT = 0.5f;
	private static final int SHIP_SIZE_SCALE = 3;
	private static final int SHIP_PIXEL_WIDTH = 17;
	public static final int SHIP_PIXEL_HEIGHT = 32;
	public static final int SHIP_WIDTH = SHIP_PIXEL_WIDTH * SHIP_SIZE_SCALE;
	public static final int SHIP_HEIGHT = SHIP_PIXEL_HEIGHT * SHIP_SIZE_SCALE;
	
	public static GameScreen instance;
	
	Animation[] shipAnims;
	public ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> enemyBulletsToRemove = new ArrayList<Bullet>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
	float stateTime = 0;
	float x = SpaceGame.WIDTH/2 - SHIP_WIDTH/2;
	float y = 15 + SHIP_HEIGHT/2;
	float speed = 200;
	private SpaceGame game;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
	private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	private ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	private ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
	private int animIndex = 0;
	private float pressedTime = 0;
	private float pressedTimeLimit = 1.0f;
	private float asteroidCreateTimer = 0f;
	private Random random;
	
	private float loadGameOverInSeconds = 2;
	private float loadGameOverTimer = 0f;
	
	private BoxCollider collider;
	
	private BitmapFont font;
	private GlyphLayout layout;
	private int score = 0;
	
	private float health;
	private float damage = 40f;
	private boolean playingGame = true;
	private float bulletTime = 0;
	private float enemySpawnTime = 0;
	
	public static float enemySpawnRateMultiplier;
	public static float asteroidSpawnRateMultiplier;
	public static float moveSpeedMultiplier = 3;
	public static int comboMultiplier;
	
	private boolean isBossPresent = false;
	private float gameTimer = 0;
	private int gameLevel = 0;
	private boolean enableYAxis = false;
	
	Texture blank = new Texture("blank.png");
	
	public GameScreen(SpaceGame game, boolean enableYAxis)
	{
		this.game = game;
		this.enableYAxis = enableYAxis;
		instance = this;
	}
	
	@Override
	public void show()
	{
		Texture shipTexture = new Texture("SpriteSheet_Ship.png");
		font = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		layout = new GlyphLayout();
		TextureRegion[][] region = TextureRegion.split(shipTexture, SHIP_PIXEL_WIDTH,SHIP_PIXEL_HEIGHT);
		random = new Random();
		
		shipAnims = new Animation[5];		
		for(int i = 0; i < shipAnims.length; i++)
		{
			shipAnims[i] = new Animation<TextureRegion>(ANIMATION_SPEED, region[i]);
		}
		
		collider = new BoxCollider(x, y, SHIP_WIDTH, SHIP_HEIGHT);
		
		health = FULLHEALTH;
		updateMultiplier(1, 1, 1, 1, 1, 1);
	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.3f, 0.1f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stateTime += delta;
		enemySpawnTime += delta;
		
		game.batch.begin();
		game.batch.setColor(Color.WHITE);
		
		layout.setText(font, "" + score);
		font.draw(game.batch, layout, SpaceGame.WIDTH/2 - layout.width/2, SpaceGame.HEIGHT - 40);
		
		myGame(delta);
		destroyUsedEntities();
		createAsteroids(delta);
		renderAndUpdateAsteroids(delta);
		renderAndUpdateBullets(delta);
		renderAndUpdateExplosion(delta);
		TouchInput(delta, enableYAxis);
		autoBullets(delta);
		//ButtonInput(delta);
		//ShipAnimation();
		CollisionCheck();
		
		if(enemySpawnTime >= ENEMY_SPAWN_TIME/enemySpawnRateMultiplier)
		{
			spawnEnemies();
			enemySpawnTime = 0;
		}
		
		renderAndUpdateEnemy(delta);
		renderAndUpdateEnemyBullets(delta);
		
		if(health <= 0)
		{
			health = 0;
			Explosion explosion = new Explosion(x - SHIP_WIDTH/2, y);
			explosion.setScale(3);
			explosions.add(explosion);
			
			playingGame = false;
		}
		
		if(!playingGame)
		{
			if(loadGameOverTimer < loadGameOverInSeconds)
				loadGameOverTimer += delta;
			else
				game.setScreen(new EndGameScreen(game,score));
		}
		
		game.batch.draw((TextureRegion)shipAnims[animIndex].getKeyFrame(stateTime,true), x , y, SHIP_WIDTH, SHIP_HEIGHT);
		collider.Move(x, y);
		
		game.batch.setColor(Color.GREEN);
		game.batch.draw(blank, x + SHIP_WIDTH + 10, y + 5, 5, SHIP_HEIGHT * 0.8f * health / FULLHEALTH);
		game.batch.setColor(Color.WHITE);
		
		game.batch.end();
	}
	
	private void myGame(float delta)
	{
		gameTimer += delta;
		
		if(gameLevel == 0 && gameTimer >= 20.0f)
		{
			gameLevel = 1;
			gameTimer = 0f;
			float rate = 1.2f;
			updateMultiplier(2, rate, rate, rate/2, rate, rate/2);
		}
		
		if(gameLevel == 1 && gameTimer >= 20f)
		{
			gameLevel = 2;
			gameTimer = 0f;
			float rate = 1.6f;
			updateMultiplier(5, rate, rate, rate/1.5f, rate, rate);
		}
		
	}
	
	private void renderAndUpdateEnemy(float delta)
	{
		for(Enemy enemy : enemies)
		{
			if(enemy != null)
			{
				enemy.render(game.batch);
				
				if(!enemy.destroy)
					enemy.update(delta);
				else
					enemiesToRemove.add(enemy);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() 
	{
		
	}
	
	private void renderAndUpdateBullets(float delta)
	{
		for(Bullet bullet : bullets)
		{
			bullet.render(game.batch);
			
			if(!bullet.destroy)
				bullet.update(delta);
			else
				bulletsToRemove.add(bullet);
		}
	}
	
	private void ShipAnimation()
	{
		if(pressedTime < 0f)
		{
			if(pressedTime > -TILT_LIMIT)
				animIndex = 1;
			else
				animIndex = 2;
		}
		else if(pressedTime > 0f)
		{
			if(pressedTime < TILT_LIMIT)
				animIndex = 3;
			else
				animIndex = 4;
		}
		else
			animIndex = 0;
	}
	
	private void shipAnimationForTouch(float posDiff)
	{
		posDiff = posDiff/Math.abs(posDiff);

		if(posDiff < -0.1f)
		{
			animIndex = 3;
		}
		else if(posDiff > 0.1f)
		{
			animIndex = 1;
		}
		else
			animIndex = 0;
	}
	
	private void TouchInput(float delta, boolean enableYAsix)
	{
		if(!playingGame)
			return;

		float posDiffX = 0;
		float posDiffY = 0;
		if(Gdx.input.isTouched(0))
		{
			posDiffX = (x - SpaceGame.camera.getWorldPoints().x) + SHIP_WIDTH/2;
			posDiffY = (y - SpaceGame.camera.getWorldPoints().y);
			//6 pixels buffer added
//			if(Math.abs(posDiffX) < 6 && Math.abs(posDiffY) < 6)
//			{
//				shipAnimationForTouch(0);
//				return;
//			}
			
			if(posDiffX > POSITION_DIFFERENCE_BUFEER  && x > 0)
				x -= speed * delta * moveSpeedMultiplier;

			if(posDiffX < -POSITION_DIFFERENCE_BUFEER && x < SpaceGame.WIDTH - SHIP_WIDTH)
				x += speed * delta * moveSpeedMultiplier;

			if(enableYAsix)
			{
				if(posDiffY > POSITION_DIFFERENCE_BUFEER  && y > 0)
					y -= speed * delta * moveSpeedMultiplier;

				if(posDiffY < -POSITION_DIFFERENCE_BUFEER && y < SpaceGame.HEIGHT - SHIP_HEIGHT)
					y += speed * delta * moveSpeedMultiplier;
			}
		}
		shipAnimationForTouch(0);
	}
	
	private void spawnEnemies()
	{
		Enemy enemy;
		int xPos = (int)(random.nextFloat() * SpaceGame.WIDTH * 0.8f) + SHIP_WIDTH;
		int bossProb = random.nextInt(100);
		
		if(!isBossPresent && bossProb >= 90)
		{
			enemy = new EnemyBoss(xPos);
			isBossPresent = true;
		}
		else
			enemy = new Enemy(xPos);
		
		enemies.add(enemy);
	}
	
	private void ButtonInput(float delta)
	{
		if(!playingGame)
			return;
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			if(pressedTime < pressedTimeLimit)
				pressedTime += 3 * delta;
			
			if(x < SpaceGame.WIDTH - SHIP_WIDTH)
				x += speed * Gdx.graphics.getDeltaTime();
		}
		else if(Gdx.input.isKeyPressed(Keys.LEFT))
		{
			if(pressedTime > -pressedTimeLimit)
				pressedTime -= 3 * delta;
			
			if(x > 0)
				x -= speed * Gdx.graphics.getDeltaTime();
		}
		else if(pressedTime != 0)
		{
			if(pressedTime > 0)
				pressedTime -= 3 * delta;
			else
				pressedTime += 3 * delta;
			
			if(Math.abs(pressedTime) < 0.05f)
				pressedTime = 0;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			Bullet bullet1 = new Bullet(x + SHIP_WIDTH/2 + 13, y + SHIP_HEIGHT - 25, 90);
			Bullet bullet2 = new Bullet(x + SHIP_WIDTH/2 - 17, y + SHIP_HEIGHT - 25, 90);
			bullets.add(bullet1);
			bullets.add(bullet2);
		}
	}
	
	private void autoBullets(float delta)
	{
		bulletTime += delta;
		
		if(bulletTime >= BULLET_SPAWN_TIME)
		{
			Bullet bullet1 = new Bullet(x + SHIP_WIDTH/2 + 13, y + SHIP_HEIGHT - 25, 90);
			Bullet bullet2 = new Bullet(x + SHIP_WIDTH/2 - 17, y + SHIP_HEIGHT - 25, 90);
			bullets.add(bullet1);
			bullets.add(bullet2);
			
			bulletTime = 0;
		}
	}
	
	private void createAsteroids(float deltaTime)
	{		
		asteroidCreateTimer += deltaTime;
		if(asteroidCreateTimer >= ASTEROID_SPAWN_TIME/asteroidSpawnRateMultiplier)
		{
			float x = (random.nextFloat() * SpaceGame.WIDTH * 0.9f) + 10;
			
			Asteroid asteroid = new Asteroid(x);
			asteroids.add(asteroid);
			asteroidCreateTimer = 0;
		}
	}
	
	private void renderAndUpdateAsteroids(float delta)
	{
		for(Asteroid ast : asteroids)
		{
			if(ast != null)
			{
				ast.render(game.batch);
				
				if(!ast.destroy) 
					ast.update(delta);
				else
					asteroidsToRemove.add(ast);
			}
		}
	}
	
	public void updateMultiplier(int combo, float asteroidSpawnRate,
			float asteroidDropSpeed,float enemySpawnRate, float enemyMoveSpeed, float enemyFireRate)
	{
		comboMultiplier = combo;
		asteroidSpawnRateMultiplier = asteroidSpawnRate;
		Asteroid.speedMultiplier = asteroidDropSpeed;
		enemySpawnRateMultiplier = enemySpawnRate;
		Enemy.speedMultiplier = enemyMoveSpeed;
		Enemy.fireRateMultiplier = enemyFireRate;
	}
	
	private void CollisionCheck()
	{
		//Asteroid Collision
		for(Asteroid asteroid : asteroids)
		{
			for(Bullet bullet : bullets)
			{
				//WithBullet
				if(asteroid.getCollider().CollidesWith(bullet.getCollider()))
				{
					score += 100 * comboMultiplier;
					
					Explosion explosion = new Explosion(asteroid.center.x, asteroid.center.y);
					explosions.add(explosion);
					
					asteroid.getCollider().SetActive(false);
					asteroidsToRemove.add(asteroid);
					bulletsToRemove.add(bullet);
				}
			}
			
			//WithPlayer
			if(asteroid.getCollider().CollidesWith(collider))
			{
				health -= damage;
				
				Explosion explosion = new Explosion(asteroid.center.x, asteroid.center.y);
				explosions.add(explosion);
				asteroidsToRemove.add(asteroid);
			}
		}
		
		//EnemyBullet Collision
		for(Bullet bullet : enemyBullets)
		{
			//With Player
			if(bullet.getCollider().CollidesWith(collider))
			{
				health -= damage;
				
				Explosion explosion = new Explosion(bullet.position().x, bullet.position().y);
				explosions.add(explosion);
				enemyBulletsToRemove.add(bullet);
			}
		}
		
		//Enemies Collision
		for(Enemy enemy : enemies) 
		{
			//With Player Bullets
			if(enemy != null)
			{
				for(Bullet bullet : bullets)
				{
					if(bullet != null && bullet.getCollider().CollidesWith(enemy.getCollider()))
					{
						enemy.takeDamage();
						
						Explosion explosion = new Explosion(bullet.position().x, bullet.position().y);
						explosions.add(explosion);
						bulletsToRemove.add(bullet);
					}
				}				
				//With Player
				if(enemy.getCollider().CollidesWith(collider))
				{ 
					float eHealth = enemy.getHealth();
					health -= eHealth;
					enemy.forceDestroy();
					
					Explosion explosion = new Explosion(enemy.position().x, enemy.position().y);
					explosions.add(explosion);
				}
				
				if(enemy.getHealth() <= 0)
				{
					if(enemy instanceof EnemyBoss) 
					{
						score += 5000 * comboMultiplier;
						isBossPresent = false;
					}
					else
						score += 500 * comboMultiplier;
					
					enemy.destroy = true;
					enemiesToRemove.add(enemy);
				}
			}			
		}
		
	}
	
	private void renderAndUpdateEnemyBullets(float delta)
	{
		for(Bullet bull : enemyBullets)
		{
			if(bull != null)
			{
				bull.render(game.batch);
				
				if(!bull.destroy) 
					bull.update(delta);
				else
					enemyBulletsToRemove.add(bull);				
			}
		}
	}
	
	private void renderAndUpdateExplosion(float delta)
	{
		for(Explosion explosion : explosions)
		{
			if(explosion != null)
			{
				explosion.render(game.batch);
				
				if(!explosion.destroy) 
					explosion.update(delta);
				else
					explosionsToRemove.add(explosion);
			}			
		}
	}
	
	private void destroyUsedEntities()
	{
		for(Bullet bullet : bulletsToRemove)
		{
			if(bullet != null)
			{
				bullet.destroy();
				bullets.remove(bullet);
			}
		}
		bulletsToRemove.clear();
		
		for(Bullet bullet : enemyBulletsToRemove)
		{
			if(bullet != null)
			{
				bullet.destroy();
				enemyBullets.remove(bullet);
			}
		}
		enemyBulletsToRemove.clear();
		
		for(Asteroid asteroid : asteroidsToRemove)
		{
			if(asteroid != null)
			{
				asteroid.destroy();
				asteroids.remove(asteroid);
			}
		}
		asteroidsToRemove.clear();
		
		for(Explosion explosion : explosionsToRemove)
		{
			if(explosion != null)
			{
				explosion.destroy();
				explosions.remove(explosion);
			}
		}
		explosionsToRemove.clear();
		
		for(Enemy enemy : enemiesToRemove)
		{
			if(enemy != null)
			{
				enemy.destroy = true;
				enemies.remove(enemy);
			}
		}
		enemiesToRemove.clear();
	}

}
