package net.mehvahdjukaar.heartstone.utils;

public class RenderHelper {
    /*
    public static void renderNoCamOffset(IRenderer renderer, MatrixStack stack, IRenderTypeBuffer buf, double camX, double camY, double camZ) {
        stack.push();
        Minecraft mc = Minecraft.getInstance();
        ActiveRenderInfo info = mc.gameRenderer.getActiveRenderInfo();
        Quaternion q = info.getRotation().copy();
        q.multiply(-1);
        stack.rotate(q);
        renderer.render(stack, buf, camX, camY, camZ);
        stack.pop();
    }

    public interface IRenderer {
        void render(MatrixStack stack, IRenderTypeBuffer buf, double camX, double camY, double camZ);
    }*/
}
