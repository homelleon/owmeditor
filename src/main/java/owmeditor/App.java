package owmeditor;

import java.util.stream.IntStream;

import org.lwjgl.opengl.GL11;

import outworldmind.owme.core.Config;
import outworldmind.owme.core.Engine;
import outworldmind.owme.core.Keyboard;
import outworldmind.owme.core.Logger;
import outworldmind.owme.core.Mouse;
import outworldmind.owme.core.Tools;
import outworldmind.owme.core.Window;
import outworldmind.owme.graphics.Material;
import outworldmind.owme.graphics.Model;
import outworldmind.owme.graphics.RenderState;
import outworldmind.owme.graphics.Renderer;
import outworldmind.owme.graphics.TextureBuilder;
import outworldmind.owme.maths.Color;
import outworldmind.owme.maths.Rotation;
import outworldmind.owme.maths.Vector3;
import outworldmind.owme.shaders.EntityShader;
import outworldmind.owme.tools.modelUtils.BoxGeometryBuilder;
import outworldmind.owme.tools.modelUtils.objLoader.OBJModelLoader;
import outworldmind.owme.units.Camera;
import outworldmind.owme.units.DrawUnit;
import outworldmind.owme.units.Scene;

public class App {
	
    public static void main(String[] args ) {
    	var config = new Config();
    	
    	var windowName = "OWMEditor Test";
    	config.setParam(Config.WINDOW_NAME, windowName);
    	config.setParam(Config.WINDOW_WIDTH, 1024);
    	config.setParam(Config.WINDOW_HEIGHT, 768);
    	
    	config.setParam(Config.CONSOLE_MODE, Logger.CONSOLE_LOG_MODE);
    	
    	config.setParam(Config.CAMERA_FOV, 50f);
    	config.setParam(Config.CAMERA_NEAR_PLANE, 0.1f);
    	config.setParam(Config.CAMERA_FAR_PLANE, 1000f);
    	
    	var owme = new Engine(config);
    	owme.start();
    	
    	var renderer = new Renderer(
        		new RenderState()
        			.setClearColor(new Color(0f, 0f, 0))
        			.setDepthTestMode(RenderState.DEPTH_LESS)
        			.setCullFaceMode(RenderState.CULL_BACK)
        	);
    	
    	var geometry = new BoxGeometryBuilder()
    			.setWidth(1)
    			.setHeight(1)
    			.setDepth(1)
    			.build();

    	var material1 = new Material()
    			.addTexture(Material.DIFFUSE, 
    					new TextureBuilder()
	    	    			.setName(Material.DIFFUSE)
	    	    			.setPath("/image/grass.png")
	    	    			.setType(GL11.GL_TEXTURE_2D)
	    	    			.setBindLocation(0)
	    	    			.build());

    	var camera = new Camera(config);
    	
    	camera
    		.move(new Vector3(0, 1.0f, 0))
    		.rotate(new Rotation(-25, 0, 0));
    	
    	var shader = new EntityShader();
    	
    	var unit = new DrawUnit()
        		.move(new Vector3(0, 0, -3))
        		.scaleTo(new Vector3(1))
    			.setModel(new Model()
	    			.setGeometry(geometry)
	    			.setMaterial(material1)
	    			.setRenderer(renderer)
	    			.setShader(shader));
    	
    	var spartan = new DrawUnit()
        		.move(new Vector3(1, 0.1f, 3))
        		.scaleTo(new Vector3(0.005f))
    			.setModel(new OBJModelLoader()
    					.load("/model/spartan/", "spartan")[0]
	    			.setRenderer(renderer)
	    			.setShader(shader));

    	var scene = new Scene()
    			.setCamera(camera)
    			.add(unit);
    	
    	
    	IntStream.range(0, 5000)
    		.mapToObj(index -> spartan.clone().move(
    				new Vector3(Math.floorDiv(index, 100) * -0.2f, 
					0, 
					-0.5f * Math.floorMod(index, 10) -0.2f * Math.floorMod(index, 100))))
    		.forEach(scene::add);
    	
    	var window = owme.getWindow(windowName);
    	
    	Tools.getControls().getKeyboard().bindKey(event -> {
			if (Keyboard.keyReleased(Keyboard.KEY_ESCAPE, window, event))
				owme.getWindows().forEach(Window::close);
    		if (Keyboard.keyHold(Keyboard.KEY_A, window, event))
    			camera.move(new Vector3(0, 0, 2));
    		if (Keyboard.keyHold(Keyboard.KEY_D, window, event))
    			camera.move(new Vector3(0, 0, -2));
    		if (Keyboard.keyPressed(Keyboard.KEY_W, window, event))
    			camera.move(new Vector3(2, 0, 0));
    		if (Keyboard.keyPressed(Keyboard.KEY_S, window, event))
    			camera.move(new Vector3(-2, 0, 0));
    		if (Keyboard.keyPressed(Keyboard.KEY_SPACE, window, event))
    			camera.move(new Vector3(0, 2, 0));
    		if (Keyboard.keyPressed(Keyboard.KEY_C, window, event))
    			camera.move(new Vector3(0, -2, 0));
    	});
    	
    	Tools.getControls().getMouse().bindMouseButton(event -> {
    	});
    	
    	Tools.getControls().getMouse().bindMouseMove(event -> {
    		var speed = 0.1f;
    		if (Mouse.movedLeft(event))
    			camera.rotate(new Rotation(0, Mouse.getDPos(event).x * speed, 0));
    		if (Mouse.movedRight(event))
    			camera.rotate(new Rotation(0, Mouse.getDPos(event).x * speed, 0));
    		if (Mouse.movedUp(event))
    			camera.rotate(new Rotation(Mouse.getDPos(event).y * speed, 0, 0));
    		if (Mouse.movedDown(event))
    			camera.rotate(new Rotation(Mouse.getDPos(event).y * speed, 0, 0));
    	});
    	
    	owme.getWindow(windowName).grabMouse();
    	
    	while (true) {
    		if (window.getCloseRequest()) break;
    		owme.update();
    		scene.draw();
    	}
    	owme.stop();
    }
}

