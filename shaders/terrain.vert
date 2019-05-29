#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;
layout (location = 2) in vec4 normal;

out vec2 tc;
out vec3 surfaceNormal;
out vec3 toLight;

uniform mat4 transformation;
uniform mat4 projection_matrix;
uniform vec3 lightPosition;
uniform mat4 viewMatrix;

void main() {
    vec4 worldCoords = transformation *vertex;
    vec4 raw_pos = projection_matrix * viewMatrix * transformation * vertex;
    gl_Position = raw_pos / raw_pos.w;
    surfaceNormal = (transformation*normal).xyz;
    toLight = lightPosition - (worldCoords.xyz);
    tc = tCoord;
}