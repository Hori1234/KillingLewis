#version 330 core

out vec4 color;

in vec2 tc;
in vec3 surfaceNormal;
in vec3 toLight;
in vec3 toCamera;

uniform sampler2D tex;
uniform vec3 lightColor;
float shine = 50;
float reflectivity = 1;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitToLight = normalize(toLight);
    vec3 unitToCamera = normalize(toCamera);
    vec3 lightSourceDirection = -unitToLight;
    vec3 reflectedLightDirection = reflect(lightSourceDirection, unitNormal);

    float specular_coeff = dot(reflectedLightDirection, unitToCamera);
    specular_coeff = max(specular_coeff, 0.0);
    float shine = pow(specular_coeff,shine);
    vec3 specular = shine * reflectivity * lightColor;
    float ndot1 = dot(unitNormal,unitToLight);
    float brightness = max(ndot1, 0.3);
    vec3 diffuse = brightness * lightColor;
    color = vec4(specular,1.0) + vec4(diffuse,1.0)* texture(tex, tc);
}