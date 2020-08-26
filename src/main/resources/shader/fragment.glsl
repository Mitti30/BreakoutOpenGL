#version 410
precision mediump float;

in lowp vec3 o_color;
in vec2 textureCoord;

out vec4 out_color;

uniform sampler2D brickNormalImage;
uniform sampler2D paddleImage;

void main()
{
	vec4 textureColor=texture2D(brickNormalImage, textureCoord);
	out_color = vec4(o_color,1.0) * textureColor;
}
