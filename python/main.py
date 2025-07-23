#!/usr/bin/env python3
#
import subprocess


def generate_bindings():
    uniffiLibraryFile  = "../rust/target/debug/liblibrust.so"
    generated_code_dir =  "../python/generated"
    print("> Generating bindings code")
    subprocess.run(["cargo", "build", "-p", "librust"], cwd="../rust", capture_output=True)
    subprocess.run(["cargo",
                    "run",
                    "-p",
                    "uniffi-bindgen",
                    "generate",
                    "--library",
                    uniffiLibraryFile,
                    "--language",
                    "python",
                    "--out-dir",
                    generated_code_dir,
                    "--no-format",
                    ],
                   cwd = "../uniffi-bindgen", capture_output=True)
    subprocess.run(["cp", uniffiLibraryFile, generated_code_dir], capture_output=True)
    print("< Generated bindings code")

def test_bindings():
    from generated.librust import MyStruct256, MyStruct128

    print("Testing the bindings")
    mystruct256 = MyStruct256()
    mystruct256.debug()
    mystruct256.debug()
    mystruct256.debug()

    print("Testing the 128 bindings")
    mystruct128 = MyStruct128()
    mystruct128.debug()
    mystruct128.debug()
    mystruct128.debug()



def main():
    generate_bindings()
    test_bindings()

if __name__ == "__main__":
    main()
