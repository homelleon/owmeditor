package outworldmind.owmeditor.shaders;

import java.util.List;

import outworldmind.owme.graphics.Shader;
import outworldmind.owme.graphics.ShaderVariable;
import outworldmind.owme.graphics.Texture;
import outworldmind.owme.maths.Matrix4;
import outworldmind.owme.maths.Vector3;
import outworldmind.owme.tools.FileLoader;

public class EntityShader extends Shader {
	
	private static final String VERTEX_SHADER = "/shader/entity_v.glsl";
	private static final String FRAGMENT_SHADER = "/shader/entity_f.glsl";
	
	public EntityShader() {
		super();
		addStages();
		addVariables();
		init();
	}
	
	private void addStages() {
		addVertexStage(FileLoader.INSTANCE.load(VERTEX_SHADER));
		addFragmentStage(FileLoader.INSTANCE.load(FRAGMENT_SHADER));
	}
	
	private void addVariables() {
		addVariables(List.of(
			new ShaderVariable(DIFFUSE_MAP, new Texture()),
			new ShaderVariable(TRANSFORMATION_MATRIX, new Matrix4()),
			new ShaderVariable(PROJECTION_MATRIX, new Matrix4()),
			new ShaderVariable(VIEW_MATRIX, new Matrix4())		
		));
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, POSITION_ATTRIBUTE);
		bindAttribute(1, NORMAL_ATTRIBUTE);
		bindAttribute(2, TEXCOORDS_ATTRIBUTE);
	}
	
}
