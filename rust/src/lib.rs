uniffi::setup_scaffolding!();

use std::arch::x86_64::{__m128i, __m256i, _mm_set1_epi8, _mm256_set1_epi8};

#[derive(uniffi::Object)]
pub struct MyStruct256(__m256i);

/// This is the problematic struct:
/// it gets dropped before its end of life...
#[uniffi::export]
impl MyStruct256 {
    pub fn debug(&self) {
        dbg!(&self.0);
    }

    #[uniffi::constructor]
    pub fn new() -> Self {
        dbg!("New 256");
        Self(unsafe { _mm256_set1_epi8(1) })
    }
}

impl Drop for MyStruct256 {
    fn drop(&mut self) {
        dbg!("I'm just dropping 256!");
        // Simulate zeroizing?
        self.0 = unsafe { _mm256_set1_epi8(0) }
    }
}

/// Everything is fine for this struct...
#[derive(uniffi::Object)]
pub struct MyStruct128(__m128i);

#[uniffi::export]
impl MyStruct128 {
    #[uniffi::constructor]
    pub fn new() -> Self {
        dbg!("New 128");
        Self(unsafe { _mm_set1_epi8(1) })
    }

    pub fn debug(&self) {
        dbg!(&self.0);
    }
}

impl Drop for MyStruct128 {
    fn drop(&mut self) {
        dbg!("I'm just dropping 128!");
        // Simulate zeroizing?
        self.0 = unsafe { _mm_set1_epi8(0) }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    /// In Rust, everything is fine: this code runs as expected and only drops after the second debug
    #[test]
    fn test_run() {
        let my_struct = MyStruct256::new();
        my_struct.debug();
        my_struct.debug();
    }
}
