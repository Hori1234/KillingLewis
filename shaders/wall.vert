#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;
layout (location = 2) in vec4 normal;

out vec2 tc;
out vec3 surfaceNormal;
out vec3 toLight;
out vec3 toCamera;

uniform mat4 transformation;
uniform mat4 projection_matrix;
uniform vec3 lightPosition;
uniform mat4 viewMatrix;

void main() {
    vec4 worldCoords = transformation *vertex;
    vec4 raw_pos =  projection_matrix * viewMatrix * worldCoords;
    gl_Position = raw_pos;
    surfaceNormal = (transformation*normal).xyz;
    toLight = lightPosition - (worldCoords.xyz);
    toCamera = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldCoords.xyz;
    tc = tCoord;

}