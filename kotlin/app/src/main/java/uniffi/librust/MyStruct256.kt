package uniffi.librust.librust;
import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure

internal interface MyStruct256Lib: Library {
    fun MyStruct256_destroy(handle: Pointer)
    fun MyStruct256_debug(handle: Pointer): Unit
    fun MyStruct256_new(): Pointer
}

class MyStruct256 internal constructor (
    internal val handle: Pointer,
    // These ensure that anything that is borrowed is kept alive and not cleaned
    // up by the garbage collector.
    internal val selfEdges: List<Any>,
)  {

    internal class MyStruct256Cleaner(val handle: Pointer, val lib: MyStruct256Lib) : Runnable {
        override fun run() {
            lib.MyStruct256_destroy(handle)
        }
    }

    companion object {
        internal val libClass: Class<MyStruct256Lib> = MyStruct256Lib::class.java
        internal val lib: MyStruct256Lib = Native.load("librust", libClass)
        @JvmStatic
        
        fun new_(): MyStruct256 {
            
            val returnVal = lib.MyStruct256_new();
            val selfEdges: List<Any> = listOf()
            val handle = returnVal 
            val returnOpaque = MyStruct256(handle, selfEdges)
            CLEANER.register(returnOpaque, MyStruct256.MyStruct256Cleaner(handle, MyStruct256.lib));
            return returnOpaque
        }
    }
    
    fun debug(): Unit {
        
        val returnVal = lib.MyStruct256_debug(handle);
        
    }

}