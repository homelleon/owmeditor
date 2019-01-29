in vec2 textureCoords;
in vec3 normal;
in vec3 lightVector;
in vec3 toCameraVector;

uniform sampler2D diffuseMap;

const float shininess = 1;

void main(void) {
	
	vec4 baseColor = texture(diffuseMap, textureCoords);	
	
	vec4 ambientColor = vec4(0.25, 0.25, 0.25, 1);
	vec4 diffuseColor = vec4(0.5, 0.5, 0.5, 1);
	vec4 specularColor = vec4(0.25, 0.25, 0.25, 1);
	
	PhongVectors phongVectors = PhongVectors(normal, lightVector, toCameraVector);
	PhongColors phongColors = PhongColors(ambientColor, diffuseColor, specularColor);
	PhongValues phongValues = PhongValues(shininess);
	vec4 finalLightColor = phongLighting(phongColors, phongVectors, phongValues);
	gl_FragColor = baseColor * finalLightColor;
	
}