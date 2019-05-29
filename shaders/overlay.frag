#version 330 core

out vec4 color;

in vec2 tc;

uniform sampler2D tex;
uniform float progress;

void main() {
    if (tc.x > progress) {
        color = vec4(0.2f, 0.2f, 0.2f, 1.0f) * texture(tex, tc);
    } else {
        color = texture(tex, tc);
    }
}