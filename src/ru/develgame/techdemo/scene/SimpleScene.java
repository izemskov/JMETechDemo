/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.develgame.techdemo.scene;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author izemskov
 */
public class SimpleScene implements Scene {
    private SimpleScene() {}
    
    private static SimpleScene instance = new SimpleScene();

    public static SimpleScene getInstance() {
        return instance;
    }

    @Override
    public void loadScene(AssetManager assetManager, PhysicsSpace physicsSpace, Node rootNode) {
        Node level = (Node) assetManager.loadModel("Scenes/SimpleScene.j3o");
        
        Spatial land = level.getChild("Land");
        RigidBodyControl landControl = new RigidBodyControl(CollisionShapeFactory.createMeshShape(land), 0);
        land.addControl(landControl);
        physicsSpace.add(landControl);
        
        Node sofa = (Node) level.getChild("SofaNode");
        RigidBodyControl rigidBodyControl = new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape(sofa), 50.0f);
        sofa.addControl(rigidBodyControl);
        rigidBodyControl.setAngularFactor(0);
        physicsSpace.add(rigidBodyControl);
        
        rootNode.attachChild(level);
    }
}
