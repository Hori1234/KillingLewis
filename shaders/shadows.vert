#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;
layout (location = 2) in vec4 normal;

uniform mat4 modelLightViewMatrix;
uniform mat4 projection_matrix;

void main() {
    gl_Position = modelLightViewMatrix * projection_matrix * vertex;
}
