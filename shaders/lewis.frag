#version 330 core

out vec4 color;

in vec2 tc;
in vec3 surfaceNormal;
in vec3 toLight;

uniform sampler2D tex;
uniform vec3 lightColor;

void main() {
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightDirection = normalize(toLight);

    float ndot1 = dot(unitNormal,unitLightDirection);
    float brightness = max(ndot1, 0.0);

    vec3 diffuse = brightness * lightColor;
    color = vec4(diffuse,1.0)* texture(tex, tc);
}