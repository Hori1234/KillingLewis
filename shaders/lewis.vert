#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;

out vec2 tc;

uniform mat4 transformation;
uniform mat4 projection_matrix;

void main() {
    vec4 raw_pos = projection_matrix * transformation * vertex;
    gl_Position = raw_pos / raw_pos.w;
    tc = tCoord;
}