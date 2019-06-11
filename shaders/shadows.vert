#version 330 core

layout (location = 0) in vec4 vertex;

uniform mat4 mvpMatrix;

void main() {
    gl_Position = mvpMatrix * vertex;
}
