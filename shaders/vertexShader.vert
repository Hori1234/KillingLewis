#version 330 core

void main() {
    const vec3 vertices[3] = vec3[3] (
        vec3(-0.5, 0.5, 0.5),
        vec3(0.5, 0.5, 0.5),
        vec3(0.5, -0.5, 0.5)
    );

    gl_Position = vec4(vertices[gl_VertexID], 1.0);
}