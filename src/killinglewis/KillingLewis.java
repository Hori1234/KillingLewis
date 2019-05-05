package killinglewis;

import static org.lwjgl.glfw.GLFW.*;


/**
 *
 * @author popacosmin
 */
public class KillingLewis implements Runnable {

    Thread graphicsThread;

    @Override
    public void run() {

    }

    private void start() {
        graphicsThread = new Thread(this, "Game Logic");
        glfwInit();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
