package example.com

import org.junit.Assert.*
import org.junit.Test
import uniffi.librust.librust.MyStruct256

class ExampleUnitTest {

    @Test
    fun too_early_drop() {

        val struct256 = MyStruct256.new_()
        struct256.debug()
        // but here the problem arises! Why is struct256 dropped?
        struct256.debug()
        struct256.debug()

        // On purpose: Wrong to make output easier visible.
        assertEquals(4, 2 + 1)
    }
}
