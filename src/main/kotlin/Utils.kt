import com.booba.MemStack
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.FloatBuffer

fun <T>withMemStack(block:MemoryStack.()->T):T{
   return MemStack.stackPush().block()
}

fun withBuf(size:Int,block:(buf:ByteBuffer)->Unit){
    val buf=MemoryUtil.memAlloc(size)
    block(buf)
    MemoryUtil.memFree(buf)
}

fun FloatBuffer.put(vararg vals:Float)=put(vals)
