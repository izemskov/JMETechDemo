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
    
    public void loadPlayer(AssetManager assetManager, PhysicsSpace physicsSpace, Node rootNode) {
        playerControl = new BetterCharacterControl(0.5f, 2, 1);
        playerControl.setJumpForce(new Vector3f(0, 20, 0));
        playerNode = new Node("Player");
        playerNode.addControl(playerControl);

        rootNode.attachChild(playerNode);

        physicsSpace.add(playerControl);

        playerControl.warp(new Vector3f(0, 5, 0));
    }

    public BetterCharacterControl getPlayerControl() {
        return playerControl;
    }

    public Node getPlayerNode() {
        return playerNode;
    }
}
