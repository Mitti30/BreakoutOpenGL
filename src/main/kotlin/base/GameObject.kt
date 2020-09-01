package base

import glm_.vec2.Vec2
import glm_.vec3.Vec3ui
import gln.identifiers.GlTexture
import helper.Vertex

abstract class GameObject(var position:Vec2,var dimension:Vec2) {

    abstract var vertices:Array<Vertex>

    abstract val companion: GameObjectCompanion
}

interface GameObjectCompanion{
    val texture:GlTexture
    val color:Vec3ui
    val mode:ColorMode
    val initialDimensions:Vec2
}

enum class ColorMode{
    BLEND,COLOR,Texture
}