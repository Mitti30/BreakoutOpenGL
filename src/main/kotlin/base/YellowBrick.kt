package base

class YellowBrick: Brick() {

    companion object:BrickCompanion{
        override val points=3
    }

    override var remainingHits: Int=3

    override val companion=Companion

}