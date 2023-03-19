import com.booba.MemStack
import kotlinx.coroutines.flow.MutableStateFlow
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.FloatBuffer
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


fun Vector3f.getFloatValuesByteBuf():ByteBuffer{
    val bbuf=ByteBuffer.allocateDirect(3*Float.SIZE_BYTES)
    get(bbuf)
    return bbuf
}

fun Vector3f.getFloatValuesFloatBuf():FloatBuffer=getFloatValuesByteBuf().asFloatBuffer()


operator fun <T> MutableStateFlow<T>.getValue(caller:Any?,prop:KProperty<*>):T =  this.value
operator fun <T> MutableStateFlow<T>.setValue(caller:Any?,prop:KProperty<*>,newValue:T) = {
    println("Caller: $caller wth prop: ${prop.name} and new val : $newValue")
    this.value=newValue
}


const val dp:Float=0.001f