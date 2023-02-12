package ru.develgame.techdemo;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.TechniqueDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.post.filters.ToneMapFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.List;
import ru.develgame.techdemo.player.Player;
import ru.develgame.techdemo.player.TimedBullet;
import ru.develgame.techdemo.scene.MansionScene;
import ru.develgame.techdemo.scene.SimpleScene;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener {
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;

    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    
    private final List<TimedBullet> sceneBullets = new ArrayList<>();

    public static void main(String[] args) {
        Main app = new Main();

        AppSettings settings = new AppSettings(true);
        settings.setTitle("Tech demo");
        settings.setResolution(1280, 720);
        settings.setSamples(16);
        settings.setGammaCorrection(true);
        app.setSettings(settings);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Enable physics...
        BulletAppState bulletAppState = new BulletAppState();
        // bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);
        
        // Configure the scene for PBR
        getRenderManager().setPreferredLightMode(TechniqueDef.LightMode.SinglePassAndImageBased);
        getRenderManager().setSinglePassLightBatchSize(10);
        
        // Adjust to near frustum to a very close amount.
        float aspect = (float)cam.getWidth() / (float)cam.getHeight();
        cam.setFrustumPerspective(60, aspect, 0.1f, 1000);

        // change the viewport background color.
        viewPort.setBackgroundColor(new ColorRGBA(0.4f, 0.5f, 0.6f, 1.0f));
        
        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();
        
        // Add some lights
        DirectionalLight directionalLight = new DirectionalLight(
                new Vector3f(-1, -1, -1).normalizeLocal(),
                new ColorRGBA(1,1,1,1)
        );
        
        // Add some post-processor effects.
        initPostFx(directionalLight);
        
        // load scene and player
        MansionScene.getInstance().loadScene(assetManager, physicsSpace, rootNode);
        Player.getInstance().loadPlayer(assetManager, physicsSpace, rootNode, cam);
        
        setUpKeys();
    }
    
    private void initPostFx(DirectionalLight directionalLight) {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 4096, 3);
        dlsf.setLight(directionalLight);
        fpp.addFilter(dlsf);

        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        SSAOFilter ssaoFilter = new SSAOFilter();
        fpp.addFilter(ssaoFilter);

        FXAAFilter fxaaFilter = new FXAAFilter();
        fpp.addFilter(fxaaFilter);

        ToneMapFilter toneMapFilter = new ToneMapFilter();
        fpp.addFilter(toneMapFilter);

        viewPort.addProcessor(fpp);
    }
    
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Shoot");
    }

    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(12.6f);
        camLeft.set(cam.getLeft()).multLocal(12.4f);

        camDir.setY(0);
        camLeft.setY(0);

        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        Player.getInstance().getPlayerControl().setWalkDirection(walkDirection);

        cam.setLocation(Player.getInstance().getPlayerNode().getLocalTranslation().add(0, 15,0));
        
        float[] angles = new float[3];
        cam.getRotation().toAngles(angles);
        angles[0] = 0;
        angles[2] = 0;
        Player.getInstance().getPistolNode().setLocalRotation(new Quaternion().fromAngles(angles));

        cam.getRotation().toAngles(angles);
        angles[1] = 0;
        angles[2] = 0;
        Player.getInstance().getPistol().setLocalRotation(new Quaternion().fromAngles(angles));
        
        Player.getInstance().getSpot().setPosition(cam.getLocation());
        Player.getInstance().getSpot().setDirection(cam.getDirection());
        
        sceneBullets.removeIf(bullet -> {

            if (bullet.updateTime(tpf) > bullet.getMaxTime()) {
                bullet.getSpatial().removeFromParent();
                RigidBodyControl rigidBodyControl = bullet.getSpatial().getControl(RigidBodyControl.class);
                getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(rigidBodyControl);
                return true;
            }

            return false;
        });
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
            left = isPressed;
        } else if (binding.equals("Right")) {
            right = isPressed;
        } else if (binding.equals("Up")) {
            up = isPressed;
        } else if (binding.equals("Down")) {
            down = isPressed;
        } else if (binding.equals("Jump")) {
            if (isPressed) {
                Player.getInstance().getPlayerControl().jump();
            }
        } else if (binding.equals("Shoot") && !isPressed) {

            Geometry bullet = new Geometry("Bullet", new Sphere(32, 32, 0.1f));
            bullet.setMaterial(new Material(assetManager, "Common/MatDefs/Light/PBRLighting.j3md"));
            bullet.getMaterial().setColor("BaseColor", ColorRGBA.Yellow);
            bullet.getMaterial().setFloat("Metallic", 0.01f);
            bullet.getMaterial().setFloat("Roughness", 0.3f);
            rootNode.attachChild(bullet);

            sceneBullets.add(new TimedBullet(bullet, 10));

            RigidBodyControl rigidBodyControl = new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape(bullet), 0.5f);
            rigidBodyControl.setCcdMotionThreshold(.2f);
            rigidBodyControl.setCcdSweptSphereRadius(.2f);
            bullet.addControl(rigidBodyControl);

            stateManager.getState(BulletAppState.class).getPhysicsSpace().add(rigidBodyControl);

            // rigidBodyControl.setPhysicsLocation(cam.getLocation().add(cam.getDirection().mult(0.5f)));
            rigidBodyControl.setPhysicsLocation(Player.getInstance().getPistol().getWorldTranslation());
            rigidBodyControl.setPhysicsRotation(cam.getRotation());
            rigidBodyControl.applyImpulse(cam.getDirection().mult(20), new Vector3f());
        }
    }
}
