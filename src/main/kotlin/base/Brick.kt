package base

abstract class Brick {

    abstract var remainingHits: Int
        protected set

    //Important for the Inheritance so we don't need to look up what type of brick it is
    abstract val companion: BrickCompanion

    fun hit() {
        remainingHits--

        //TODO: Change Appearance and play sound
    }

}

interface BrickCompanion {
    val points: Int
}