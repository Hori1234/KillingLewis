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


void main() {
    vec4 raw_pos = projection_matrix * transformation * vertex;
    gl_Position = raw_pos / raw_pos.w;
    surfaceNormal = (transformation*normal).xyz;
    toLight = lightPosition - (raw_pos.xyz);
    tc = tCoord;
}