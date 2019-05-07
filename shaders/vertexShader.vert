#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;

out vec2 tc;

uniform mat4 view_matrix;

void main() {
    gl_Position = view_matrix * vertex;
    tc = tCoord;
}