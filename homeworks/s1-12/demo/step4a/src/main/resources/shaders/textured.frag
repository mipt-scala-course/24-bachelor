#version 330 core

in vec2 UV;

out vec3 color;

uniform Sampler2D index;

void main() {

    color = texture(index, UV).rgb;

}
