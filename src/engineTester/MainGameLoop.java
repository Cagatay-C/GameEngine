package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay(); 
		Loader loader = new Loader();
		// *****************TERRAIN TEXTURE STUFF*******************
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel tree = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
				new ModelTexture(loader.loadTexture("treeTexture")));
		
		TexturedModel bunnyModel = new TexturedModel(OBJLoader.loadObjModel("stanfordBunny", loader),
				new ModelTexture(loader.loadTexture("white")));

		
		
		grass.getTexture().setHasTransparency(true);
//		grass.getTexture().setShineDamper(10);
//		grass.getTexture().setReflectivity(1);
		grass.getTexture().setUseFakeLighting(true);
		
		Entity entityGrass = new Entity(grass, new Vector3f(100,0,-25),0,0,0,1);
		Entity entityGrass2 = new Entity(grass, new Vector3f(50,0,-100),0,0,0,1);
		Entity entity2 = new Entity(tree, new Vector3f(100,0,-25),0,0,0,1);
		Entity entity3 = new Entity(tree, new Vector3f(50,0,-100),0,0,0,2);
		Light light = new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap,"heightMap");
		
		Player player = new Player(bunnyModel,new Vector3f(100,0,-50),0,180,0,0.6f);
		Camera camera = new Camera(player);
				
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processEntity(entityGrass);
			renderer.processEntity(entityGrass2);
			renderer.processEntity(entity2);
			renderer.processEntity(entity3);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
