import com.booba.MemStack
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.FloatBuffer

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
