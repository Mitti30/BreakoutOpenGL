package base

class GreenBrick:Brick() {

    companion object:BrickCompanion{
        override val points: Int=5
    }

    override var remainingHits: Int=2
    override val companion: BrickCompanion=Companion

}