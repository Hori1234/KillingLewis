package killinglewis.utils;

import static org.lwjgl.opengl.GL20.*;

public class ShaderLoader {

    /**
     * Loads shader program from shader paths.
     *
     * @param vertexPath path of vertex shader
     * @param fragmentPath path of fragment shader
     * @return program ID
     */
    public static int load(String vertexPath, String fragmentPath) {
        return createProgram(FileLoader.load(vertexPath), FileLoader.load(fragmentPath));
    }

    /**
     * Creates shader program.
     *
     * @param vertexSource vertex shader as string
     * @param fragmentSource fragment shader as string
     * @return program ID
     */
    private static int createProgram(String vertexSource, String fragmentSource) {
        int program = glCreateProgram();

        // compile vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        // check if any errors occurred while compiling vertex shader
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader failed to compile.");
            System.err.println(glGetShaderInfoLog(vertexShader));
        }


        // compile fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        // check if any errors occurred while compiling fragment shader
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader failed to compile.");
            System.err.println(glGetShaderInfoLog(fragmentShader));
        }

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        glValidateProgram(program);

        return program;
    }


}
