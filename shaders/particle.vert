#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;
layout (location = 2) in vec4 normal;

out vec2 tc;


uniform mat4 transformation;
uniform mat4 projection_matrix;
uniform vec3 lightPosition;
uniform mat4 viewMatrix;

void main() {
    vec4 worldCoords = transformation *vertex;
    vec4 raw_pos =  projection_matrix * viewMatrix * worldCoords;
    gl_Position = raw_pos;
    tc = tCoord;

}