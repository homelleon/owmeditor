in vec3 Position;
in vec3 Normal;
in vec2 TexCoords;

out vec2 textureCoords;
out vec3 normal;
out vec3 lightVector;
out vec3 toCameraVector;

uniform mat4 Transformation;
uniform mat4 Projection;
uniform mat4 View;

const vec3 mainLightPosition = vec3(1000, 1000, 1000);

void main(void) {
	
	vec4 worldPosition = Transformation * vec4(Position, 1.0);
	vec4 positionRelativeToCam = View * worldPosition;
	gl_Position = Projection * positionRelativeToCam;
	
	normal = Normal;
	
	textureCoords = vec2(TexCoords.xy);
	
	lightVector = mainLightPosition - worldPosition.xyz;
	toCameraVector = (inverse(View) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
			
}