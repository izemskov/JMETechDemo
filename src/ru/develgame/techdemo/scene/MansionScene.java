/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.develgame.techdemo.scene;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author izemskov
 */
public class MansionScene implements Scene {
    private MansionScene() {}
    
    private static MansionScene instance = new MansionScene();

    public static MansionScene getInstance() {
        return instance;
    }

    @Override
    public void loadScene(AssetManager assetManager, PhysicsSpace physicsSpace, Node rootNode) {
        Node level = (Node) assetManager.loadModel("Scenes/Mansion.j3o");
        
        for (Spatial elem : level.getChildren()) {
            if (elem.getName().equals("Sofa07")) {
                RigidBodyControl rigidBodyControl = new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape(elem), 100.0f);
                elem.addControl(rigidBodyControl);
                rigidBodyControl.setAngularFactor(0);
                physicsSpace.add(rigidBodyControl);
            }
            else {
                RigidBodyControl control = new RigidBodyControl(CollisionShapeFactory.createMeshShape(elem), 0);
                elem.addControl(control);
                physicsSpace.add(control);
            }
        }    
        
        rootNode.attachChild(level);
    }
    
}
