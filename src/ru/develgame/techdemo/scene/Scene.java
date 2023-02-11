/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ru.develgame.techdemo.scene;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.scene.Node;

/**
 *
 * @author izemskov
 */
public interface Scene {
    void loadScene(AssetManager assetManager, PhysicsSpace physicsSpace, Node rootNode);
}
