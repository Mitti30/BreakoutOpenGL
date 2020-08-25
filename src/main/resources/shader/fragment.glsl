#version 410
precision mediump float;

in lowp vec3 o_color;
out vec4 out_color;

void main()
{
	out_color = vec4(o_color,1.0);
}
