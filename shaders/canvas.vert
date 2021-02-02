#version 330 core

layout (location = 0) in vec4 vertex;
layout (location = 1) in vec2 tCoord;
layout (location = 2) in vec4 normal;

out vec2 tc;

uniform mat4 transformation;

void main() {
    vec4 raw_pos = transformation * vertex;
    gl_Position = raw_pos / raw_pos.w;
    tc = tCoord;
}