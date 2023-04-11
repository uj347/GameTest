package com.booba

import com.booba.shaders.ShaderProgramSpec
import kotlinx.coroutines.flow.MutableStateFlow
import org.lwjgl.Version
//import org.lwjgl.opengl.GL

import  org.lwjgl.opengl.GL33.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL

import org.lwjgl.system.Configuration
import withMemStack


class HelloWorldWindow(
    private val header:String="NoName",
    private val dimension:Dimension=720 to 480,
//    private val shaderSpec: ShaderProgramSpec
) {

    private val pressedKeys= mutableSetOf<Int>()

    private var window:Long?=null
     val actionMapState:MutableStateFlow<ActionMap> = MutableStateFlow( mapOf(GLFW_KEY_ESCAPE to { _, _->close()}))

    val renderState= MutableStateFlow<(()->Unit)?>(null)

    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!")
        initGl()
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


    private fun initGl() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize  Most GLFW functions will not work before doing this.
        check(glfwInit()) { "Unable to initialize GLFW" }


        // Configure GLFW

        glfwDefaultWindowHints() // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)


        // Create the window
        window = glfwCreateWindow(dimension.first, dimension.second, header, MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window!!) { window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if(key in actionMapState.value&&action== GLFW_PRESS) {
            pressedKeys.add(key)
            }
            if(key in actionMapState.value&&action==GLFW_RELEASE){
               pressedKeys.remove(key)
            }
//            actionMapState.value[key]?.invoke(window,key)

        }
        MemStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window!!, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())
"glfw version: ${glfwGetVersionString()} ".let(::println)

            // Center the window
            glfwSetWindowPos(
                window!!,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window!!)
        Configuration.DEBUG.set(true)
        Configuration.DEBUG_STREAM.set(System.out)
        glfwGetVersionString()
        GL_CURRENT_COLOR

//        val callBack=object:
            glfwSetFramebufferSizeCallback(window!!,
                {window,w,h->
                    println("Window resize callback")
                    glViewport(0,0,w,h)
                })

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
        GL.createCapabilities().apply {


            this.OpenGL46.let (::println)
        }

//
//        shaderSpec.compile().let{res->
//            if(!res) error("Program not created!!")
//        }

//        glEnableClientState(GL_VERTEX_ARRAY)

        glDisable(GL_DEPTH_TEST)
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // Set the clear color

        //        drawLine();
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
//        glUseProgram(shaderSpec.programId!!)
        while (!glfwWindowShouldClose(window!!)) {

            glClear(GL_COLOR_BUFFER_BIT )
            glClearColor(0f, 0f, 0.5f, 0.0f)
//            renderTriangle()
            // clear the framebuffer
//            gl;
//            if(renderState.value==null) println("Null renderstate")
            renderState.value?.invoke()
            glfwSwapBuffers(window!!)
            // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents()
            pressedKeys.forEach {key->
                actionMapState.value[key]?.invoke(window!!,key)
            }

        }
    }






}