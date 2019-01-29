#version 430

// Tools
float saturate(float value) {
	return clamp(value, 0.0, 1.0);
}

// Crypting 
vec4 encodeFloat(float f) {
  vec4 enc = vec4(1.0, 255.0, 65025.0, 16581375.0) * f;
  enc = fract(enc);
  enc -= enc.yzww * vec4(1.0 / 255.0, 1.0 / 255.0, 1.0 / 255.0, 0.0);
  return enc;
}

float decodeFloat(vec4 rgba) {
  return dot(rgba, vec4(1.0, 1 / 255.0, 1 / 65025.0, 1 / 16581375.0));
}

// Lighting
float diffuseLightFactor(vec3 normalVectorNormalized, vec3 lightVectorNormalized) {
	float angleFactor = dot(normalVectorNormalized, lightVectorNormalized);
	
	return max(0.0, angleFactor);
}

float specularLightFactor(vec3 normalVectorNormalized, vec3 lightVectorNormalized, vec3 toCameraVectorNormalized) {
	vec3 toLightVector = -lightVectorNormalized;
	vec3 reflectedLightVector = reflect(toLightVector, normalVectorNormalized);
	float angleFactor = dot(reflectedLightVector, toCameraVectorNormalized);
	
	return max(0.0, angleFactor);
}

vec4 phongLighting(vec4 ambient, vec4 diffuse, vec4 specular) {
	return ambient + diffuse + specular;
}

struct PhongColors {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

struct PhongVectors {
	vec3 normal;
	vec3 light;
	vec3 toCamera;
};

struct PhongValues {
	float shininess;
};

vec4 phongLighting(PhongColors colors, PhongVectors vectors, PhongValues values) {
	vec3 normalNormalized = normalize(vectors.normal);
	vec3 lightVectorNormalized = normalize(vectors.light);
	float diffuseFactor = diffuseLightFactor(normalNormalized, lightVectorNormalized);
	float specularFactor = specularLightFactor(lightVectorNormalized, lightVectorNormalized, normalize(vectors.toCamera));
	
	vec4 finalDiffuse = colors.diffuse * diffuseFactor;
	vec4 finalSpecular = colors.specular * pow(specularFactor, values.shininess);
	
	return phongLighting(colors.ambient, finalDiffuse, finalSpecular);
}