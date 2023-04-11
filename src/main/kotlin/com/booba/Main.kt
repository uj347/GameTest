package com.booba
/* test */
import com.booba.animators.InfiniteDynamicAccelerationAnimator
import com.booba.animators.InfiniteGenericAnimator
import com.booba.canvas.Canvas
import com.booba.canvas.testcanvas.NaiveCanvas
import com.booba.canvasobjects.DebugNdcVertexPlaceableObject
import com.booba.hitboxes.PolygonHitbox
import com.booba.interactable.Interaction
import com.booba.vertexdata.Object2DVertexData
import com.booba.vertexdata.VertexDescription2D
import com.booba.canvasobjects.Placeable
import com.booba.canvasobjects.PlaceableObject2D
import com.booba.canvasobjects.SimplePlaceable2D
import com.booba.shaders.*
import com.booba.textures.TextureDescription
import com.booba.textures.TextureLoader
import com.booba.vertexdata.DebugVertexMeshData
import degreeToRads
import genId
import kotlinx.coroutines.flow.update
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30.*
import toDimensionF
import toVec3f
import toVec4f
import withBuf
import java.awt.Color
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.math.absoluteValue


fun main(){
//    tryLoadRes()
    val pic = "C:\\Shared\\testgame_assets\\Rocket.png"
    val out = "C:\\Shared\\testo.png"


    helloWorld()
}


fun tryLoadRes(){
    val res=ClassLoader.getSystemResource("VERTEX_SHADER").openStream()
    println(res.bufferedReader().readLine())
}

fun helloWorld(){

    val simpleShaderSpec=ResourceShaderProgramSpec(
        vertexShaderFileName = "VERTEX_SHADER",
        fragmentShaderFileName = "FRAGMENT_SHADER",
        listOf(
            transformMatrixSpec, worldTransformMatrixSpec,
            cameraTransformMatrixSpec, projectionTransformMatrixSpec,
            alphaMaskMatrixSpec, texture2dTextureSpec
        )

    )
    val hBoxVerts= TextureLoader.loadAlphaPolyHitbox("C:\\Shared\\testgame_assets\\Rocket.png",1f,16,16)
    val greenNdcShaderSpec=ResourceShaderProgramSpec(
        vertexShaderFileName = "NDC_VERTEX_SHADER",
        fragmentShaderFileName = "GREEN_COLOR_FRAGMENT_SHADER",
        listOf(
        )
    )
    val debugHitboxDisplayData=DebugVertexMeshData(
        hBoxVerts
    )
    val hBoxDebugDisplay= DebugNdcVertexPlaceableObject(debugHitboxDisplayData,greenNdcShaderSpec)
    var gravityAnimator:InfiniteDynamicAccelerationAnimator?=null
    val borderClampInteraction=Interaction(
        name="borderClamp",
        launchPredicate = {anothers, self ->
            ( self as Placeable).coordinates.let {
                it.first.absoluteValue>=1000||
                        it.second.absoluteValue>=1000 }
        },
        block = {anothers, self ->
            println("running border reach interaction with dimension ${(self as Placeable).coordinates}")
            self as Placeable
            val clampedCoords=self.coordinates.first.coerceIn(-1000f,1000f) to self.coordinates.second.coerceIn(-1000f,1000f)
            self.coordinates=clampedCoords
            //TODO This is shit
            gravityAnimator?.lastFrameSpeed=0f to 0f
        }
    )


    val textureLazy = lazy {
        val texture:TextureDescription =TextureLoader.loadTexture(
            TextureDescription.TextureType.TEXTURE,
//            "C:\\Shared\\GOSPODA.jpg"
            "C:\\Shared\\testgame_assets\\Rocket.png"
        )
        val alpha :TextureDescription = TextureLoader.loadTexture(
            TextureDescription.TextureType.ALPHA_MASK,
//            "C:\\Shared\\GOSPODA.jpg"
            "C:\\Shared\\testgame_assets\\Rocket.png"
        )

        listOf(
            texture,alpha
        )
    }





    val prepBloc= {


        true
    }
    val releaseBlock={

        true
    }

    val vertexCoordList = listOf<DimensionF>(
        (-0.5f to 0.5f), (0.5f to 0.5f) ,
        (-0.5f to -0.5f) ,(0.5f to -0.5f))
    val textureCoordList= listOf(
        (0f to 1f), (1f to 1f),
        (0f to 0f), (1f to 0f))
    val vertices=vertexCoordList.mapIndexed{ind,it->
        VertexDescription2D(it,textureCoordList[ind])
    }

    val triangleObj=Object2DVertexData(
        vertices= vertices,
        vertexEboOrder= listOf(0,1,2,2,3,1),
        resourcePreparation = prepBloc,
        resourceRelease =releaseBlock
    )

    val idMatrix=Matrix4f()
    val canvas=NaiveCanvas()



    val crossHair=SimplePlaceable2D(
        tags= listOf("CrossHair"),
        textureDataLazy=textureLazy,
        object2D = triangleObj,
        initProgram = simpleShaderSpec,
        initDimension =  100f to 100f,
        initCoordinates = 500f to 500f,
        initRotationDegree = 0f,
        initHitboxes = listOf()
    )


    val testPlaceable=SimplePlaceable2D(
        tags= listOf("Booba"),
        textureDataLazy=textureLazy,
        object2D = triangleObj,
        initProgram = simpleShaderSpec,
        initDimension =  400f to 400f,
        initCoordinates = 500f to 500f,
        initRotationDegree = 0f,
        initHitboxes = listOf(
//            WholeRectHitbox,
            PolygonHitbox(hBoxVerts)
        )
    ).apply {
        isInteractable=true
        addInteraction(borderClampInteraction)
    }
    gravityAnimator=InfiniteDynamicAccelerationAnimator(
        valueGetter = {testPlaceable.coordinates},
        valueSetter = {
            println("Setting new acceleration val $it")
            testPlaceable.coordinates=it
                      },
        initAccelerationUnitVector = 0f to -25f
    )
    testPlaceable.addAnimator(gravityAnimator,canvas)

//    TODO
//    gravityAnimator.launch()




    val borderReachThenClearInteraction=Interaction(
        name="borderReachThenClear",
        launchPredicate = {anothers, self ->
            ( self as Placeable).coordinates.let {
                it.first.absoluteValue>=1000||
                        it.second.absoluteValue>=1000 }
        },
        block = {anothers, self ->
            println("running border reach interaction with dimension ${(self as Placeable).coordinates}")
            canvas.removePlaceable(self as Placeable)
        }
    )

    val projCounter=AtomicInteger(0)
    val projectileProducer = {coord:DimensionF,dir:Float->
            SimplePlaceable2D(
                listOf("Projectile ${projCounter.getAndIncrement()}"),
                triangleObj,
                textureDataLazy = textureLazy,
                simpleShaderSpec,
                100f to 100f, coord, initRotationDegree = dir
            )
        }



    val actionMap:ActionMap= mapOf(
        GLFW.GLFW_KEY_LEFT to {_,_,->
            crossHair.translate(-3f,0f)
//            testPlaceable?.rotationDegreeState?.update { it+5 }
        },
        GLFW.GLFW_KEY_RIGHT to {_,_,->
            crossHair.translate(3f,0f)
//            testPlaceable?.rotationDegreeState?.update { it-5 }
        },
        GLFW.GLFW_KEY_UP to {_,_,->
//            setThrust(System.currentTimeMillis(),Thrust.UP, testPlaceable.rotationDegree){gravityAnimator.acceleration=it}
            crossHair.translate(0f,3f)
        },
        GLFW.GLFW_KEY_DOWN to {_,_,->
//                setThrust(System.currentTimeMillis(),Thrust.DOWN,testPlaceable.rotationDegree){gravityAnimator.acceleration=it}
            crossHair.translate(0f,-3f)

        },
        GLFW.GLFW_KEY_PAGE_UP to {_,_,->
            testPlaceable?.rotationDegreeState?.update { it+5 }
        },
        GLFW.GLFW_KEY_PAGE_DOWN to {_,_,->
            testPlaceable?.rotationDegreeState?.update { it-5 }
        },
        GLFW.GLFW_KEY_SPACE to {_,_,->
//            testPlaceable.fire(canvas,500f,projectileProducer,borderReachThenClearInteraction)
            val crosshairPolygon= arrayOf(
                crossHair.coordinates.first-crossHair.dimensions.first/2 to crossHair.coordinates.second+crossHair.dimensions.second/2,
                crossHair.coordinates.first+crossHair.dimensions.first/2 to crossHair.coordinates.second+crossHair.dimensions.second/2,
                crossHair.coordinates.first+crossHair.dimensions.first/2 to crossHair.coordinates.second-crossHair.dimensions.second/2,
                crossHair.coordinates.first-crossHair.dimensions.first/2 to crossHair.coordinates.second-crossHair.dimensions.second/2,

            )
            testPlaceable.checkHitPoly(crosshairPolygon).let{
                if(it)println("Poly hit detected on coordinates ${crossHair.coordinates}")
            }

            testPlaceable.checkHitPoint(crossHair.coordinates).let{
                if(it) println("Hit detecteed on coordinates ${crossHair.coordinates}")else{
                    println("No hit on coordinates ${crossHair.coordinates}")
                }
            }

        },
    )








    canvas.addPlaceable(testPlaceable)
    canvas.addPlaceable(crossHair)
    canvas.addPlaceable(hBoxDebugDisplay)
//    canvas.addInteraction(frameBorderInteraction)

    HelloWorldWindow(
    ).apply {
        actionMapState.update {
            it+actionMap
        }

                renderState.value={
                    listOf(CAMERA_TRANSFORM_LITERAL, PROJECTION_TRANSFORM_LITERAL).forEach {
                        simpleShaderSpec.setUniform(it,idMatrix)
                    }

                    //TODO




                    val currTime=System.currentTimeMillis()
                    canvas.drawFrame(currTime)
                    checkTimeout(currTime,testPlaceable.rotationDegree){gravityAnimator.acceleration=it}

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



fun genTexture(path:String):Pair<Int,Int>{
    var alphaMask=-1
    var texture=-1
    val file=path.let(::Path).toFile()
    var size=0
    var width=0
    var height=0
    val img=ImageIO.read(file)

    width=img.width
    height=img.height
    size=width*height*4
    withBuf(size){alphas->
    withBuf(size){bBuf->
        for(pixY in height-1 downTo  0){
            for (pixX in width-1 downTo 0){
                val (r,g,b,a)=Color(img.getRGB(pixX,pixY)).let{c->

                    arrayOf(
                        c.red.toByte(),
                        c.green.toByte(),
                        c.blue.toByte(),
                        c.alpha.toByte()
                    )
                }
                bBuf.put(r)
                bBuf.put(g)
                bBuf.put(b)
                bBuf.put(a)
                alphas.put(a)
                repeat(3){alphas.put(0)}

            }
        }
        bBuf.flip()
        alphas.flip()


        img.graphics.dispose()

        glActiveTexture(1)
        texture=glGenTextures()
        glBindTexture(GL_TEXTURE_2D,texture)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
            GL_UNSIGNED_BYTE,bBuf
        )

        glGenerateMipmap(GL_TEXTURE_2D)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)


        glActiveTexture(2)
         alphaMask=glGenTextures()
        glBindTexture(GL_TEXTURE_2D,texture)
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
            GL_UNSIGNED_BYTE,bBuf
        )
        glGenerateMipmap(GL_TEXTURE_2D)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP)

    }
    }
    if (texture==-1||alphaMask==-1){error("No texture initalized")}

        return texture to alphaMask
}

fun PlaceableObject2D.fire(
    canvas: Canvas,
    projectileSpeed:Float,
    projectileProducer:(DimensionF,Float)->PlaceableObject2D,
    frameBorderInteraction:Interaction
){
    val startPosition=coordinates
    val direction=rotationDegree
    val projectile=projectileProducer(startPosition,direction)
    projectile.isInteractable=true
    projectile.addInteraction(frameBorderInteraction)
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
const val tTout=100
var lastTCheckPoint:Long=0
fun checkTimeout(currTime:Long,directionAngle: Float,setterBlock:(DimensionF)->Unit){
    if(currTime- lastTCheckPoint> tTout){
     setThrust(currTime,Thrust.NONE,directionAngle,setterBlock)
    }
}
const val T_AMOUNT:Float= 400f
fun setThrust(currTime:Long,
              thrust: Thrust,
              directionAngle:Float,
              setterBlock:(DimensionF)->Unit){
    if(thrust!= Thrust.NONE){
        lastTCheckPoint=currTime
    }
    val rads=directionAngle.degreeToRads()
    val rotMat=Matrix3f().rotate(rads,Vector3f(0f,0f,1f))
//    if(lastThrust!=thrust)updateAcceleration(thrust, setterBlock)
   if(thrust== Thrust.NONE){
       setterBlock(thrust.acc)
   }else{
       val thr=thrust.acc.toVec3f().mul(rotMat).let{it.x to it.y}
       setterBlock(thr)
   }

    lastThrust=thrust
}

//fun updateAcceleration(thrust: Thrust,setterBlock:(DimensionF)->Unit){
//    setterBlock(thrust.acc)
//}
var lastThrust:Thrust=Thrust.NONE

enum class Thrust(val acc:DimensionF){
    NONE(0f to -100f),
    UP(0f to T_AMOUNT),
    DOWN(0f to -T_AMOUNT),
    LEFT(-T_AMOUNT to 0f),
    RIGHT(T_AMOUNT to 0f)
}