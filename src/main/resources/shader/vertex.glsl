#version 410
layout(location = 0) in vec2 v_position;
layout(location = 1) in vec3 v_color;

out vec3 o_color;

void main()
{
	gl_Position = vec4(v_position,0.0,1.0);
	o_color = v_color;
}