/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ru.develgame.techdemo.player;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author izemskov
 */
public class Player {
    private Player() {}
    
    private static Player instance = new Player();
    
    public static Player getInstance() {
        return instance;
    }
    
    private BetterCharacterControl playerControl;
    private Node playerNode;
    private Spatial pistol;
    private Node pistolNode;
    
    public void loadPlayer(AssetManager assetManager, PhysicsSpace physicsSpace, Node rootNode) {
        playerControl = new BetterCharacterControl(0.5f, 2, 1);
        playerControl.setJumpForce(new Vector3f(0, 20, 0));
        playerNode = new Node("Player");
        playerNode.addControl(playerControl);

        rootNode.attachChild(playerNode);

        physicsSpace.add(playerControl);

        playerControl.warp(new Vector3f(0, 5, 0));
        
        pistol = assetManager.loadModel("Models/Pistol.gltf.j3o");

        pistolNode = new Node("Pistol Node");
        pistolNode.attachChild(pistol);
        pistol.setLocalTranslation(-.2f, 14.9f, .3f);
        playerNode.attachChild(pistolNode);
    }

    public BetterCharacterControl getPlayerControl() {
        return playerControl;
    }

    public Node getPlayerNode() {
        return playerNode;
    }

    public Spatial getPistol() {
        return pistol;
    }

    public Node getPistolNode() {
        return pistolNode;
    }
}
