package example.com

import org.junit.Assert.*
import org.junit.Test
import uniffi.librust.MyStruct128
import uniffi.librust.MyStruct256

class ExampleUnitTest {

    @Test
    fun too_early_drop() {
        // this is fine
        val struct128 = MyStruct128()
        struct128.debug()
        struct128.debug()
        struct128.debug()
        // everything is fine and as expected up to this point

        val struct256 = MyStruct256()
        struct256.debug()
        // but here the problem arises! Why is struct256 dropped?
        struct256.debug()
        struct256.debug()

        // On purpose: Wrong to make output easier visible.
        assertEquals(4, 2 + 1)
    }
}
