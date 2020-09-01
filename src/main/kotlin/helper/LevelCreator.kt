package helper

import base.Brick
import base.BrickCompanion
import base.bricks.GreenBrick
import base.bricks.OrangeBrick
import base.bricks.RedBrick
import base.bricks.YellowBrick
import glm_.func.common.floor
import glm_.vec2.Vec2
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.createInstance

//https://www.gamasutra.com/view/feature/1630/breaking_down_breakout_system_and_.php?print=1

class LevelCreator(private val maxWidth: Float,private val maxHeight: Float, private val colCount: Int, private val rowCount: Int,private val topX:Float,private val topY:Float) {
   // https://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
/*
    val brickRandomizer=RandomSelector()

    class RandomSelector {
        private var items=Brick.subclasses
        private var totalSum = items.sumBy { selector->
            if(selector.companionObjectInstance!=null)
            return@sumBy(selector.companionObjectInstance as BrickCompanion).points
            else return@sumBy 0
        }
       private val seed=Random.nextInt()
        val random: Brick
            get() {
                val index: Int = Random(seed).nextInt(totalSum)
                var sum = 0
                var i = 0
                while (sum < index) {
                    sum += totalSum-(items[i++].companionObjectInstance as BrickCompanion).points
                }
                return items[Math.max(0, i - 1)].createInstance()
            }
    }

*/

    fun createWall():ArrayList<Brick>{
        val space=maxWidth*0.05
        val list=ArrayList<Brick>()
        val spaceBetweenCount=colCount-1
        val height=30f
        val outerSpace=height*8+spaceBetweenCount*space

        var remainingWidth=maxWidth-space
        val width=remainingWidth/colCount

        var x=topX
        var y=topY



        for (i in 1..8){

            for (e in 1..colCount){
                when(i){
                    in 1..2 -> list.add(RedBrick(Vec2(x,y), Vec2(width,height)))
                    in 3..4 -> list.add(OrangeBrick(Vec2(x,y), Vec2(width,height)))
                    in 5..6 -> list.add(GreenBrick(Vec2(x,y), Vec2(width,height)))
                    in 7..8 -> list.add(YellowBrick(Vec2(x,y), Vec2(width,height)))
                }


            }

        }
        return list
    }

}