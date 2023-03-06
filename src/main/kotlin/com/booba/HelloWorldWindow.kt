package com.booba

import kotlinx.coroutines.flow.MutableStateFlow
import org.lwjgl.Version
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback


class HelloWorldWindow(
    private val header:String="NoName",
    private val dimension:Dimension=720 to 480,
) {
    private var window:Long?=null
     val actionMapState:MutableStateFlow<ActionMap> = MutableStateFlow( mapOf(GLFW_KEY_ESCAPE to { _, _->close()}))


    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!")
        init()
        loop()
        window?.let{
            Callbacks.glfwFreeCallbacks(it)
            glfwDestroyWindow(it)
            // Terminate GLFW and free the error callback
            glfwTerminate()
            glfwSetErrorCallback(null)!!.free()
        }
        // Free the window callbacks and destroy the window

    }

    fun close() {
        window?.let {
            glfwSetWindowShouldClose(
                it,
                true
            )
        }
    }

    private fun init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize  Most GLFW functions will not work before doing this.
        check(glfwInit()) { "Unable to initialize GLFW" }

//        glEnable(0);
        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        // Create the window
        window = glfwCreateWindow(dimension.first, dimension.second, header, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window!!) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if(key in actionMapState.value&&action==GLFW_RELEASE){
                actionMapState.value[key]?.invoke(window,key)
            }
        }
        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window!!, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                window!!,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window!!)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window!!)
    }



    private fun loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities()

        // Set the clear color
        GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
        //        drawLine();
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window!!)) {

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
            // clear the framebuffer
//            gl;

            glfwSwapBuffers(window!!)
            // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
        }
    }


}