package org.example;

/**
 *
 */
public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.example.VolatileTest", true, new UserClassLoader());
    }

    private static class UserClassLoader extends ClassLoader {
        public UserClassLoader() {
            super();
        }
    }
}
