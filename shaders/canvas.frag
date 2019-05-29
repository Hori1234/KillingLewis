#version 330 core

out vec4 color;

in vec2 tc;

uniform sampler2D tex;
uniform vec3 square_color;
uniform float square_transparency;

void main() {
    color = vec4(square_color, square_transparency) * texture(tex, tc);
}