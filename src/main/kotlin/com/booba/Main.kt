package com.booba
/* test */
import com.booba.animators.InfiniteGenericAnimator
import com.booba.biggun.TestGun
import com.booba.canvas.Canvas
import com.booba.canvas.testcanvas.NaiveCanvas
import com.booba.interactable.Interaction
import com.booba.objects.ColoredObject2D
import com.booba.objects.TexturedObject2D
import com.booba.placeable.PlaceableObject2D
import com.booba.placeable.SimplePlaceable2D
import com.booba.shaders.*
import degreeToRads
import genId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*
import toDimensionF
import toVec4f
import withBuf
import java.awt.Color
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.math.absoluteValue


fun main(){
//    tryLoadRes()
    helloWorld()
}


fun tryLoadRes(){
    val res=ClassLoader.getSystemResource("VERTEX_SHADER").openStream()
    println(res.bufferedReader().readLine())
}

fun helloWorld(){

    val shaderSpec=ResourceShaderProgram(
        listOf(
            transformMatrixSpec, worldTransformMatrixSpec, cameraTransformMatrixSpec, projectionTransformMatrixSpec
        )

    )
    val triangleColor=ColoredObject2D(
        listOf(
            (-0.9f to 0f) to Color.YELLOW,
            (0f to 0.9f) to Color.YELLOW,
            (0.9f to 0f) to Color.YELLOW,

        )
    )
    var testGun:TestGun?=null
    val texture:Int by lazy{genTexture("C:\\Shared\\GOSPODA.jpg")}
    val prepBloc= {
        texture
        true
    }
    val releaseBlock={
//        GL11.glDeleteTextures(texture!!)
        true
    }
    val triangleTexture=TexturedObject2D(
        vertexMapping = listOf<Pair<DimensionF,DimensionF>>(
//            (-1f to 0f) to (-0.5f to 0f),
//            (0f to 1f) to (0f to 1f),
//            (1f to 0f) to (1f to 0f),
            (-0.5f to 0.5f) to (0f to 1f),
            (0.5f to 0.5f) to (1f to 1f),
            (-0.5f to -0.5f) to (0f to 0f),
            (-0.5f to -0.5f) to (0f to 0f),
            (0.5f to 0.5f) to (1f to 1f),
            (0.5f to -0.5f) to (1f to 0f),
            ),
        resourcePreparation = prepBloc,
        resourceRelease =releaseBlock
    )

    val idMatrix=Matrix4f()


    val placeable=SimplePlaceable2D(
        tags= listOf("Booba"),
        object2D = triangleTexture,
        initProgram = shaderSpec,
        initDimension =  150f to 150f,
        initCoordinates = 500f to 500f,
        initRotationDegree = 0f
    )


    val canvas=NaiveCanvas()

    val projCounter=AtomicInteger(0)
    val projectileProducer = {coord:DimensionF,dir:Float->
            SimplePlaceable2D(
                listOf("Projectile ${projCounter.getAndIncrement()}"),
                triangleTexture,shaderSpec,
                100f to 100f, coord, initRotationDegree = dir
            )
        }



    val actionMap:ActionMap= mapOf(
        GLFW.GLFW_KEY_LEFT to {_,_,->
            placeable?.translate(-10f,0f)
        },
        GLFW.GLFW_KEY_RIGHT to {_,_,->
            placeable?.translate(10f,0f)
        },
        GLFW.GLFW_KEY_UP to {_,_,->
            placeable?.translate(0f,10f)
        },
        GLFW.GLFW_KEY_DOWN to {_,_,->
            placeable?.translate(0f,-10f)
        },
        GLFW.GLFW_KEY_PAGE_UP to {_,_,->
            placeable?.rotationDegreeState?.update { it+5 }
        },
        GLFW.GLFW_KEY_PAGE_DOWN to {_,_,->
            placeable?.rotationDegreeState?.update { it-5 }
        },
        GLFW.GLFW_KEY_SPACE to {_,_,->
            placeable.fire(canvas,500f,projectileProducer)
            canvas.addPlaceable(placeable)
        },
    )


//    val objAnimator=InfiniteFloatAnimator(
//        deltaTimeMapper = produceLinearDeltaTimeFloatMapper(180f),
//        valueGetter = {placeable.rotationDegreeState.value},
//        valueSetter = {newVal->
//            placeable.rotationDegreeState.update { newVal }}
//    )
//    val timerObjAnimator= FiniteFloatAnimator(
//        stopPredicate= produceTimerPredicate(5000) ,
//        underlyingAnimator = objAnimator
//    )

//    objAnimator.launch()
//    timerObjAnimator.launch()
    testGun



    val frameBorderInteraction=Interaction(
        name="borderReach",
        launchPredicate = {anothers, self ->
            anothers.filterIsInstance<PlaceableObject2D>().any {
                it.coordinates.first.absoluteValue>=1000||
                    it.coordinates.second.absoluteValue>=1000 }
        },
        block = {anothers, self ->
            println("Border reach interaction running")
            val canvas=(self as com.booba.canvas.Canvas)
            anothers
                .filterIsInstance<PlaceableObject2D>()
                .filter { it.coordinates.first.absoluteValue>=1000||
                        it.coordinates.second.absoluteValue>=1000 }
                .forEach {
                    println("Removing placeable from canvas: ${it.tags.joinToString()}")
                    canvas.removePlaceable(it) }
        }
    )



    //TODO
//    = TestGun(
//        gunObject = SimplePlaceable2D(
//            triangleTexture,shaderSpec,
//            150f to 150f, 500f to 500f,0f
//
//        ),
//        projectileProducer = {coord,dir->
//            SimplePlaceable2D(
//                triangleTexture,shaderSpec,
//                100f to 100f, coord, initRotationDegree = dir
//
//            )
//        },
//        projectileSpeed = 2560f
//    )

    //TODO




    canvas.addPlaceable(placeable)
    canvas.addInteraction(frameBorderInteraction)

    HelloWorldWindow(shaderSpec =shaderSpec
    ).apply {
        actionMapState.update {
            it+actionMap
        }

                renderState.value={programId->
                    listOf(CAMERA_TRANSFORM_LITERAL, PROJECTION_TRANSFORM_LITERAL).forEach {
                        shaderSpec.setUniform(it,idMatrix)
                    }

                    canvas.drawFrame(System.currentTimeMillis())


                  //TODO()
//                    testGun?.drawComplex(System.currentTimeMillis())




                //                    timerObjAnimator.commitTime(System.currentTimeMillis())
//                    placeable.draw()
                }

                    println("Render state invoked!!!")
            run()

    }


}

fun PlaceableObject2D.translate(x:Float, y:Float){
    this.coordinatesState.update {old->
        old.first+x to old.second+y
    }
}

fun MutableStateFlow<Matrix4f>.translate(x:Float, y:Float, step:Float){
    update {m4->
        Matrix4f(m4.translate(x*step,y*step,0f))
    }
}


fun genTexture(path:String):Int{
    var res=-1
    val file=path.let(::Path).toFile()
    var size=0
    var width=0
    var height=0
    val img=ImageIO.read(file)

    width=img.width
    height=img.height
    size=width*height*3
    withBuf(size){bBuf->



        for(pixY in height-1 downTo  0){
            for (pixX in width-1 downTo 0){

                val (r,g,b)=Color(img.getRGB(pixX,pixY)).let{c->
                    arrayOf(
                        c.red.toByte(),
                        c.green.toByte(),
                        c.blue.toByte()
                    )
                }
                bBuf.put(r)
                bBuf.put(b)
                bBuf.put(g)
            }
        }
        bBuf.flip()

        img.graphics.dispose()
        res=glGenTextures()
        glBindTexture(GL_TEXTURE_2D,res)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB,
            GL_UNSIGNED_BYTE,bBuf
        )

        glGenerateMipmap(GL_TEXTURE_2D)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)

    }
    if (res==-1){error("No texture initalized")}

        return res
}

fun PlaceableObject2D.fire(
    canvas: Canvas,
    projectileSpeed:Float,
    projectileProducer:(DimensionF,Float)->PlaceableObject2D
){
    val startPosition=coordinates
    val direction=rotationDegree
    val projectile=projectileProducer(startPosition,direction)
    canvas.addPlaceable(projectile)
        val projAnimator=object: InfiniteGenericAnimator<Vector4f>(){
            override val id: Long=genId()
            override val deltaTimeMapper: (Long) -> Vector4f ={ delta->
                val units=delta*0.001f*projectileSpeed
                val vec= Vector4f(0f,units,0f,1f)
                val matrix=Matrix4f()
                    .rotate(direction.degreeToRads(), Vector3f(0f,0f,1f))
                val resVec= matrix.transformAffine(vec)
                resVec

            }
            override val valueAdder: (old: Vector4f, deltaT: Vector4f) -> Vector4f= { old, deltaT ->
            old.add(deltaT)
            }
            override val valueGetter: () -> Vector4f={
                projectile.coordinates.toVec4f()
            }
            override val valueSetter: (Vector4f) -> Unit= {
                projectile.coordinates=it.toDimensionF()
            }
        }
        projectile.addAnimator(projAnimator,canvas)
        projectile.launchAnimators(canvas)


}