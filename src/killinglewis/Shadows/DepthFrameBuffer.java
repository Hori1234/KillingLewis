package killinglewis.Shadows;
import static killinglewis.KillingLewis.WINDOW_HEIGHT;
import static killinglewis.KillingLewis.WINDOW_WIDTH;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import java.nio.ByteBuffer;

public class DepthFrameBuffer {
    /**width of shadow map**/
    private int width;
    /**height of shadow map**/
    private int height;
    /**frame buffer objects**/
    private int fbo;
    /**shadow map text**/
    private int shadow_map;

    protected DepthFrameBuffer(int width, int height){
        this.width = width;
        this.height = height;
        fbo = createFrameBuffer();
        shadow_map = genDepthAttachmentBuffer(width,height);
        unbindFB();
    }


    protected int getShadow_map(){
        return shadow_map;
    }

    protected void bindFB() {
        bindFB(fbo,width,height);
    }

    /**
     * unbinds framebuffer
     */
    protected  void unbindFB() {
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        glViewport(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    /**
     * delete framebuffer and shadow map
     */
    protected void delete() {
        glDeleteFramebuffers(fbo);
        glDeleteTextures(shadow_map);
    }


    /**
     * binds frame buffer
     * @param frameBuffer
     * @param width
     * @param height
     */
    private static void bindFB(int frameBuffer, int width, int height) {
        glBindTexture(GL_TEXTURE_2D,0);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
        glViewport(0,0,width,height);
    }

    /**
     * Creates a frame buffer
     * @return created framebuffer id
     */
    private static int createFrameBuffer() {
        int frameBuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,frameBuffer);
        glDrawBuffer(GL_NONE); //Draw buffer set to none because there's no color buffer rendered
        glReadBuffer(GL_NONE);
        return frameBuffer;
    }



    /**
     * Generates depth buffer texture
     * @param width
     * @param height
     * @return depth texture id.
     */
    private static int genDepthAttachmentBuffer(int width, int height) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,texture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_DEPTH_COMPONENT16,width,height,
                0, GL_DEPTH_COMPONENT,GL_FLOAT, (ByteBuffer)null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,texture,0);
        return texture;
    }

}
