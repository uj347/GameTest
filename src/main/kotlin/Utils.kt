import com.booba.DimensionF
import com.booba.MemStack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KProperty

fun <T>withMemStack(block:MemoryStack.()->T):T{
    val res=
    MemStack.stackPush().use {
        it.block()
    }
   return res
}

fun <T>withBuf(size:Int,block:(buf:ByteBuffer)->T):T{
    val buf=MemoryUtil.memAlloc(size)
   val res= block(buf)
    buf.clear()
    MemoryUtil.memFree(buf)
    return res
}

fun FloatBuffer.put(vararg vals:Float)=put(vals)

fun Matrix4f.getFloatValuesArr():FloatArray=FloatArray(16).let{ arr-> get(arr)}

fun Matrix4f.getFloatValuesByteBuf():ByteBuffer{
    val bbuf=ByteBuffer.allocateDirect(16*Float.SIZE_BYTES)
    this.get(bbuf)
    return bbuf
}
fun Matrix4f.getFloatValuesFloatBuf():FloatBuffer=getFloatValuesByteBuf().asFloatBuffer()

fun DimensionF.toVec4f()=Vector4f(first,second,0f,1f)
fun DimensionF.toVec3f()=Vector3f(first,second,0f,)
fun DimensionF.toVec2f()= Vector2f(first,second,)

fun Vector4f.toDimensionF()=x to y

fun Vector3f.getFloatValuesByteBuf():ByteBuffer{
    val bbuf=ByteBuffer.allocateDirect(3*Float.SIZE_BYTES)
    get(bbuf)
    return bbuf
}

fun Vector3f.getFloatValuesFloatBuf():FloatBuffer=getFloatValuesByteBuf().asFloatBuffer()

fun DimensionF.transformPointWith(tMat:Matrix4f):DimensionF{
    return tMat.transformAffine(Vector4f(first,second,0f,1f)).let{it.x to it.y}
}

operator fun <T> MutableStateFlow<T>.getValue(caller:Any?,prop:KProperty<*>):T =  this.value
operator fun <T> MutableStateFlow<T>.setValue(caller:Any?,prop:KProperty<*>,newValue:T) {
    println("Caller: $caller wth prop: \n${prop.name} oldVal is:\n ${this.value} and new val :\n $newValue")
    value=newValue
}


fun Float.degreeToRads():Float=toDouble().let(Math::toRadians).toFloat()

private val idCounter= AtomicLong(0)
fun genId():Long=idCounter.getAndIncrement()



const val dp:Float=0.001f