package com.booba.textures

import com.booba.DimensionF
import org.joml.Vector2f
import org.lwjgl.opengl.GL30
import withBuf
import java.awt.Color
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.math.*


object TextureLoader{


    private val _registry:MutableSet<TextureDescription> = mutableSetOf<TextureDescription>()
    val registry:Set<TextureDescription>
        get() = _registry

    fun loadTexture(type:TextureDescription.TextureType,path:String):TextureDescription{
        return _registry.firstOrNull { it.src==path&&it.type==type }?:run{
           val id= when(type){
                TextureDescription.TextureType.ALPHA_MASK-> loadAlphaMask(path)
                else-> wildJavaTextureLoading(path)
            }
            val res=TextureDescription(path,id,type)
           _registry.add(res)
            res
        }
    }





////////////////////////////////////////
    fun loadAlphaPolyHitbox(path:String, hitboxThreshold: Float, dimensionX:Int, dimensionY:Int ):Array<DimensionF>{
    val string= produceStringIndex(path, hitboxThreshold, dimensionX, dimensionY)
    val res=hitboxRegistry.computeIfAbsent(string){
        internalLoadAlphaPolyHitbox(path, hitboxThreshold, dimensionX, dimensionY)
    }
    return res
    }

    private fun produceStringIndex(path:String,hitboxThreshold: Float, dimensionX:Int, dimensionY:Int):String{
        return "$path$%$hitboxThreshold*$dimensionX*$dimensionY"
    }

    private val hitboxRegistry= mutableMapOf<String,Array<DimensionF>>()

    private fun internalLoadAlphaPolyHitbox(path:String, hitboxThreshold: Float, dimensionX:Int, dimensionY:Int ):Array<DimensionF>{
        val file=path.let(::Path).toFile()
        var width=0
        var height=0
        val img= ImageIO.read(file)
        width=img.width
        height=img.height
        val arr= IntArray(4)
        //TRUE- xValsByYInd ;FALSE - yValsByXInd; Int - row\column number
        val indexMap= mutableMapOf<Boolean,MutableMap<Float,FloatArray>>()

        for(pixYi in height-1 downTo  0){
            val pixYf=pixYi.toFloat()
            val lineX= FloatArray(2){-1f}
            var xCheck=false
            for (pixXi in width-1 downTo 0){
                val pixXf=pixXi.toFloat()
                Color(img.getRGB(pixXi,pixYi)).let{ c->
                    img.raster.getPixel(pixXi,pixYi,arr)
                    if(arr[3].toFloat()/255f>=hitboxThreshold){
                        xCheck=true
                        if(lineX[0]==-1f){
                            lineX[0]=pixXf
                        }
                        lineX[1]=pixXf
                        //yLineMagic
                        val yEntries=indexMap.getOrPut(false){
                            mutableMapOf()
                        }
                        yEntries[pixXf]?.let{presentArr->
                            presentArr[1]=pixYf
                        }?:run{
                            yEntries[pixXf]= floatArrayOf(pixYf,pixYf)
                        }
                    }
                }
                if(xCheck){
                   val xEntries= indexMap.getOrPut(true ){
                        mutableMapOf()
                    }
                    xEntries[pixYf]=lineX
                }
            }
        }
        val xChunk=width/dimensionX
        val yChunk=height/dimensionY


//        val (xS,yS)=indexMap.map { (isX,valMap)->
//            if(isX){
//                valMap.entries.sortedBy { it.key }
//            }else{
//
//            }
//
//        }

        val reduced=indexMap.flatMap {(isX,valMap)->
            if(isX ==true){
                //xs by y
             val xRes= valMap
                 .entries
                 .sortedBy { it.key }
                 .chunked(yChunk){list->
                     val res=list
                         .flatMap { listOf( it.value[0] to it.key, it.value[1] to it.key)}
                         .sortedBy { it.first }
                     listOf( res.first(),res.last())
                 }

            xRes.flatten()
            }else{
                //ys by x
              val yRes=valMap
                  .entries
                  .sortedBy { it.key }
                  .chunked(xChunk){list->
                      val res=list
                          .flatMap { listOf( it.key to it.value[0] ,it.key to  it.value[1])}
                          .sortedBy { it.second }
                      listOf( res.first(),res.last())
                  }
                yRes.flatten()
            }
        }
        val ndcs=reduced.map { (x,y)->
            val reverseY=abs(height-y)
            val normX=(4*x-2*width)/(2*width)
            val normY=(4*reverseY-2*height)/(2*height)
            normX to normY
        }
        val sort=ndcs.sortedBy {(x,y)->

                val arc=when{
                x in 0f..1f->{
                    asin(y)
                }
                    x<0-> PI.toFloat()- asin(y)
                    else ->error("BLDZHAD")
                }
            val vecL=Vector2f(x,y).length()
            val k=PI/6*((1f- vecL))
            val res=1.6*arc+k
            res
        }

//        reduced.forEach {
//            img.setRGB(it.first.toInt(),it.second.toInt(),Color(255,255,255).rgb)
//        }
//        ImageIO.write(img,"png", File("C:\\Shared\\testo.png"))

        img.graphics.dispose()
        return sort.toTypedArray()
    }


////////////////////////////////////////////////////////



    private fun loadAlphaMask(path:String):Int{
        var alpha=-1
        val file=path.let(::Path).toFile()
        var size=0
        var width=0
        var height=0
        val img= ImageIO.read(file)
        width=img.width
        height=img.height
        size=width*height
//        withBuf(size){alphas->
        withBuf(size){bBuf->
            for(pixY in height-1 downTo  0){
                for (pixX in width-1 downTo 0){
                    val a= Color(img.getRGB(pixX,pixY)).let{
                        it.alpha.toByte()
                    }
                    bBuf.put(a)
            }
            bBuf.flip()
            img.graphics.dispose()
            GL30.glActiveTexture(GL30.GL_TEXTURE0)
            alpha= GL30.glGenTextures()
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, alpha)
            GL30.glTexImage2D(
                GL30.GL_TEXTURE_2D, 0, GL30.GL_RED, width, height, 0, GL30.GL_ALPHA,
                GL30.GL_UNSIGNED_BYTE, bBuf
            )

            GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D)
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP)

        }

        if (alpha==-1){error("No texture initalized")}


    }
        return alpha
    }


    private fun wildJavaTextureLoading(path:String):Int{

        var texture=-1
        val file=path.let(::Path).toFile()
        var size=0
        var width=0
        var height=0
        val img= ImageIO.read(file)
        width=img.width
        height=img.height
        size=width*height*4*Float.SIZE_BYTES

            withBuf(size){bBuf->
                val floatBuf=bBuf.asFloatBuffer()
                for(pixY in height-1 downTo  0){
                    val arr= IntArray(4,)
                    for (pixX in width-1 downTo 0){
                        val (r,g,b,a)= Color(img.getRGB(pixX,pixY)).let{ c->

                            img.raster.getPixel(pixX,pixY,arr)
                            arrayOf(
                                arr[0].toFloat()/255f,
                                arr[1].toFloat()/255f,
                                arr[2].toFloat()/255f,
                                arr[3].toFloat()/255f
                            )

                        }
                        floatBuf.put(r)
                        floatBuf.put(g)
                        floatBuf.put(b)
                        floatBuf.put(a)
                    }
                }
                floatBuf.flip()
                img.graphics.dispose()
                GL30.glActiveTexture(GL30.GL_TEXTURE0)
                texture= GL30.glGenTextures()
                GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture)
                GL30.glTexImage2D(
                    GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width, height, 0, GL30.GL_RGBA,
                    GL30.GL_FLOAT, floatBuf
                )

                GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D)
                GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP)
  }
        if (texture==-1

        ){error("No texture initalized")}

        return texture
    }
}